package sat.spike.tracking.db

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ParcelRepo {
    fun create(parcelRecord: ParcelRecord): UUID =
        ParcelTable.insert {
            it[ParcelTable.id] = parcelRecord.id
            it[ParcelTable.contents] = parcelRecord.contents
        } get ParcelTable.id

    fun update(parcelRecord: ParcelRecord) =
        ParcelTable.update {
            it[ParcelTable.id] = parcelRecord.id
            it[ParcelTable.contents] = parcelRecord.contents
        }
}