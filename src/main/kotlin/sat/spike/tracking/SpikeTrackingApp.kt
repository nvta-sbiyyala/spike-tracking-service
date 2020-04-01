package sat.spike.tracking

import org.jetbrains.exposed.sql.Database
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import sat.spike.tracking.db.DatabaseMigrator
import javax.sql.DataSource

@SpringBootApplication
class SpikingTrackingApp(dataSource: DataSource, migrator: DatabaseMigrator) {
    init {
        Database.connect(dataSource)
        migrator.migrate()
    }
}

fun main(args: Array<String>) {
    runApplication<SpikingTrackingApp>(*args)
}
