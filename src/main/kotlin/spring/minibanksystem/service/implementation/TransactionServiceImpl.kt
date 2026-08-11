package spring.minibanksystem.service.implementation

import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import spring.minibanksystem.dto.ResponsePageMeta
import spring.minibanksystem.dto.ResponsePagination
import spring.minibanksystem.dto.ResponseSuccess
import spring.minibanksystem.dto.request.TransactionRequest
import spring.minibanksystem.dto.response.TransactionResponse
import spring.minibanksystem.handleException.HandleException
import spring.minibanksystem.model.Transaction
import spring.minibanksystem.model.enum.TransactionStatus
import spring.minibanksystem.model.enum.TransactionType
import spring.minibanksystem.repository.AccountRepository
import spring.minibanksystem.repository.TransactionRepository
import spring.minibanksystem.service.interfaceService.TransactionService
import spring.minibanksystem.service.AuthorizationService
import spring.minibanksystem.service.TransactionStatusService
import spring.minibanksystem.util.exchangeAmount
import spring.minibanksystem.util.toSuccess
import spring.minibanksystem.util.validationAmountLimited
import java.math.BigDecimal

@Service
class TransactionServiceImpl(
    private val transactionRepo: TransactionRepository,
    private val accountRepo: AccountRepository,
    private val authorizationService: AuthorizationService,
    private val transactionStatusService: TransactionStatusService,
) : TransactionService {

    @Transactional
    override fun transfer(
        userId: Long?,
        request: TransactionRequest
    ): ResponseSuccess<TransactionResponse> {

        val (fromAccount, toAccount, amount) = request

        // 1. Find sender
        val senderAccount = accountRepo.findByAccountNumber(fromAccount)
            ?: throw HandleException.ResourceNotFound("Sender account not found")

        // 2. Check ownership
        authorizationService.validateOwner(senderAccount, userId)

        // 3. Validate amount
        if (amount == null || amount <= BigDecimal.ZERO) {
            throw HandleException.BadRequest("Amount must be greater than 0")
        }

        if (amount > senderAccount.balance) {
            throw HandleException.BadRequest("Don't have enough balance")
        }

        amount.validationAmountLimited(senderAccount.currency)

        // 4. Find receiver
        val receiverAccount = accountRepo.findByAccountNumber(toAccount)
            ?: throw HandleException.ResourceNotFound("Receiver account not found")

        // 5. Cannot transfer to yourself
        if (senderAccount.accountNumber == receiverAccount.accountNumber) {
            throw HandleException.BadRequest("Cannot transfer to this account")
        }

        // 6. Convert currency
        val convertedAmount = amount.exchangeAmount(
            senderAccount.currency,
            receiverAccount.currency
        )

        // 7. Create transaction
        val transfer = Transaction(
            fromAccount = senderAccount.accountNumber,
            toAccount = receiverAccount.accountNumber,
            currency = senderAccount.currency,
            amount = amount,
            type = TransactionType.TRANSFER,
            status = TransactionStatus.PENDING
        )

        // 8. FIRST TRANSACTION
        // PENDING is committed immediately
        val savedTransfer = transactionStatusService.savePending(transfer)

        try {
            // 9. Update sender
            senderAccount.balance =
                senderAccount.balance?.minus(amount)

            // 10. Update receiver
            receiverAccount.balance =
                receiverAccount.balance?.plus(convertedAmount)

            // 11. Save accounts
            accountRepo.save(senderAccount)
            accountRepo.save(receiverAccount)

            // 12. SECOND TRANSACTION
            // PENDING → SUCCESS
            transactionStatusService.saveSuccess(savedTransfer.id)

        } catch (e: Exception) {

            // 13. Separate transaction
            // PENDING → FAILED
            transactionStatusService.saveFailed(savedTransfer.id)

            throw e
        }

        return TransactionResponse(
            savedTransfer.id,
            savedTransfer.fromAccount,
            savedTransfer.toAccount,
            savedTransfer.currency,
            savedTransfer.amount,
            savedTransfer.type,
            TransactionStatus.COMPLETED,
            savedTransfer.createdAt
        ).toSuccess(
            message = "Transfer Successful"
        )
    }

    override fun historyTransaction(userId: Long?,accountNumber: String?, page: Int, size: Int): ResponseSuccess<ResponsePagination <TransactionResponse>> {
       val account = accountRepo.findByAccountNumber(accountNumber)
            ?: throw HandleException.ResourceNotFound("Account not found")
        authorizationService.validateOwner(account, userId)
        if (page < 1) {
            throw HandleException.BadRequest("Page must be 1 or greater")
        }
        val pageable = PageRequest.of(
            page - 1,
            size
        )

        val history = transactionRepo.findByFromAccountOrToAccount(
            account.accountNumber,
            account.accountNumber,
            pageable
        )

        val transaction = history.content.map {
            TransactionResponse(
                it.id,
                it.fromAccount,
                it.toAccount,
                it.currency,
                it.amount,
                it.type,
                it.status,
                it.createdAt
            )
        }

        val pagination = ResponsePagination(
            ResponsePageMeta(
                history.number + 1,
                history.size,
                history.totalElements,
                history.totalPages,
            ),
            transaction,
        )
        return pagination.toSuccess(message = "Display transaction history")

    }

    override fun getTransaction(
        userId: Long?,
        accountNumber: String?,
        id: Long?
    ): ResponseSuccess<TransactionResponse> {
        if (userId == null) {
            throw HandleException.BadRequest("User ID is required")
        }
        if (id == null) {
            throw HandleException.BadRequest("User ID is required")
        }
        if (accountNumber == null) {
            throw HandleException.BadRequest("Account number is required")
        }
        val account = accountRepo.findByAccountNumber(accountNumber)
            ?: throw HandleException.ResourceNotFound("Account not found")
        authorizationService.validateOwner(account, userId)

        val transaction = transactionRepo.findByFromAccountOrToAccountAndId(account.accountNumber,id)
            .orElseThrow {
                HandleException.ResourceNotFound("Transaction not found")
            }

        return TransactionResponse(
           transaction.id,
            transaction.fromAccount,
            transaction.toAccount,
            transaction.currency,
            transaction.amount,
            transaction.type,
            transaction.status,
            transaction.createdAt
        ).toSuccess(
            message = "Get transaction Successful"
        )
    }
}