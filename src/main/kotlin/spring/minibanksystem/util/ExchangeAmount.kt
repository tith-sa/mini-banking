package spring.minibanksystem.util

import spring.minibanksystem.handleException.BadRequestException
import spring.minibanksystem.model.enum.CurrencyType
import spring.minibanksystem.model.enum.CurrencyType.*
import java.math.BigDecimal
import java.math.RoundingMode

// Converts a BigDecimal amount from one currency to another.
// The default exchange rate is 1 USD = 4,000 KHR.
fun BigDecimal.exchangeAmount(
    fromAccountCurrency: CurrencyType,
    toAccountCurrency: CurrencyType,
    exchangeRate: BigDecimal = BigDecimal("4000"),
): BigDecimal {

    // No conversion is needed when both currencies are the same.
    if (fromAccountCurrency == toAccountCurrency) return this

    // Convert the amount based on the target currency.
    return when (toAccountCurrency) {

        // Convert KHR to USD by dividing by the exchange rate.
        USD ->
            this.divide(exchangeRate, 2, RoundingMode.HALF_EVEN)

        // Convert USD to KHR by multiplying by the exchange rate.
        KHR ->
            this.multiply(exchangeRate).setScale(2, RoundingMode.HALF_EVEN)
    }
}