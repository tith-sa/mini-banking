package spring.minibanksystem.service

import org.springframework.data.domain.Page
import spring.minibanksystem.dto.ResponseDto
import spring.minibanksystem.dto.request.TransactionRequest
import spring.minibanksystem.dto.response.TransactionResponse

interface TransactionService {
    fun transfer(request: TransactionRequest) : ResponseDto<TransactionResponse>
    fun historyTransaction(accountNumber: String,page: Int, size: Int) : ResponseDto<Page<TransactionResponse>>
}