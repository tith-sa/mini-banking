package spring.minibanksystem.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import spring.minibanksystem.model.enum.CurrencyType
import java.math.BigDecimal

data class AccountRequest(
    @field:NotBlank
    val currency: CurrencyType,

    @Positive
    val balance: BigDecimal = BigDecimal.ZERO.setScale(2),
)
