package net.moviepumpkins.core.user

import jakarta.transaction.Transactional
import net.moviepumpkins.core.user.db.UserAccountRepository
import net.moviepumpkins.core.user.model.UserProfile
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userAccountRepository: UserAccountRepository,
) {

    @Transactional
    fun saveUser(userProfile: UserProfile) {
        userAccountRepository.save(userProfile.toUserAccountEntity())
    }

    @Transactional
    fun getUserProfileByUsername(username: String): UserProfile? {
        return userAccountRepository.findFirstByUsername(username)?.toUserProfile()
    }
}