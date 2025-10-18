package net.moviepumpkins.core.app.config

import org.hibernate.boot.model.naming.Identifier
import org.hibernate.boot.model.relational.QualifiedName
import org.hibernate.boot.model.relational.QualifiedSequenceName
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment
import org.hibernate.id.enhanced.ImplicitDatabaseObjectNamingStrategy
import org.hibernate.service.ServiceRegistry

class PostgresSequenceNamingStrategy : ImplicitDatabaseObjectNamingStrategy {
    override fun determineSequenceName(
        catalogName: Identifier?,
        schemaName: Identifier?,
        map: MutableMap<*, *>?,
        serviceRegistry: ServiceRegistry?,
    ): QualifiedName {
        val jdbcEnvironment = serviceRegistry?.getService(JdbcEnvironment::class.java)
        val seqName = (map?.get("target_table") as String) + "_id_seq"
        return QualifiedSequenceName(
            catalogName,
            schemaName,
            jdbcEnvironment?.identifierHelper?.toIdentifier(seqName)
        )
    }

    override fun determineTableName(
        p0: Identifier?,
        p1: Identifier?,
        p2: MutableMap<*, *>?,
        p3: ServiceRegistry?,
    ): QualifiedName {
        TODO("Not yet implemented")
    }
}