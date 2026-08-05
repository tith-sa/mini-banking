package spring.minibanksystem.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.minibanksystem.model.Account
import spring.minibanksystem.model.Transaction
import java.util.Optional

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByFromAccountOrToAccount(fromAccount: Account, toAccount: Account, pageable: Pageable) : Page<Transaction>
}