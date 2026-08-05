package spring.minibanksystem.service.impl

import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import spring.minibanksystem.dto.ResponseDto
import spring.minibanksystem.dto.request.TransactionRequest
import spring.minibanksystem.dto.response.TransactionResponse
import spring.minibanksystem.model.Transaction
import spring.minibanksystem.model.enum.CurrencyType
import spring.minibanksystem.model.enum.TransactionType
import spring.minibanksystem.repository.AccountRepository
import spring.minibanksystem.repository.TransactionRepository
import spring.minibanksystem.service.TransactionService
import spring.minibanksystem.util.exchangeAmount
import spring.minibanksystem.util.toSuccess
import spring.minibanksystem.util.validationAmountLimited
import java.math.BigDecimal

@Service
class TransactionServiceImpl(
    private val transactionRepo: TransactionRepository,
    private val accountRepo: AccountRepository
) : TransactionService {

    @Transactional
    override fun transfer(request: TransactionRequest): ResponseDto<TransactionResponse> {
        val (fromAccount, toAccount, amount) = request

        val senderAccount = accountRepo.findByAccountNumber(fromAccount)
            ?: throw IllegalStateException("Account not found")
        if (amount > senderAccount.balance) {
            throw IllegalArgumentException("Don't have enough balances")
        }
        amount.validationAmountLimited(senderAccount.currency)
        val receiverAccount = accountRepo.findByAccountNumber(toAccount)
            ?: throw IllegalStateException("Account not found")
        if (senderAccount.accountNumber == receiverAccount.accountNumber) {
            throw IllegalArgumentException("Cannot transfer to this account")
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

    override fun historyTransaction(accountNumber: String, page: Int, size: Int): ResponseDto<Page<TransactionResponse>>{
       val historyTransaction = accountRepo.findByAccountNumber(accountNumber)
            ?: throw IllegalStateException("Account not found")
        val pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        )

        val history = transactionRepo.findByFromAccountOrToAccount(
            historyTransaction,
            historyTransaction,
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
}