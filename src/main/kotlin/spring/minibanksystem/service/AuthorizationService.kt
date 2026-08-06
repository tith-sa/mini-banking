package spring.minibanksystem.service

import org.springframework.stereotype.Service
import spring.minibanksystem.handleException.HandleException
import spring.minibanksystem.model.Account
import spring.minibanksystem.model.Transaction

@Service
class AuthorizationService {

    fun validateOwner(
        account: Account,
        userId: Long
    ) {
        if (account.owner.id != userId) {
            throw HandleException.Authorization(
                "Access is denied"
            )
        }
    }
}