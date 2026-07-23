package spring.minibanksystem.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.minibanksystem.model.Transaction

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByFromAccountOrToAccount(fromAccount: String,toAccount: String, pageable: Pageable) : Page<Transaction>
}