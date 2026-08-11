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
import spring.minibanksystem.model.enum.TransactionStatus
import spring.minibanksystem.model.enum.TransactionType
import java.math.BigDecimal

@Entity
@Table(name = "Transactions")
class Transaction (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column("fromAccount", length = 20)
    var fromAccount: String? = null,

    @Column("toAccount", length = 20)
    var toAccount: String? = null,

    @Enumerated(EnumType.STRING)
    @Column("currency", length = 10)
    var currency: CurrencyType? = null,

    @Column("amount")
    var amount: BigDecimal? = null,

    @Enumerated(EnumType.STRING)
    @Column("type", length = 10)
    var type: TransactionType? = null,

    @Enumerated(EnumType.STRING)
    @Column("Status", length = 20)
    var status: TransactionStatus? = null,

): BaseModel()