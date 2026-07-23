package spring.minibanksystem.service.impl

import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import spring.minibanksystem.dto.ResponseDto
import spring.minibanksystem.dto.request.TransactionRequest
import spring.minibanksystem.dto.response.TransactionResponse
import spring.minibanksystem.model.Transaction
import spring.minibanksystem.model.enum.TransactionType
import spring.minibanksystem.repository.AccountRepository
import spring.minibanksystem.repository.TransactionRepository
import spring.minibanksystem.service.TransactionService
import spring.minibanksystem.util.toSuccess

@Service
class TransactionServiceImpl(
    private val transactionRepo: TransactionRepository,
    private val accountRepo: AccountRepository
) : TransactionService {

    override fun deposit(request: TransactionRequest): ResponseDto<TransactionResponse> {
        val (_, toAccount, amount) = request

        if (toAccount.isNullOrBlank()) {
            throw IllegalArgumentException("receiver account is required")
        }

        val account = accountRepo.findByAccountNumber(toAccount)
            ?: throw IllegalStateException("No account found")

        account.balance += amount
        accountRepo.save(account)

        val deposit = Transaction(
            null,
            toAccount,
            account.currency,
            amount,
            TransactionType.DEPOSIT
        )
        transactionRepo.save(deposit)

        return TransactionResponse(
            deposit.id,
            deposit.fromAccount,
            deposit.toAccount,
            deposit.currency,
            deposit.amount,
            deposit.type,
            deposit.createdAt
        ).toSuccess(
            message = "Deposit Successful"
        )
    }

    override fun withdraw(request: TransactionRequest): ResponseDto<TransactionResponse> {
        val (fromAccount,_,amount) = request
        if (fromAccount.isNullOrBlank()) {
            throw IllegalArgumentException("Sender account is required")
        }
        val account = accountRepo.findByAccountNumber(fromAccount)
        ?: throw IllegalStateException("Account Not found")
        if (amount > account.balance) {
            throw IllegalArgumentException("Don't have enough balances")
        }
        account.balance -= amount
        accountRepo.save(account)
        val withdraw = Transaction(
            fromAccount,
            null,
            account.currency,
            amount,
            TransactionType.WITHDRAW
        )
        transactionRepo.save(withdraw)

        return TransactionResponse(
            withdraw.id,
            withdraw.fromAccount,
            withdraw.toAccount,
            withdraw.currency,
            withdraw.amount,
            withdraw.type,
            withdraw.createdAt
        ).toSuccess(
            message = "Withdraw Successful"
        )
    }

    @Transactional
    override fun transfer(request: TransactionRequest): ResponseDto<TransactionResponse> {
        val (fromAccount, toAccount, amount) = request
        if (fromAccount.isNullOrBlank()) {
            throw IllegalArgumentException("Sender account is required")
        }
        if (toAccount.isNullOrBlank()) {
            throw IllegalArgumentException("Sender account is required")
        }

        val senderAccount = accountRepo.findByAccountNumber(fromAccount)
            ?: throw IllegalStateException("Account not found")
        if (amount > senderAccount.balance) {
            throw IllegalArgumentException("Don't have enough balances")
        }
        val receiverAccount = accountRepo.findByAccountNumber(toAccount)
            ?: throw IllegalStateException("Account not found")
        if (senderAccount == receiverAccount) {
            throw IllegalArgumentException("Cannot transfer to this account")
        }

        senderAccount.balance -= amount
        receiverAccount.balance += amount
        accountRepo.save(senderAccount)
        accountRepo.save(receiverAccount)

        val transfer = Transaction(
            fromAccount,
            toAccount,
            senderAccount.currency,
            amount,
            TransactionType.TRANSFER
        )
        transactionRepo.save(transfer)

        return TransactionResponse(
            transfer.id,
            transfer.fromAccount,
            transfer.toAccount,
            transfer.currency,
            transfer.amount,
            transfer.type,
            transfer.createdAt
        ).toSuccess(
            message = "Transfer Successful"
        )
    }

    override fun historyTransaction(accountNumber: String, page: Int, size: Int): ResponseDto<Page<TransactionResponse>>{
        accountRepo.findByAccountNumber(accountNumber)
            ?: throw IllegalStateException("Account not found")
        val pageable: Pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        )

        val history = transactionRepo.findByFromAccountOrToAccount(
            accountNumber,
            accountNumber,
            pageable
        )

        val response = history.map {
            TransactionResponse(
                it.id,
                it.fromAccount,
                it.toAccount,
                it.currency,
                it.amount,
                it.type,
                it.createdAt
            )
        }

        return response.toSuccess(message = "Display transaction history")

    }
}