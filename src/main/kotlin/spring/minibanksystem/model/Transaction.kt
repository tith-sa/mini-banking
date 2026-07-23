package spring.minibanksystem.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import spring.minibanksystem.model.enum.CurrencyType
import spring.minibanksystem.model.enum.TransactionType
import java.math.BigDecimal

@Entity
@Table(name = "transactions")
data class Transaction (
    @Column("from_account")
    var fromAccount: String?,

    @Column("to_account")
    var toAccount: String?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var currency: CurrencyType,

    @Column(nullable = false)
    var amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column("transaction_type", nullable = false)
    var type : TransactionType

): BaseModel()