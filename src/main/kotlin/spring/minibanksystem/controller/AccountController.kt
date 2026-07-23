package spring.minibanksystem.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.minibanksystem.dto.ResponseDto
import spring.minibanksystem.dto.response.AccountResponse
import spring.minibanksystem.service.AccountService

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val accountService: AccountService
) {

    @GetMapping("/{userId}")
    fun getAccountByUser(@PathVariable userId: Long): ResponseEntity<ResponseDto<List<AccountResponse>>> {
        val result = accountService.getAccountByOwner(userId)
        return ResponseEntity.ok(result)
    }
}