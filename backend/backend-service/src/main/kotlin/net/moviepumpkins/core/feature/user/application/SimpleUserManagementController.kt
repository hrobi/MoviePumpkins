package net.moviepumpkins.core.feature.user.application

import net.moviepumpkins.core.api.controllers.UsersController
import net.moviepumpkins.core.api.controllers.UsersRoleController
import net.moviepumpkins.core.api.models.UserRoleEnum
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class SimpleUserManagementController(
    private val simpleUserManagementService: SimpleUserManagementService,
) : UsersController, UsersRoleController {

    override fun createUser(): ResponseEntity<Unit> {
        val username = simpleUserManagementService.createUserAndGetUsername()
        return ResponseEntity.created(URI.create("/users/${username}")).build()
    }

    override fun getUserRole(username: String): ResponseEntity<UserRoleEnum> {
        return ResponseEntity.ok(simpleUserManagementService.getUserRoleByUsername(username))
    }

}