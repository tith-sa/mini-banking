package spring.minibanksystem.service.implementation
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import spring.minibanksystem.config.JwtUtil
import spring.minibanksystem.dto.ResponseSuccess
import spring.minibanksystem.dto.request.AccountRequest
import spring.minibanksystem.dto.request.LoginRequest
import spring.minibanksystem.dto.request.RegisterRequest
import spring.minibanksystem.dto.response.LoginResponse
import spring.minibanksystem.dto.response.RegisterResponse
import spring.minibanksystem.handleException.BadRequestException
import spring.minibanksystem.model.User
import spring.minibanksystem.model.enum.CurrencyType
import spring.minibanksystem.repository.UserRepository
import spring.minibanksystem.service.interfaceService.AccountService
import spring.minibanksystem.service.interfaceService.AuthService

@Service
class AuthServiceImpl(
    private val userRepo: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val accountService: AccountService,
    private val jwtUtil: JwtUtil
) : AuthService {

    @Transactional
    override fun register(request : RegisterRequest) : ResponseSuccess<RegisterResponse>{
        val (username, email,password) = request // object destructuring
        if (userRepo.existsByUsername(username)) {
            throw BadRequestException("Username is already registered")
        }
        if (userRepo.existsByEmail(email)) {
            throw BadRequestException("Email is already registered")
        }
        val hashPassword = passwordEncoder.encode(password)
        val user = User(
            username = username,
            email = email,
            password = hashPassword
        )
        userRepo.save(user)

        user.id?.let { accountService.createAccount( it, AccountRequest(CurrencyType.KHR)) }
        user.id?.let { accountService.createAccount(it, AccountRequest(CurrencyType.USD)) }

        val response = RegisterResponse(
            user.id,
            user.username,
            user.email,
        )
        return ResponseSuccess(
            status = HttpStatus.CREATED,
            data = response,
            message = "User registered"
        )
    }

    override fun login(request: LoginRequest): ResponseSuccess<LoginResponse> {
        val ( email, password) = request

        val user = userRepo.findByEmail(email)
            ?: throw BadRequestException("Email is not registered")
        if (!passwordEncoder.matches(password, user.password)){
            throw BadRequestException("Password is incorrect")
        }

        val token = jwtUtil.generateToken(user.id)

        val response = LoginResponse(
            token
        )
        return ResponseSuccess(
            data = response,
            message = "User logged in"
        )
    }
}
