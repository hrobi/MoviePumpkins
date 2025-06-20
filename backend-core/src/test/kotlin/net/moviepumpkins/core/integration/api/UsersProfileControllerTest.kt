package net.moviepumpkins.core.integration.api

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import net.moviepumpkins.core.user.db.UserAccountEntity
import net.moviepumpkins.core.user.db.UserAccountRepository
import net.moviepumpkins.core.user.model.UserRole
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@FlywayTest
@AutoConfigureEmbeddedDatabase
class UsersProfileControllerTest {
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userAccountRepository: UserAccountRepository

    companion object {
        val validUsersString = """
            paul-atraedes,Paul Atreides,Muaddib,lisan-algaib@sietch.ar,admin
            stillgar,Stillgar Of Fremen,Stillgar,stillgar@sietch.ar,supervisor
            jamis,Jamis Of Fremen,Jamis,jamis@sietch.ar,reviewer
        """.trimIndent()

        val validUserAccountEntities = validUsersString
            .lines()
            .map {
                val (username, name, displayName, email, roleLowerCase) = it.split(",")
                UserAccountEntity(
                    fullName = name,
                    email = email,
                    username = username,
                    displayName = displayName,
                    role = UserRole.valueOf(roleLowerCase.uppercase())
                )
            }
    }

    @BeforeEach
    fun setup(wac: WebApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
    }

    @Test
    fun `expect all valid users to be returned in http json response`() {
        userAccountRepository.saveAll(validUserAccountEntities)
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
                    jsonPath("$.about") { value("") }
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
}