package spring.minibanksystem.service.interfaceService

import spring.minibanksystem.dto.ResponseSuccess
import spring.minibanksystem.dto.request.AccountRequest
import spring.minibanksystem.dto.response.AccountResponse

interface AccountService {
    fun createAccount(userId: Long?,request : AccountRequest): ResponseSuccess<AccountResponse>
    fun getAccountByOwner(ownerId: Long?) : ResponseSuccess<List<AccountResponse>>
    fun getAccountById(id: Long?, ownerId: Long?) : ResponseSuccess<AccountResponse>
}