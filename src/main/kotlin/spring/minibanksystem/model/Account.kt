package spring.minibanksystem.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import spring.minibanksystem.model.enum.CurrencyType
import java.math.BigDecimal

@Entity
@Table(name = "Accounts")
class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column( "accountNumber", length = 20)
    var accountNumber: String? = null,

    @Enumerated(EnumType.STRING)
    @Column("currency",length = 10)
    var currency: CurrencyType? = null,

    @Column("balance")
    var balance: BigDecimal? = null,

    @Column( "ownerId")
    var ownerId: Long? = null

) : BaseModel()