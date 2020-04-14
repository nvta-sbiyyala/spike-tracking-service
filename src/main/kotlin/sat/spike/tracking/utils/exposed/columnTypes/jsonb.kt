package sat.spike.tracking.utils.exposed.columnTypes
// reference: https://github.com/JetBrains/Exposed/issues/127#issuecomment-612918775
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

fun <T : Any> Table.jsonb(name: String, jsonParser: IJsonParser, nullable: Boolean): Column<T> {
    return registerColumn<T>(name, ParsedJsonColumnType<T>(jsonParser, nullable))
}

// Interface
interface IJsonParser {
    fun fromJson(json: String): Any?
    fun toJson(source: Any): String
}

class ParsedJsonColumnType<out T : Any>(private val parser: IJsonParser, override var nullable: Boolean) : IColumnType {
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
            parser.fromJson(value.value) ?: throw RuntimeException("Cannot parse JSON: $value")
        }
    }

    override fun notNullValueToDB(value: Any): Any = parser.toJson(value)
    override fun nonNullValueToString(value: Any): String = "'${parser.toJson(value)}'"
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
