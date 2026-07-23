package spring.minibanksystem.controller

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import spring.minibanksystem.dto.ResponseDto
import spring.minibanksystem.dto.request.TransactionRequest
import spring.minibanksystem.dto.response.TransactionResponse
import spring.minibanksystem.service.TransactionService

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
   private val transactionService: TransactionService,
) {

    @PostMapping("/deposit")
    fun deposit(@Valid @RequestBody request : TransactionRequest): ResponseEntity<ResponseDto<TransactionResponse>> {
        val result = transactionService.deposit(request)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/withdraw")
    fun withdraw(@Valid @RequestBody request: TransactionRequest): ResponseEntity<ResponseDto<TransactionResponse>> {
        val result = transactionService.withdraw(request)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/transfer")
    fun transfer(@Valid @RequestBody request: TransactionRequest) : ResponseEntity<ResponseDto<TransactionResponse>> {
        val result = transactionService.transfer(request)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/history/{accountNumber}")
    fun historyTransaction(
        @PathVariable accountNumber: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ) : ResponseEntity<ResponseDto<Page<TransactionResponse>>> {
        val result = transactionService.historyTransaction(accountNumber, page, size)
        return ResponseEntity.ok(result)
    }
}