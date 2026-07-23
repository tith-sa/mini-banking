package spring.minibanksystem.dto.response

import spring.minibanksystem.model.enum.CurrencyType
import spring.minibanksystem.model.enum.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionResponse (
    val id: Long,
    val fromAccount: String?,
    val toAccount: String?,
    val currency: CurrencyType,
    val amount: BigDecimal,
    val type: TransactionType,
    val createdAt: LocalDateTime?,
)