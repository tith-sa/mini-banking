package spring.minibanksystem.service.implementation

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import spring.minibanksystem.dto.ResponseSuccess
import spring.minibanksystem.dto.request.AccountRequest
import spring.minibanksystem.dto.response.AccountResponse
import spring.minibanksystem.handleException.ResourceNotFoundException
import spring.minibanksystem.model.Account
import spring.minibanksystem.repository.AccountRepository
import spring.minibanksystem.repository.UserRepository
import spring.minibanksystem.service.interfaceService.AccountService
import spring.minibanksystem.service.AuthorizationService
import spring.minibanksystem.util.generatedAccountNumber

@Service
class AccountServiceImpl(
    private val accountRepo: AccountRepository,
    private val userRepo: UserRepository,
    private val authorizationService: AuthorizationService
) : AccountService {

    override fun createAccount(userId: Long,request: AccountRequest): ResponseSuccess<AccountResponse> {
        val (currency, balance) = request
        val owner = userRepo.findById(userId)
            .orElseThrow {
                ResourceNotFoundException("User Not Found")
            }
        var accountNumber = generatedAccountNumber()
        while (accountRepo.existsByAccountNumber(accountNumber)) {
            accountNumber = generatedAccountNumber()
        }

        val account = Account(
            accountNumber = accountNumber,
            currency = currency,
            balance = balance,
            ownerId = owner.id
        )
        accountRepo.save(account)

        val response = AccountResponse(
            account.id,
            account.accountNumber,
            account.currency,
            account.balance
        )
        return ResponseSuccess(
            status = HttpStatus.CREATED,
            data = response,
            message = "Account ${account.currency} created"
        )
    }

    override fun getAccountByOwner(ownerId: Long): ResponseSuccess<List<AccountResponse>>{
        val owner = userRepo.findById(ownerId)
            .orElseThrow{
                ResourceNotFoundException("User Not Found")
            }
        val accounts = accountRepo.findByOwnerId(owner.id)
        val response = accounts.map {
            AccountResponse(
                it.id,
                it.accountNumber,
                it.currency,
                it.balance
            )
        }
        return ResponseSuccess(
            data = response,
            message = "Getting accounts By Owner"
        )
    }

    override fun getAccountById(id: Long, ownerId: Long): ResponseSuccess<AccountResponse> {
        val account = accountRepo.findById(id)
            .orElseThrow{
                ResourceNotFoundException("Account Not Found")
            }
        authorizationService.validateOwner(account, ownerId)
        val response = AccountResponse(
            account.id,
            account.accountNumber,
            account.currency,
            account.balance,
        )
        return ResponseSuccess(
            data = response,
            message = "Getting account"
        )
    }

}