package net.moviepumpkins.core

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@FlywayTest
@AutoConfigureEmbeddedDatabase
class BackendCoreApplicationTests {

    @Test
    fun contextLoads() {
    }

}
