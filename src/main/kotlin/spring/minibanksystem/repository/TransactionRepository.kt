package spring.minibanksystem.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import spring.minibanksystem.model.Account
import spring.minibanksystem.model.Transaction
import java.util.Optional

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByFromAccountOrToAccount(fromAccount: Account, toAccount: Account, pageable: Pageable) : Page<Transaction>

    @Query ("""
    SELECT t
    FROM Transaction t
    WHERE t.id = :id
    AND (
        t.fromAccount = :account
        OR t.toAccount = :account
    )
    """)
    fun findByFromAccountOrToAccountAndId(account: Account, id: Long): Optional<Transaction>
}