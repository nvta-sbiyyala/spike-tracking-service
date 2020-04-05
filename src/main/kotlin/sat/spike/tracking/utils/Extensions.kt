package sat.spike.tracking.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

fun JsonNode.toString(objectMapper: ObjectMapper): String = objectMapper.writeValueAsString(this)