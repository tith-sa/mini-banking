package spring.minibanksystem.service

import org.springframework.data.domain.Page
import spring.minibanksystem.dto.ResponseDto
import spring.minibanksystem.dto.request.TransactionRequest
import spring.minibanksystem.dto.response.TransactionResponse

interface TransactionService {
    fun transfer(userId: Long,request: TransactionRequest) : ResponseDto<TransactionResponse>
    fun historyTransaction(userId: Long,accountNumber: String,page: Int, size: Int) : ResponseDto<Page<TransactionResponse>>
    fun getTransaction(userId: Long,accountNumber: String,id: Long) : ResponseDto<TransactionResponse>
}