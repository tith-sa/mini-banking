package spring.minibanksystem.dto.request

import spring.minibanksystem.model.enum.TransactionStatus
import spring.minibanksystem.model.enum.TransactionType
import java.time.LocalDateTime

data class TransactionSearchRequest(
    val accountNumber: String? = null,
    val statuses: List<TransactionStatus>? = null,
    val types: List<TransactionType>? = null,
    val fromDate: LocalDateTime? = null,
    val toDate: LocalDateTime? = null
)