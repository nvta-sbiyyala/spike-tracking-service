package sat.spike.tracking.controllers

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sat.spike.tracking.db.ParcelRepo
import java.util.UUID

@RestController
class ParcelController(private val parcelRepo: ParcelRepo) {
    @PostMapping("/api/parcel",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createParcel(@RequestBody request: NewParcelRequest): UUID = request
        .let { parcelRepo.create(UUID.randomUUID(), request.contents) }
}