package sat.spike.tracking.services

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sat.spike.tracking.controllers.ParcelRequest
import sat.spike.tracking.db.ParcelRecord
import sat.spike.tracking.db.ParcelRepo
import sat.spike.tracking.events.OutboxEvent

@Service
class ParcelService(
    private val parcelRepo: ParcelRepo,
    private val eventService: EventService,
    private val objectMapper: ObjectMapper
) {

    @Transactional
    fun createParcel(request: ParcelRequest): UUID {
        val parcelRecord = toParcelRecord(UUID.randomUUID(), request.contents)
        val uuid = request
            .let { parcelRepo.create(parcelRecord) }
        saveToOutbox(parcelRecord)
        return uuid
    }

    @Transactional
    fun updateParcel(parcelId: UUID, request: ParcelRequest) {
        val parcelRecord = toParcelRecord(parcelId, request.contents)
        request.let { parcelRepo.update(parcelRecord) }
        saveToOutbox(parcelRecord)
    }

    private fun saveToOutbox(parcelRecord: ParcelRecord) {
        val outboxEvent = createOutboxEvent(parcelRecord, objectMapper)
        eventService.handleOutboxEvent(outboxEvent)
    }
}

fun createOutboxEvent(record: ParcelRecord, mapper: ObjectMapper): OutboxEvent {
    val payload = mapper.convertValue(record, JsonNode::class.java)
    val eventType = "PARCEL.OUTBOX" // Magic string! <refactor>
    return OutboxEvent(eventType, payload)
}

fun toParcelRecord(uuid: UUID, contents: String): ParcelRecord =
    ParcelRecord(id = uuid, contents = contents, tags = listOf("tag-1", "tag-2", "tag-3"))
