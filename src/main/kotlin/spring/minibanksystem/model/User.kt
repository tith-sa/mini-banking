package spring.minibanksystem.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(

    @Column(unique = true,nullable = false)
    var username: String,

    @Column(unique = true,nullable = false)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    var accounts: MutableList<Account> = mutableListOf(),

) : BaseModel()
