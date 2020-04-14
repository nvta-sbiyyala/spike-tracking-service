package sat.spike.tracking.db

import org.jetbrains.exposed.sql.Table
import sat.spike.tracking.utils.JacksonParser
import sat.spike.tracking.utils.exposed.columnTypes.jsonb
import java.util.UUID

object OutboxTable : Table("outbox") {
    val uuid = uuid("uuid")
    val eventType = text("event_type")
    val payload = jsonb<ParcelRecord>(
        name = "payload",
        jsonParser = JacksonParser(klass = ParcelRecord::class.java),
        nullable = false)
}

data class OutboxRecord(val uuid: UUID, val eventType: String, val payload: ParcelRecord)
