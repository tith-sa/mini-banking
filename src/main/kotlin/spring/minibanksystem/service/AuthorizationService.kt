package spring.minibanksystem.service

import org.springframework.stereotype.Service
import spring.minibanksystem.handleException.HandleException
import spring.minibanksystem.model.Account

@Service
class AuthorizationService {

    fun validateOwner(
        account: Account,
        userId: Long?
    ) {
        if (account.ownerId != userId) {
            throw HandleException.Authorization(
                "Access is denied"
            )
        }
    }
}