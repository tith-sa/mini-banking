package spring.minibanksystem.service

import org.springframework.stereotype.Service
import spring.minibanksystem.handleException.AuthorizationException
import spring.minibanksystem.model.Account

@Service
class AuthorizationService {

    fun validateOwner(
        account: Account,
        userId: Long
    ) {
        if (account.ownerId != userId) {
            throw AuthorizationException(
                "Access is denied"
            )
        }
    }
}