package sat.spike.tracking.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import sat.spike.tracking.utils.exposed.columnTypes.IJsonParser

class JacksonParser<T>(
    private val klass: Class<T>,
    private val jsonMapper: ObjectMapper = jacksonObjectMapper()
) : IJsonParser {

    override fun fromJson(json: String): T = jsonMapper.readValue<T>(json, klass)

    override fun toJson(source: Any): String = jsonMapper.writeValueAsString(source)
}
