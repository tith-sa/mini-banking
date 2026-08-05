package spring.minibanksystem.util

import spring.minibanksystem.model.enum.CurrencyType
import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.exchangeAmount(
    from: CurrencyType,
    to: CurrencyType,
    exchangeRate: BigDecimal = BigDecimal("4000"),
): BigDecimal {
    if (from == to) return this
    return when (to) {
        CurrencyType.USD -> this.divide(exchangeRate,2, RoundingMode.HALF_EVEN)
        CurrencyType.KHR -> this.multiply(exchangeRate).setScale(2, RoundingMode.HALF_EVEN)
    }
}