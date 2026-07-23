package spring.minibanksystem.service.impl
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import spring.minibanksystem.dto.ResponseDto
import spring.minibanksystem.dto.request.AccountRequest
import spring.minibanksystem.dto.request.AuthRequest
import spring.minibanksystem.dto.response.AuthResponse
import spring.minibanksystem.model.User
import spring.minibanksystem.model.enum.CurrencyType
import spring.minibanksystem.repository.UserRepository
import spring.minibanksystem.service.AccountService
import spring.minibanksystem.service.AuthService
import spring.minibanksystem.util.toSuccess

@Service
class AuthServiceImpl(
    private val userRepo: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val accountService: AccountService
) : AuthService {

    @Transactional
    override fun register(request : AuthRequest) : ResponseDto<AuthResponse>{
        val (username, email,password) = request
        if (userRepo.existsByUsername(username)) {
            throw IllegalArgumentException("Username is already registered")
        }
        if (userRepo.existsByEmail(email)) {
            throw IllegalArgumentException("Email is already registered")
        }
        val hashPassword = passwordEncoder.encode(password)
        val user = User(
            username = username,
            email = email,
            password = hashPassword
        )
        userRepo.save(user)
        print("Created new user $user")

        accountService.createAccount(user.id, AccountRequest(CurrencyType.KHR))
        accountService.createAccount(user.id, AccountRequest(CurrencyType.USD))

        return AuthResponse(
            user.id,
            user.username,
            user.email,
        ).toSuccess(
            HttpStatus.CREATED,
            "User registered successfully."
        )
    }
}