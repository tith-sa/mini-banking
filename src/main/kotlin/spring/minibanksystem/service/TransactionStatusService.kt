package spring.minibanksystem.service


import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import spring.minibanksystem.handleException.BadRequestException
import spring.minibanksystem.handleException.ResourceNotFoundException
import spring.minibanksystem.model.Account
import spring.minibanksystem.model.Transaction
import spring.minibanksystem.model.enum.TransactionStatus
import spring.minibanksystem.repository.AccountRepository
import spring.minibanksystem.repository.TransactionRepository
import spring.minibanksystem.util.exchangeAmount
import kotlin.plus

@Service
class TransactionStatusService(
    val transactionRepo: TransactionRepository,
    val accountRepo: AccountRepository,
) {

    // Starts a completely new database transaction.
    //
    // REQUIRES_NEW means this method gets its own transaction,
    // even if the method that calls it is already inside another transaction.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun firstSave(transaction: Transaction): Transaction {
        // Change the transaction status to PENDING.
        transaction.status = TransactionStatus.PENDING
        // Save the transaction to the database.
        // This allows the PENDING status to be committed separately.
        return transactionRepo.save(transaction)
    }


    // Starts a new independent transaction.
    // Used when a transaction should be marked as COMPLETED.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun secondSave(transactionId: Long, senderAccount: Account, receiverAccount: Account): Transaction {

        // Search for the transaction using its ID.
        // findById() returns Optional<Transaction>,
        // so orElseThrow() is used if no transaction exists.
        val transaction = transactionRepo.findById(transactionId)
        .orElseThrow{
            throw ResourceNotFoundException("Transaction not fount")
        }

        // 6. Convert currency
        val senderCurrency = senderAccount.currency
            ?: throw BadRequestException("Sender account does not have a valid currency")
        val receiverCurrency = receiverAccount.currency
            ?: throw BadRequestException("Receiver account does not have a valid currency")
        val convertedAmount = transaction.amount?.exchangeAmount(
            senderCurrency,
            receiverCurrency
        )
        // 9. Update sender
        senderAccount.balance =
            transaction.amount?.let { senderAccount.balance?.minus(it) }

        // 10. Update receiver
        receiverAccount.balance =
            convertedAmount?.let { receiverAccount.balance?.plus(it) }

        // 11. Save accounts
        accountRepo.save(senderAccount)
        accountRepo.save(receiverAccount)

        // Change the transaction status to COMPLETE.
        transaction.status = TransactionStatus.COMPLETED
        return transactionRepo.save(transaction)
    }


    // Starts a new independent transaction.
    // Used when a transaction should be marked as FAILED.
    fun thirdSave(transactionId: Long): Transaction {
        val transaction = transactionRepo.findById(transactionId)
            .orElseThrow{
                ResourceNotFoundException("Transaction not found?")
            }
        // Change the transaction status to FAILED.
        transaction.status = TransactionStatus.FAILED
        return transactionRepo.save(transaction)
    }
}