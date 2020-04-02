package sat.spike.tracking.services

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sat.spike.tracking.controllers.NewParcelRequest
import sat.spike.tracking.db.ParcelRecord
import sat.spike.tracking.db.ParcelRepo
import sat.spike.tracking.events.OutboxEvent
import java.util.UUID

@Service
class ParcelService(
    private val parcelRepo: ParcelRepo,
    private val eventService: EventService,
    private val objectMapper: ObjectMapper
) {

    @Transactional
    fun createParcel(request: NewParcelRequest): UUID {
        val parcelRecord = toParcelRecord(UUID.randomUUID(), request.contents)
        val uuid = request
            .let { parcelRepo.create(parcelRecord) }

        val outboxEvent = createOutboxEvent(parcelRecord, objectMapper)
        eventService.handleOutboxEvent(outboxEvent)

        return uuid
    }
}

fun createOutboxEvent(record: ParcelRecord, mapper: ObjectMapper): OutboxEvent {
    val payload = mapper.convertValue(record, JsonNode::class.java)
    val eventType = "PARCEL_CREATED" // Magic string! <refactor>
    return OutboxEvent(eventType, payload)
}

fun toParcelRecord(uuid: UUID, contents: String): ParcelRecord =
    ParcelRecord(id = uuid, contents = contents)