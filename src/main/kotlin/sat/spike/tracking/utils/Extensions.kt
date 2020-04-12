package sat.spike.tracking.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.ResultSet

fun JsonNode.toString(objectMapper: ObjectMapper): String = objectMapper.writeValueAsString(this)

fun <T : Any> String.execAndMap(transform: (ResultSet) -> T): List<T> {
    val result = arrayListOf<T>()
    TransactionManager.current().exec(this) { rs ->
        while (rs.next()) {
            result += transform(rs)
        }
    }
    return result
}