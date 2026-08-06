package spring.minibanksystem.service.impl
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import spring.minibanksystem.config.JwtUtil
import spring.minibanksystem.dto.ResponseDto
import spring.minibanksystem.dto.request.AccountRequest
import spring.minibanksystem.dto.request.LoginRequest
import spring.minibanksystem.dto.request.RegisterRequest
import spring.minibanksystem.dto.response.LoginResponse
import spring.minibanksystem.dto.response.RegisterResponse
import spring.minibanksystem.handleException.HandleException
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
    private val accountService: AccountService,
    private val jwtUtil: JwtUtil
) : AuthService {

    @Transactional
    override fun register(request : RegisterRequest) : ResponseDto<RegisterResponse>{
        val (username, email,password) = request
        if (userRepo.existsByUsername(username)) {
            throw HandleException.BadRequest("Username is already registered")
        }
        if (userRepo.existsByEmail(email)) {
            throw HandleException.BadRequest("Email is already registered")
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

        return RegisterResponse(
            user.id,
            user.username,
            user.email,
        ).toSuccess(
            HttpStatus.CREATED,
            "User registered successfully."
        )
    }

    override fun login(request: LoginRequest): ResponseDto<LoginResponse> {
        val ( email, password) = request

        val user = userRepo.findByEmail(email)
            ?: throw HandleException.BadRequest("Email is not registered")
        if (!passwordEncoder.matches(password, user.password)){
            throw HandleException.BadRequest("Password is incorrect")
        }

        val token = jwtUtil.generateToken(user.id, user.email)

        return LoginResponse(
            token
        ).toSuccess(
            message = "Login successful",
        )
    }
}
