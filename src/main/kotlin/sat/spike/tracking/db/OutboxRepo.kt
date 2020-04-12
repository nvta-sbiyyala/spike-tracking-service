package sat.spike.tracking.db

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.springframework.stereotype.Repository

@Repository
class OutboxRepo {
    fun save(outboxRecord: OutboxRecord) {
        OutboxTable.insert {
            it[OutboxTable.uuid] = outboxRecord.uuid
            it[OutboxTable.payload] = outboxRecord.payload
            it[OutboxTable.eventType] = outboxRecord.eventType
        }
    }

    fun delete(outboxRecord: OutboxRecord) {
        OutboxTable.deleteWhere { OutboxTable.uuid eq outboxRecord.uuid }
    }
}
