package spring.minibanksystem.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.web.PagedModel
import org.springframework.stereotype.Repository
import spring.minibanksystem.dto.ResponsePagination
import spring.minibanksystem.model.Account
import spring.minibanksystem.model.Transaction
import java.util.Optional

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    fun findByFromAccountOrToAccount(fromAccount: String?, toAccount: String?, pageable: Pageable) : Page<Transaction>
}