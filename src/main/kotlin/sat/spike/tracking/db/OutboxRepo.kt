package sat.spike.tracking.db

import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.springframework.stereotype.Repository
import sat.spike.tracking.utils.execAndMap
import java.util.UUID

@Repository
class OutboxRepo(private val objectMapper: ObjectMapper) {
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

    fun fetchParcelHistory(parcelId: UUID): List<ParcelRecord> =
        "select payload from outbox where payload@>'{\"id\": \"$parcelId\"}';".execAndMap {
            objectMapper.readValue(it.getString(1), ParcelRecord::class.java)
        }
}
