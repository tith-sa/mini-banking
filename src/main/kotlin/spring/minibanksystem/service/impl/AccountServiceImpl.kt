package spring.minibanksystem.service.impl

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import spring.minibanksystem.dto.ResponseDto
import spring.minibanksystem.dto.request.AccountRequest
import spring.minibanksystem.dto.response.AccountResponse
import spring.minibanksystem.model.Account
import spring.minibanksystem.repository.AccountRepository
import spring.minibanksystem.repository.UserRepository
import spring.minibanksystem.service.AccountService
import spring.minibanksystem.util.generatedAccountNumber
import spring.minibanksystem.util.toSuccess

@Service
class AccountServiceImpl(
    private val accountRepo: AccountRepository,
    private val userRepo: UserRepository,
) : AccountService {

    override fun createAccount(userId: Long,request: AccountRequest): ResponseDto<AccountResponse> {
        val (currency, balance) = request
        val owner = userRepo.findById(userId)
            .orElseThrow {
                IllegalArgumentException("User Not Found")
            }
        val accountNumber = generatedAccountNumber()
        print(accountNumber)
        if (accountRepo.existsByAccountNumber(accountNumber)) {
            throw IllegalArgumentException("Account already exists")
        }

        val account = Account(
            accountNumber = accountNumber,
            currency = currency,
            balance = balance,
            owner = owner
        )
        accountRepo.save(account)

        return AccountResponse(
            account.id,
            account.accountNumber,
            account.currency,
            account.balance
        ).toSuccess(
            HttpStatus.CREATED,
            "Account ${account.currency} created"
        )
    }

    override fun getAccountByOwner(ownerId: Long): ResponseDto<List<AccountResponse>>{
        val owner = userRepo.findById(ownerId)
            .orElseThrow{
                IllegalArgumentException("User Not Found")
            }
        val accounts = accountRepo.findByOwner(owner)
        val response = accounts.map {
            AccountResponse(
                it.id,
                it.accountNumber,
                it.currency,
                it.balance
            )
        }
        return response.toSuccess(message = "Getting accounts By Owner")
    }
}