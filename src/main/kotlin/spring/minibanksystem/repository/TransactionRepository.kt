package spring.minibanksystem.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.web.PagedModel
import org.springframework.stereotype.Repository
import spring.minibanksystem.dto.ResponsePagination
import spring.minibanksystem.model.Account
import spring.minibanksystem.model.Transaction
import java.util.Optional

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByFromAccountOrToAccount(fromAccount: String?, toAccount: String?, pageable: Pageable) : Page<Transaction>

    @Query ("""
    SELECT t
    FROM Transaction t
    WHERE t.id = :id
    AND (
        t.fromAccount = :account
        OR t.toAccount = :account
    )
    """) // JPQL
    fun findByFromAccountOrToAccountAndId(account: String?, id: Long?): Optional<Transaction>
}