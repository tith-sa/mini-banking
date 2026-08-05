package spring.minibanksystem.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import spring.minibanksystem.model.enum.CurrencyType
import java.math.BigDecimal

@Entity
@Table(name = "accounts")
data class Account(
    @Column( "account_id", nullable = false, unique = true)
    var accountNumber: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var currency: CurrencyType,

    @Column(nullable = false)
    var balance: BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( "owner_id", nullable = false)
    var owner: User,

    @OneToMany(mappedBy = "fromAccount", fetch = FetchType.LAZY)
    var outGoingTransaction: MutableList<Transaction> = mutableListOf(),

    @OneToMany(mappedBy = "toAccount", fetch = FetchType.LAZY)
    var inComingTransaction: MutableList<Transaction> = mutableListOf(),

    ) : BaseModel()

// get user but don't want account