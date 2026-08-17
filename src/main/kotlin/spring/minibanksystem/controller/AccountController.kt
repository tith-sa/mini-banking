package spring.minibanksystem.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Account API")
class AccountController(
    private val accountService: AccountService
) {

    @GetMapping
    @Operation(
        summary = "Get accounts by Owner",
        description = "Fetches the list of bank account using its unique owner id."
    )
    fun getAccountByUser(
        @RequestAttribute userId: Long
    ): ResponseEntity<ResponseSuccess<List<AccountResponse>>> {
        val result = accountService.getAccountByOwner(userId)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get account details by account id",
        description = "Fetches the full details of a specific bank account using its unique account id."
    )
    fun getAccountById(
        @PathVariable id: Long,
        @RequestAttribute userId: Long
    ): ResponseEntity<ResponseSuccess<AccountResponse>> {
        val result = accountService.getAccountById(id, userId)
        return ResponseEntity.ok(result)
    }
}