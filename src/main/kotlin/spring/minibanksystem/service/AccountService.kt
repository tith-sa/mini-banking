package spring.minibanksystem.service

import spring.minibanksystem.dto.ResponseDto
import spring.minibanksystem.dto.request.AccountRequest
import spring.minibanksystem.dto.response.AccountResponse

interface AccountService {
    fun createAccount(userId: Long,request : AccountRequest): ResponseDto<AccountResponse>
    fun getAccountByOwner(ownerId: Long) : ResponseDto<List<AccountResponse>>
}