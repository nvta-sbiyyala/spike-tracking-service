package sat.spike.tracking.events

import com.fasterxml.jackson.databind.JsonNode

data class OutboxEvent(val eventType: String, val payload: JsonNode)
