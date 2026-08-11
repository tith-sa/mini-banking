package spring.minibanksystem.service


import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import spring.minibanksystem.handleException.HandleException
import spring.minibanksystem.model.Transaction
import spring.minibanksystem.model.enum.TransactionStatus
import spring.minibanksystem.repository.TransactionRepository

@Service
class TransactionStatusService(
    val transactionRepo: TransactionRepository,
) {

    // Starts a completely new database transaction.
    //
    // REQUIRES_NEW means this method gets its own transaction,
    // even if the method that calls it is already inside another transaction.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun savePending(transaction: Transaction): Transaction {
        // Change the transaction status to PENDING.
        transaction.status = TransactionStatus.PENDING
        // Save the transaction to the database.
        // This allows the PENDING status to be committed separately.
        return transactionRepo.save(transaction)
    }


    // Starts a new independent transaction.
    // Used when a transaction should be marked as COMPLETED.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveSuccess(transactionId: Long?): Transaction {
        // Make sure the transaction ID was provided.
        if (transactionId == null) {
            throw HandleException.ResourceNotFound("Transaction not found")
        }

        // Search for the transaction using its ID.
        // findById() returns Optional<Transaction>,
        // so orElseThrow() is used if no transaction exists.
        val transaction = transactionRepo.findById(transactionId)
        .orElseThrow{
            throw HandleException.ResourceNotFound("Transaction not fount")
        }
        // Change the transaction status to COMPLETE.
        transaction.status = TransactionStatus.COMPLETED
        return transactionRepo.save(transaction)
    }


    // Starts a new independent transaction.
    // Used when a transaction should be marked as FAILED.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveFailed(transactionId: Long?): Transaction {
        if (transactionId == null) {
            throw HandleException.ResourceNotFound("Transaction not found")
        }
        val transaction = transactionRepo.findById(transactionId)
            .orElseThrow{
                HandleException.ResourceNotFound("Transaction not found?")
            }
        // Change the transaction status to FAILED.
        transaction.status = TransactionStatus.FAILED
        return transactionRepo.save(transaction)
    }
}