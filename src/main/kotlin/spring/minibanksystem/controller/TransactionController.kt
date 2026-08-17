package spring.minibanksystem.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import spring.minibanksystem.dto.ResponsePagination
import spring.minibanksystem.dto.ResponseSuccess
import spring.minibanksystem.dto.request.TransactionRequest
import spring.minibanksystem.dto.request.TransactionSearchRequest
import spring.minibanksystem.dto.response.TransactionResponse
import spring.minibanksystem.service.interfaceService.TransactionService

@RestController
@RequestMapping("/api/transactions")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Transaction API")
class TransactionController(
   private val transactionService: TransactionService,
) {

    @PostMapping("/transfer")
    @Operation(
        summary = "Initiate fund transfer",
        description = "Executes a money transfer from the authenticated user's source account to a destination account."
    )
    fun transfer(
        @Valid
        @RequestBody request: TransactionRequest,
        @RequestAttribute userId: Long
    ) : ResponseEntity<ResponseSuccess<TransactionResponse>> {
        val result = transactionService.transfer(userId,request)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/history/{accountNumber}")
    @Operation(
        summary = "Get transaction history by account number",
        description = "Retrieves a paginated list of all incoming and outgoing transactions for a specific bank account."
    )
    fun historyTransaction(
        @PathVariable accountNumber: String,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestAttribute userId: Long
    ) : ResponseEntity<ResponseSuccess<ResponsePagination<TransactionResponse>>> {
        val result = transactionService.historyTransaction(userId,accountNumber, page, size)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/history/{accountNumber}/{id}")
    @Operation(
        summary = "Get transaction details by ID",
        description = "Fetches the full details of a specific transaction using its unique transaction ID and associated account number."
    )
    fun gatTransaction(
        @PathVariable id: Long,
        @PathVariable accountNumber: String,
        @RequestAttribute userId: Long
    ) : ResponseEntity<ResponseSuccess<TransactionResponse>> {
        val result = transactionService.getTransaction(userId,accountNumber, id)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/search")
    @Operation(
        summary = "Search transaction history with filters",
        description = "Searches and filters across all transaction histories using dynamic parameters like accountNumber, fromDate, toDate, status, or types."
    )
    fun searchTransactionHistory(
        @RequestAttribute("userId") userId: Long,
        @ModelAttribute request: TransactionSearchRequest,// include all @RequestParam in request DTO
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResponseEntity<ResponseSuccess<ResponsePagination<TransactionResponse>>> {
        val result = transactionService.searchTransactionHistory(userId,request,page, size)
        return ResponseEntity.ok(result)
    }

}