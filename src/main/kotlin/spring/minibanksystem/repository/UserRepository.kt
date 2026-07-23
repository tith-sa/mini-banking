package spring.minibanksystem.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.minibanksystem.model.User

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun existsByEmail(email : String): Boolean
    fun existsByUsername(username : String): Boolean
}