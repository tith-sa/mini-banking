package spring.minibanksystem.service.implementation

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import spring.minibanksystem.dto.ResponsePagination
import spring.minibanksystem.dto.ResponseSuccess
import spring.minibanksystem.dto.request.TransactionRequest
import spring.minibanksystem.dto.request.TransactionSearchRequest
import spring.minibanksystem.dto.response.TransactionResponse
import spring.minibanksystem.handleException.BadRequestException
import spring.minibanksystem.handleException.ResourceNotFoundException
import spring.minibanksystem.model.Transaction
import spring.minibanksystem.model.enum.TransactionStatus
import spring.minibanksystem.model.enum.TransactionType
import spring.minibanksystem.repository.AccountRepository
import spring.minibanksystem.repository.TransactionRepository
import spring.minibanksystem.repository.specification.TransactionSpecification
import spring.minibanksystem.service.interfaceService.TransactionService
import spring.minibanksystem.service.AuthorizationService
import spring.minibanksystem.service.TransactionStatusService
import spring.minibanksystem.util.validationAmountLimited

@Service
class TransactionServiceImpl(
    private val transactionRepo: TransactionRepository,
    private val accountRepo: AccountRepository,
    private val authorizationService: AuthorizationService,
    private val transactionStatusService: TransactionStatusService,
) : TransactionService {

    override fun transfer(
        userId: Long,
        request: TransactionRequest
    ): ResponseSuccess<TransactionResponse> {

        //destructuring declaration
        val (fromAccount, toAccount, amount) = request

        // 1. Find sender
        val senderAccount = accountRepo.findByAccountNumber(fromAccount)
            ?: throw ResourceNotFoundException("Sender account not found")

        // 2. Check ownership
        authorizationService.validateOwner(senderAccount, userId)

        // 3. Validate amount
        if (amount > senderAccount.balance) {
            throw BadRequestException("Don't have enough balance")
        }

        // 4. Find receiver
        val receiverAccount = accountRepo.findByAccountNumber(toAccount)
            ?: throw ResourceNotFoundException("Receiver account not found")

        // 5. Cannot transfer to yourself
        if (senderAccount.accountNumber == receiverAccount.accountNumber) {
            throw BadRequestException("Cannot transfer to this account")
        }


        // validate limit min/max amount
        val currency = senderAccount.currency
            ?: throw BadRequestException("Account currency is required")
        amount.validationAmountLimited(currency)


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
        val savedTransfer = transactionStatusService.firstSave(transfer)

        try {
            // 12. SECOND TRANSACTION
            // PENDING → SUCCESS
            savedTransfer.id?.let { transactionStatusService.secondSave(
                    it,
                    senderAccount,
                    receiverAccount
                )
            }

        } catch (e: Exception) {

            // 13. Separate transaction
            // PENDING → FAILED
            savedTransfer.id?.let { transactionStatusService.thirdSave(it) }

            throw e
        }

        val response = TransactionResponse(
            savedTransfer.id,
            savedTransfer.fromAccount,
            savedTransfer.toAccount,
            savedTransfer.currency,
            savedTransfer.amount,
            savedTransfer.type,
            TransactionStatus.COMPLETED,
            savedTransfer.createdAt
        )

        return ResponseSuccess(
            data = response,
            message = "Transfer successfully"
        )
    }


    // Get transaction history for a specific account with pagination
    override fun historyTransaction(
        userId: Long,
        accountNumber: String,
        page: Int,
        size: Int
    ): ResponseSuccess<ResponsePagination <TransactionResponse>> {
        // Find the account using the provided account number.
        // If the account does not exist, throw a ResourceNotFound exception.
       val account = accountRepo.findByAccountNumber(accountNumber)
            ?: throw ResourceNotFoundException("Account not found")

        // Verify that the authenticated user owns the requested account.
        authorizationService.validateOwner(account, userId)

        // Validate that the page number starts from 1.
        if (page < 1) {
            throw BadRequestException("Page must be 1 or greater")
        }

        // Validate that the size page minimum 1.
        if (size < 1) {
            throw BadRequestException("Size must be 1 or greater")
        }

        // Create a Pageable object.
        // PageRequest uses zero-based indexing, so subtract 1 from the page number.
        val pageable = PageRequest.of(
            page - 1,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        )

        // Find transactions where the account is either
        // the sender (fromAccount) or receiver (toAccount).
        // The pageable object limits the results and provides pagination metadata.
        val history = transactionRepo.findByFromAccountOrToAccount(
            account.accountNumber,
            account.accountNumber,
            pageable
        )

        // Convert Transaction entities into TransactionResponse DTOs.
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

        // Build pagination metadata using information from the Page object.
        val pagination = ResponsePagination(
            ResponsePagination.ResponsePageMeta(
                history.number + 1,
                history.size,
                history.totalElements,
                history.totalPages,
            ),
            transaction,
        )

        return ResponseSuccess(
            data = pagination,
            message = "Display transaction history"
        )

    }

    override fun getTransaction(
        userId: Long,
        accountNumber: String,
        id: Long
    ): ResponseSuccess<TransactionResponse> {

        // find account number
        val account = accountRepo.findByAccountNumber(accountNumber)
            ?: throw ResourceNotFoundException("Account not found")

        // validate owner
        authorizationService.validateOwner(account, userId)

        // Build the JPA Specification find transaction by id and account number
        val spec = TransactionSpecification.findByIdAndAccounts(id,account.accountNumber)
        val transaction = transactionRepo.findOne(spec)
            .orElseThrow {
                ResourceNotFoundException("Transaction not found")
            }

        // Convert Transaction entities into TransactionResponse DTOs
        val response = TransactionResponse(
           transaction.id,
            transaction.fromAccount,
            transaction.toAccount,
            transaction.currency,
            transaction.amount,
            transaction.type,
            transaction.status,
            transaction.createdAt
        )

        return ResponseSuccess(
            data = response,
            message = "Transaction successfully"
        )
    }


    // search transaction function
    override fun searchTransactionHistory(
        userId: Long,
        request: TransactionSearchRequest,
        page: Int,
        size: Int
    ): ResponseSuccess<ResponsePagination<TransactionResponse>> {

        if (page < 1) {
            throw BadRequestException("Page must be 1 or greater")
        }
        if (size < 1) {
            throw BadRequestException("Size must be 1 or greater")
        }
        val pageable = PageRequest.of(
            page - 1,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        )

        // Build the JPA Specification based on the search filters
        // provided in the request, such as account number, status, transaction type, and date range.
        val spec = TransactionSpecification.buildSpecification(userId, request)

        // Execute the query using the generated Specification
        // and retrieve all transactions that match the filters.
        val transactions = transactionRepo.findAll(spec, pageable)

        // Convert each Transaction entity into a TransactionResponse DTO
        // before returning the data to the client.
        // Convert Transaction entities into TransactionResponse DTOs and map data.
        val transactionResponses = transactions.content.map {
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

        // Use pagination information from Page<Transaction>
        val pagination = ResponsePagination(
            ResponsePagination.ResponsePageMeta(
                transactions.number + 1,
                transactions.size,
                transactions.totalElements,
                transactions.totalPages,
            ),
            transactionResponses,
        )

        // Return the transaction responses with a success message.
        return ResponseSuccess(
            data = pagination,
            message = "Search Transaction history"
        )
    }
}