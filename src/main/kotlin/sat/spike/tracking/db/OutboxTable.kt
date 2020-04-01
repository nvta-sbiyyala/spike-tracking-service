package sat.spike.tracking.db

import org.jetbrains.exposed.sql.Table
import java.util.UUID

object OutboxTable : Table("outbox") {
    val uuid = uuid("uuid")
    val eventType = text("event_type")
    val payload = text("payload") // TODO: Blob?
}

data class OutboxRecord(val uuid: UUID, val eventType: String, val payload: String)