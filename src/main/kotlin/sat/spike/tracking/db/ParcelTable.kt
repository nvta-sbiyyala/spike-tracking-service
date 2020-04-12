package sat.spike.tracking.db

import java.util.UUID
import org.jetbrains.exposed.sql.Table

object ParcelTable : Table("parcel") {
    val id = uuid("id")
    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "author_pkey")
    val contents = text("contents")
}

data class ParcelRecord(val id: UUID, val contents: String)
