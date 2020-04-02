package sat.spike.tracking

import org.jetbrains.exposed.spring.SpringTransactionManager
import org.jetbrains.exposed.sql.Database
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.transaction.annotation.EnableTransactionManagement
import sat.spike.tracking.db.DatabaseMigrator
import javax.sql.DataSource

@SpringBootApplication
@EnableTransactionManagement
class SpikingTrackingApp(dataSource: DataSource, migrator: DatabaseMigrator) {
    init {
        Database.connect(dataSource)
        migrator.migrate()
    }

    @Bean
    fun transactionManager(dataSource: DataSource): SpringTransactionManager {
        return SpringTransactionManager(dataSource)
    }
}

fun main(args: Array<String>) {
    runApplication<SpikingTrackingApp>(*args)
}
