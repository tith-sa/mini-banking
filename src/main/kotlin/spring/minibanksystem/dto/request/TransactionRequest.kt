package spring.minibanksystem.dto.request

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import java.math.BigDecimal

data class TransactionRequest (
    val fromAccount: String? = null,
    val toAccount: String? = null,

    @DecimalMax("10000")
    @DecimalMin("0.01")
    val amount: BigDecimal,

)