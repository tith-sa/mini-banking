package spring.minibanksystem.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class TransactionRequest (
    @field:NotBlank (message = "from account cannot be blank")
    val fromAccount: String,

    @field:NotBlank (message = "to account cannot be blank")
    val toAccount: String,

    @field:NotNull(message = "Amount is required")
    val amount: BigDecimal,

    )