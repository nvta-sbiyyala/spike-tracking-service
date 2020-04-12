package sat.spike.tracking.db

import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.springframework.stereotype.Repository
import java.sql.ResultSet
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

    fun fetchParcelHistory(parcelId: UUID): List<ParcelRecord> {
        val conn = TransactionManager.current().connection
        val statement = conn.prepareStatement(
            "select payload from outbox where payload@>'{\"id\": \"$parcelId\"}';",
            arrayOf("payload"))
        val resultSet: ResultSet = statement.executeQuery()
        return resultSet.use {
            generateSequence {
                if (resultSet.next())
                    objectMapper.readValue(resultSet.getString(1), ParcelRecord::class.java)
                else null
            }.toList()
        }
    }
}
