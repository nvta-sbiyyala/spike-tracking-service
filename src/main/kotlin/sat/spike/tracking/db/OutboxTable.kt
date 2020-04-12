package sat.spike.tracking.db

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.UUID
import org.jetbrains.exposed.sql.Table
import sat.spike.tracking.utils.exposed.columnTypes.jsonb

object OutboxTable : Table("outbox") {
    val uuid = uuid("uuid")
    val eventType = text("event_type")
    val payload = jsonb("payload", ParcelRecord::class.java, jacksonObjectMapper(), false)
}

data class OutboxRecord(val uuid: UUID, val eventType: String, val payload: ParcelRecord)
