package net.moviepumpkins.core.config

import liquibase.change.DatabaseChange
import liquibase.integration.spring.SpringLiquibase
import net.moviepumpkins.core.general.getLogger
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Configuration
@ConditionalOnClass(SpringLiquibase::class, DatabaseChange::class)
@ConditionalOnProperty(prefix = "spring.liquibase", name = ["enabled"], matchIfMissing = true)
@AutoConfigureAfter(DataSourceAutoConfiguration::class, HibernateJpaAutoConfiguration::class)
class SchemaInit {

    companion object {
        val LOG = getLogger()
    }

    @Component
    @ConditionalOnProperty(prefix = "spring.liquibase", name = ["enabled"], matchIfMissing = true)
    class SchemaInitBean(
        private val dataSource: DataSource,
        @Value("\${db.schema}") private val schemaName: String,
    ) : InitializingBean {
        override fun afterPropertiesSet() {
            dataSource.connection.use { conn ->
                conn
                    .createStatement()
                    .also {
                        LOG.info("Going to create DB schema '{}' if not exists", schemaName)
                    }
                    .execute("create schema if not exists $schemaName")
            }
        }
    }

    @ConditionalOnBean(SchemaInitBean::class)
    @Component
    class SpringLiquibaseDependsOnPostProcessor : AbstractDependsOnBeanFactoryPostProcessor(
        SpringLiquibase::class.java,
        SchemaInitBean::class.java,
    )
}