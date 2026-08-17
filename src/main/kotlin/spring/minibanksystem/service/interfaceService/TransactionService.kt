package spring.minibanksystem.service.interfaceService

import spring.minibanksystem.dto.ResponsePagination
import spring.minibanksystem.dto.ResponseSuccess
import spring.minibanksystem.dto.request.TransactionRequest
import spring.minibanksystem.dto.request.TransactionSearchRequest
import spring.minibanksystem.dto.response.TransactionResponse

interface TransactionService {
    fun transfer(userId: Long,request: TransactionRequest) : ResponseSuccess<TransactionResponse>
    fun historyTransaction(userId: Long,accountNumber: String,page: Int, size: Int) : ResponseSuccess<ResponsePagination<TransactionResponse>>
    fun getTransaction(userId: Long,accountNumber: String,id: Long) : ResponseSuccess<TransactionResponse>
    fun searchTransactionHistory(userId: Long,request: TransactionSearchRequest,page: Int, size: Int) : ResponseSuccess<ResponsePagination<TransactionResponse>>
}