package spring.minibanksystem.controller

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
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

    @PostMapping("/transfer")
    fun transfer(
        @Valid
        @RequestBody request: TransactionRequest,
        @RequestAttribute userId: Long
    ) : ResponseEntity<ResponseDto<TransactionResponse>> {
        val result = transactionService.transfer(userId,request)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/history/{accountNumber}")
    fun historyTransaction(
        @PathVariable accountNumber: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestAttribute userId: Long
    ) : ResponseEntity<ResponseDto<Page<TransactionResponse>>> {
        val result = transactionService.historyTransaction(userId,accountNumber, page, size)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    fun gatTransaction(
        @PathVariable id: Long,
        @RequestAttribute userId: Long
    ) : ResponseEntity<ResponseDto<TransactionResponse>> {
        val result = transactionService.getTransaction(userId,id)
        return ResponseEntity.ok(result)
    }

}