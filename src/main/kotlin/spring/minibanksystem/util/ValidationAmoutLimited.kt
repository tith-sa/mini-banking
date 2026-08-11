package spring.minibanksystem.util

import spring.minibanksystem.handleException.HandleException
import spring.minibanksystem.model.enum.CurrencyType
import java.math.BigDecimal

fun BigDecimal.validationAmountLimited(fromCurrency: CurrencyType?){
    if (fromCurrency == null) {
        throw HandleException.BadRequest("Currency is required.")
    }
    val maxAmount = when (fromCurrency) {
        CurrencyType.USD -> BigDecimal("10000")
        CurrencyType.KHR -> BigDecimal("40000000")
    }

    val minAmount = when (fromCurrency) {
        CurrencyType.USD -> BigDecimal("0.05")
        CurrencyType.KHR -> BigDecimal("200")
    }

    if (this >= maxAmount) {
        throw HandleException.BadRequest("Transfer amount exceeds the maximum limit.")
    }
    if (this <= minAmount) {
        throw HandleException.BadRequest("Transfer amount exceeds the minimum limit.")
    }
}