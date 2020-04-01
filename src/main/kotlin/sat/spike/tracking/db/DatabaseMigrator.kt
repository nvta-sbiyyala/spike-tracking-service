package sat.spike.tracking.db

import org.flywaydb.core.Flyway
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.stereotype.Component

@Component
class DatabaseMigrator(private val flyway: Flyway) : FlywayMigrationStrategy {
    override fun migrate(flyway: Flyway?) = Unit

    fun migrate() {
        flyway.migrate()
    }
}