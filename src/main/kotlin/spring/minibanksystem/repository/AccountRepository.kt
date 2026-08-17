package spring.minibanksystem.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.minibanksystem.model.Account
import java.util.Optional

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
    fun existsByAccountNumber(accountNumber: String?): Boolean
    fun findByOwnerId(ownerId: Long?): List<Account>
    fun findByAccountNumber(accountNumber: String?): Account?
}