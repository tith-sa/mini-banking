package spring.minibanksystem.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import spring.minibanksystem.model.enum.CurrencyType
import spring.minibanksystem.model.enum.TransactionType
import java.math.BigDecimal

@Entity
@Table(name = "transactions")
data class Transaction (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn("from_account", nullable = false)
    var fromAccount: Account,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn("to_account", nullable = false)
    var toAccount: Account,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var currency: CurrencyType,

    @Column(nullable = false)
    var amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column("transaction_type", nullable = false)
    var type : TransactionType

): BaseModel()