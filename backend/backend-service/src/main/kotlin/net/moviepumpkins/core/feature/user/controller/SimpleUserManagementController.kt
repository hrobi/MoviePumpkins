package net.moviepumpkins.core.feature.user.controller

import net.moviepumpkins.core.api.controllers.UsersController
import net.moviepumpkins.core.api.controllers.UsersRoleController
import net.moviepumpkins.core.api.models.UserRoleEnum
import net.moviepumpkins.core.feature.user.service.SimpleUserPersistenceService
import net.moviepumpkins.core.feature.user.util.throwUserNotFoundServiceException
import net.moviepumpkins.core.shared.security.facade.AuthenticationFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class SimpleUserManagementController(
    private val simpleUserPersistenceService: SimpleUserPersistenceService,
    private val authenticationFacade: AuthenticationFacade,
) : UsersController, UsersRoleController {

    override fun createUser(): ResponseEntity<Unit> {
        val userAccount = authenticationFacade.extractUserAccount()
        val userEntity = simpleUserPersistenceService.createUser(userAccount)
        return ResponseEntity.created(URI.create("/users/${userEntity.username}")).build()
    }

    override fun getUserRole(username: String): ResponseEntity<UserRoleEnum> {
        val role = simpleUserPersistenceService.getUserAccountEntity(username)?.role
            ?: throwUserNotFoundServiceException(username)
        return ResponseEntity.ok(
            UserRoleEnum.valueOf(role.name)
        )
    }

}