package net.moviepumpkins.core.integration.apispec

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.validation.Valid
import net.moviepumpkins.core.integration.controllers.SpecController
import net.moviepumpkins.core.integration.models.Specification
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class SpecController(
    private val objectMapper: ObjectMapper,
) : SpecController {

    @Valid
    override fun get(): ResponseEntity<Specification> {
        val specification =
            objectMapper.readValue<Specification>(javaClass.classLoader.getResource("config/spec.response.json"))
        return ResponseEntity.ok(specification)
    }

}