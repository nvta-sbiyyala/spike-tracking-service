package sat.spike.tracking.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener
import sat.spike.tracking.db.OutboxRecord
import sat.spike.tracking.db.OutboxRepo
import sat.spike.tracking.events.OutboxEvent
import java.util.UUID

@Service
class EventService(
    private val outboxRepo: OutboxRepo,
    private val objectMapper: ObjectMapper
) {

    @TransactionalEventListener
    fun handleOutboxEvent(outboxEvent: OutboxEvent) {
        val uuid = UUID.randomUUID()
        val outboxRecord = OutboxRecord(
            uuid,
            outboxEvent.eventType,
            objectMapper.writeValueAsString(outboxEvent.payload))

        outboxRepo.save(outboxRecord = outboxRecord)

        // once record is written in outbox table, logs are generated and dbz (CDC eventing) picks it up
        // so Delete the event once written - so outbox doesn't grow
        outboxRepo.delete(outboxRecord = outboxRecord)
    }
}