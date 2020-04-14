package sat.spike.tracking.db

import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import sat.spike.tracking.db.ParcelTable.contents
import sat.spike.tracking.db.ParcelTable.id

@Repository
class ParcelRepo {
    fun create(parcelRecord: ParcelRecord): UUID =
        ParcelTable.insert {
            it[ParcelTable.id] = parcelRecord.id
            it[ParcelTable.contents] = parcelRecord.contents
        } get ParcelTable.id

    fun update(parcelRecord: ParcelRecord) =
        ParcelTable.update({ ParcelTable.id eq parcelRecord.id }) {
            it[ParcelTable.contents] = parcelRecord.contents
        }

    fun fetchParcel(parcelId: UUID): ParcelRecordDto {
        return ParcelTable.select { ParcelTable.id eq parcelId }
            .limit(1)
            .first()
            .let { mapToResponse(it) }
    }

    private fun mapToResponse(row: ResultRow): ParcelRecordDto = ParcelRecordDto(
        id = row[id],
        contents = row[contents]
    )
}
