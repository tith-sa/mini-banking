package spring.minibanksystem.dto.response

import spring.minibanksystem.model.enum.CurrencyType
import java.math.BigDecimal

data class AccountResponse(
    val id : Long,
    val accountNumber: String,
    val currency: CurrencyType,
    val balance: BigDecimal,
)
