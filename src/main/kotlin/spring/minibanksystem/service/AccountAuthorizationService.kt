package spring.minibanksystem.service

import org.springframework.stereotype.Service
import spring.minibanksystem.model.Account

@Service
class AccountAuthorizationService {

    fun validateOwner(
        account: Account,
        userId: Long
    ) {
        if (account.owner.id != userId) {
            throw IllegalArgumentException(
                "You don't have permission"
            )
        }
    }


}