package spring.minibanksystem.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.minibanksystem.model.Account
import spring.minibanksystem.model.User

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
    fun existsByAccountNumber(accountNumber: String): Boolean
    fun findByOwner(owner: User): List<Account>
    fun findByAccountNumber(accountNumber: String): Account?
}