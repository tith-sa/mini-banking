package spring.minibanksystem.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "Users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column("username", unique = true)
    var username: String?,

    @Column("email", unique = true, length = 254)
    var email: String?,

    @Column("password", length = 60)
    var password: String?

) : BaseModel()