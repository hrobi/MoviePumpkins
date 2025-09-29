package net.moviepumpkins.core.integration.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import net.moviepumpkins.core.app.model.UserAccount
import net.moviepumpkins.core.app.model.UserRole
import net.moviepumpkins.core.integration.models.UpdateUserProfileRequest
import net.moviepumpkins.core.oauth.AuthorizationService
import net.moviepumpkins.core.user.entity.UserAccountEntity
import net.moviepumpkins.core.user.repository.UserAccountRepository
import net.moviepumpkins.core.util.jwtAuthenticationByUserAccount
import org.flywaydb.test.annotation.FlywayTest
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import kotlin.test.assertEquals

@SpringBootTest
@FlywayTest
@AutoConfigureEmbeddedDatabase
class UsersProfileControllerTest {
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userAccountRepository: UserAccountRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var authorizationService: AuthorizationService

    companion object {
        val validUsersString = """
            paul-atraedes,Paul Atreides,Muaddib,lisan-algaib@sietch.ar,admin
            stillgar,Stillgar Of Fremen,Stillgar,stillgar@sietch.ar,supervisor
            jamis,Jamis Of Fremen,Jamis,jamis@sietch.ar,reviewer
        """.trimIndent()

        val validUserAccounts = validUsersString
            .lines()
            .map {
                val (username, name, displayName, email, roleLowerCase) = it.split(",")
                UserAccount(
                    fullName = name,
                    email = email,
                    username = username,
                    displayName = displayName,
                    role = UserRole.valueOf(roleLowerCase.uppercase()),
                    isAppUser = true
                )
            }

        fun paulAtraedes() = jwtAuthenticationByUserAccount(validUserAccounts[0])
        fun stillgar() = jwtAuthenticationByUserAccount(validUserAccounts[1])
    }

    @BeforeEach
    fun setup(wac: WebApplicationContext) {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(wac)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()

        userAccountRepository.saveAll(validUserAccounts.map {
            UserAccountEntity(
                username = it.username,
                fullName = it.fullName,
                email = it.email,
                displayName = it.displayName,
                role = it.role
            )
        })

        every { authorizationService.updateUserByUsername(any(), any()) } just Runs
    }

    @Test
    fun `expect all valid users to be returned in http json response`() {
        for (validUserString in validUsersString.lines()) {
            val (username, name, displayName, email, roleLowerCase) = validUserString.split(",")
            mockMvc.get("/users/${username}/profile").andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$.fullName") { value(name) }
                    jsonPath("$.email") { value(email) }
                    jsonPath("$.username") { value(username) }
                    jsonPath("$.displayName") { value(displayName) }
                    jsonPath("$.role") { value(roleLowerCase) }
                }
            }
        }
    }

    @Test
    fun `expect to get back 404 error when trying to find a non-existent user`() {
        mockMvc.get("/users/does.not.exist/profile").andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `expect to modify profile correctly`() {

        mockMvc.put("/users/paul-atraedes/profile") {
            with(paulAtraedes())
            content = objectMapper.writeValueAsString(
                UpdateUserProfileRequest(
                    displayName = "Lisan Al-Gaib",
                    fullName = "Paul Atreides",
                    email = "lisan-algaib@sietch.ar"
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNoContent() }
        }

        val paulFromDb = userAccountRepository.viewFirstByUsername("paul-atraedes")!!

        assertAll(
            { assertEquals(expected = "Lisan Al-Gaib", actual = paulFromDb.displayName) },
            { assertEquals(expected = "Paul Atreides", actual = paulFromDb.fullName) },
            { assertEquals(expected = "lisan-algaib@sietch.ar", actual = paulFromDb.email) },
        )

        mockMvc.put("/users/stillgar/profile") {
            with(stillgar())
            content = objectMapper.writeValueAsString(
                UpdateUserProfileRequest(
                    displayName = "The Naib",
                    fullName = "Stillgar Tabr",
                    email = "still@sietch.ar"
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNoContent() }
        }

        val stillgarFromDb = userAccountRepository.viewFirstByUsername("stillgar")!!

        assertAll(
            { assertEquals(expected = "The Naib", actual = stillgarFromDb.displayName) },
            { assertEquals(expected = "Stillgar Tabr", actual = stillgarFromDb.fullName) },
            { assertEquals(expected = "still@sietch.ar", actual = stillgarFromDb.email) },
        )

    }

    @Test
    fun `expect to fail modifying another users' profile`() {

        mockMvc.put("/users/stillgar/profile") {
            with(paulAtraedes())
            content = objectMapper.writeValueAsString(
                UpdateUserProfileRequest(
                    displayName = "The Naib",
                    fullName = "Stillgar Tabr",
                    email = "still@sietch.ar"
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    fun `expect to fail modifying user's profile with invalid request body`() {
        mockMvc.put("/users/paul-atraedes/profile") {
            with(paulAtraedes())
            content = objectMapper.writeValueAsString(
                UpdateUserProfileRequest(
                    displayName = "  Lisan Al-Gaib",
                    fullName = "Paul  Atreides ",
                    email = "lisan-algaib"
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath(
                "$.violations[*].fields[*]",
                containsInAnyOrder(
                    "displayName",
                    "fullName",
                    "email"
                )
            )
        }
    }
}