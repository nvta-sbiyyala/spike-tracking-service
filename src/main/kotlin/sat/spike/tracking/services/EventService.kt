package sat.spike.tracking.services

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.UUID
import org.springframework.stereotype.Service
import sat.spike.tracking.db.OutboxRecord
import sat.spike.tracking.db.OutboxRepo
import sat.spike.tracking.db.ParcelRecord
import sat.spike.tracking.events.OutboxEvent

@Service
class EventService(
    private val outboxRepo: OutboxRepo,
    private val objectMapper: ObjectMapper
) {

    fun handleOutboxEvent(outboxEvent: OutboxEvent) {
        val uuid = UUID.randomUUID()
        val outboxRecord = OutboxRecord(
            uuid,
            outboxEvent.eventType,
            objectMapper.convertValue(outboxEvent.payload, ParcelRecord::class.java))

        outboxRepo.save(outboxRecord = outboxRecord)

        // once record is written in outbox table, logs are generated and dbz (CDC eventing) picks it up
        // so Delete the event once written - so outbox doesn't grow
        // Commenting out delete, to test 'jsonb'
        // outboxRepo.delete(outboxRecord = outboxRecord)
    }
}
