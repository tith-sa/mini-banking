package spring.minibanksystem.service.impl

import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import spring.minibanksystem.dto.ResponseDto
import spring.minibanksystem.dto.request.TransactionRequest
import spring.minibanksystem.dto.response.TransactionResponse
import spring.minibanksystem.handleException.HandleException
import spring.minibanksystem.model.Transaction
import spring.minibanksystem.model.enum.TransactionType
import spring.minibanksystem.repository.AccountRepository
import spring.minibanksystem.repository.TransactionRepository
import spring.minibanksystem.service.TransactionService
import spring.minibanksystem.service.AuthorizationService
import spring.minibanksystem.util.exchangeAmount
import spring.minibanksystem.util.toSuccess
import spring.minibanksystem.util.validationAmountLimited

@Service
class TransactionServiceImpl(
    private val transactionRepo: TransactionRepository,
    private val accountRepo: AccountRepository,
    private val authorizationService: AuthorizationService
) : TransactionService {

    @Transactional
    override fun transfer(userId: Long,request: TransactionRequest): ResponseDto<TransactionResponse> {
        val (fromAccount, toAccount, amount) = request

        val senderAccount = accountRepo.findByAccountNumber(fromAccount)
            ?: throw HandleException.ResourceNotFound("Account not found")
        authorizationService.validateOwner(senderAccount, userId)
        if (amount > senderAccount.balance) {
            throw HandleException.BadRequest("Don't have enough balances")
        }
        amount.validationAmountLimited(senderAccount.currency)
        val receiverAccount = accountRepo.findByAccountNumber(toAccount)
            ?: throw HandleException.ResourceNotFound("Account not found")
        if (senderAccount.accountNumber == receiverAccount.accountNumber) {
            throw HandleException.BadRequest("Cannot transfer to this account")
        }

        val convertAmount = amount.exchangeAmount(senderAccount.currency, receiverAccount.currency)

        senderAccount.balance -= amount
        receiverAccount.balance += convertAmount
        accountRepo.save(senderAccount)
        accountRepo.save(receiverAccount)

        val transfer = Transaction(
            senderAccount,
            receiverAccount,
            senderAccount.currency,
            amount,
            TransactionType.TRANSFER
        )
        transactionRepo.save(transfer)

        return TransactionResponse(
            transfer.id,
            transfer.fromAccount.accountNumber,
            transfer.toAccount.accountNumber,
            transfer.currency,
            transfer.amount,
            transfer.type,
            transfer.createdAt
        ).toSuccess(
            message = "Transfer Successful"
        )
    }

    override fun historyTransaction(userId: Long,accountNumber: String, page: Int, size: Int): ResponseDto<Page<TransactionResponse>>{
       val account = accountRepo.findByAccountNumber(accountNumber)
            ?: throw HandleException.ResourceNotFound("Account not found")
        authorizationService.validateOwner(account, userId)
        val pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        )

        val history = transactionRepo.findByFromAccountOrToAccount(
            account,
            account,
            pageable
        )

        val response = history.map {
            TransactionResponse(
                it.id,
                it.fromAccount.accountNumber,
                it.toAccount.accountNumber,
                it.currency,
                it.amount,
                it.type,
                it.createdAt
            )
        }

        return response.toSuccess(message = "Display transaction history")

    }

    override fun getTransaction(userId: Long,accountNumber: String,id: Long): ResponseDto<TransactionResponse> {
        val account = accountRepo.findByAccountNumber(accountNumber)
            ?: throw HandleException.ResourceNotFound("Account not found")
        authorizationService.validateOwner(account, userId)

        val transaction = transactionRepo.findByFromAccountOrToAccountAndId(account,id)
            .orElseThrow {
                HandleException.ResourceNotFound("Transaction not found")
            }

        return TransactionResponse(
           transaction.id,
            transaction.fromAccount.accountNumber,
            transaction.toAccount.accountNumber,
            transaction.currency,
            transaction.amount,
            transaction.type,
            transaction.createdAt
        ).toSuccess(
            message = "Get transaction Successful"
        )
    }
}