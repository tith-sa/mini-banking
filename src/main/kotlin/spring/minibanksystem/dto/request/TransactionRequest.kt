package spring.minibanksystem.dto.request

import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class TransactionRequest (
    @field:NotBlank
    val fromAccount: String,

    @field:NotBlank
    val toAccount: String,

    val amount: BigDecimal,

    )