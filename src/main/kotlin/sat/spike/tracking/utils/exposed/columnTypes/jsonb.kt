package sat.spike.tracking.utils.exposed.columnTypes
// reference: https://gist.github.com/quangIO/a623b5caa53c703e252d858f7a806919
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jetbrains.exposed.sql.BooleanColumnType
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.Function
import org.jetbrains.exposed.sql.IColumnType
import org.jetbrains.exposed.sql.IntegerColumnType
import org.jetbrains.exposed.sql.QueryBuilder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.VarCharColumnType
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.postgresql.util.PGobject

fun <T : Any> Table.jsonb(
    name: String,
    klass: Class<T>,
    jsonMapper: ObjectMapper = jacksonObjectMapper(),
    nullable: Boolean): Column<T> {
    return registerColumn<T>(name, JsonColumnType(klass, jsonMapper, nullable))
}

class JsonColumnType<out T : Any>(
    private val klass: Class<T>,
    private val jsonMapper: ObjectMapper,
    override var nullable: Boolean
) : IColumnType {

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        val obj = PGobject()
        obj.type = "jsonb"
        if (value != null)
            obj.value = value as String
        stmt[index] = obj
    }

    override fun valueFromDB(value: Any): Any = when (value) {
        is HashMap<*, *> -> value
        is Map<*, *> -> value
        else -> {
            value as PGobject
            try {
                val json = value.value
                jsonMapper.readValue<T>(json, klass)
            } catch (e: Exception) {
                e.printStackTrace()
                throw RuntimeException("Can't parse JSON: $value")
            }
        }
    }

    override fun notNullValueToDB(value: Any): Any = jsonMapper.writeValueAsString(value)
    override fun nonNullValueToString(value: Any): String = "'${jsonMapper.writeValueAsString(value)}'"
    override fun sqlType() = "jsonb"
}

class JsonKey(val key: String) : Expression<String>() {
    init {
        if (!key.matches("[a-zA-Z]+".toRegex())) throw IllegalArgumentException("Only simple json key allowed.")
    }

    override fun toQueryBuilder(queryBuilder: QueryBuilder) = queryBuilder { append(key) }
}

inline fun <reified T> Column<Map<*, *>>.json(jsonKey: JsonKey): Function<T> {
    val columnType = when (T::class) {
        Int::class -> IntegerColumnType()
        String::class -> VarCharColumnType()
        Boolean::class -> BooleanColumnType()
        else -> throw java.lang.RuntimeException("Column type ${T::class} not supported for json field.")
    }

    return json(jsonKey, columnType)
}

fun <T> Column<Map<*, *>>.json(jsonKey: JsonKey, columnType: IColumnType): Function<T> {
    return JsonVal<T>(expr = this, jsonKey = jsonKey, columnType = columnType)
}

private class JsonVal<T>(
    val expr: Expression<*>,
    val jsonKey: JsonKey,
    override val columnType: IColumnType
) : Function<T>(columnType) {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) =
        queryBuilder {
            append("CAST((${expr.toQueryBuilder(queryBuilder)} ->> '${jsonKey.key}') AS ${columnType.sqlType()})")
        }
}
