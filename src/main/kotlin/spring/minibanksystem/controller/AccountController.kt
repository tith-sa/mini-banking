package spring.minibanksystem.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.minibanksystem.dto.ResponseSuccess
import spring.minibanksystem.dto.response.AccountResponse
import spring.minibanksystem.service.interfaceService.AccountService

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val accountService: AccountService
) {

    @GetMapping
    fun getAccountByUser(
        @RequestAttribute userId: Long
    ): ResponseEntity<ResponseSuccess<List<AccountResponse>>> {
        val result = accountService.getAccountByOwner(userId)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    fun getAccountById(
        @PathVariable id: Long,
        @RequestAttribute userId: Long
    ): ResponseEntity<ResponseSuccess<AccountResponse>> {
        val result = accountService.getAccountById(id, userId)
        return ResponseEntity.ok(result)
    }
}