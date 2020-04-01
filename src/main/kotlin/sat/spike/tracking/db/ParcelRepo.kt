package sat.spike.tracking.db

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ParcelRepo {
    fun create(id: UUID, contents: String): UUID {
        return transaction {
            ParcelTable.insert {
                it[ParcelTable.id] = id
                it[ParcelTable.contents] = contents
            } get ParcelTable.id
        }
    }
}