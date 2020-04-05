package sat.spike.tracking.controllers

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sat.spike.tracking.services.ParcelService
import java.util.UUID

@RestController
class ParcelController(private val parcelService: ParcelService) {
    @PostMapping("/api/parcel",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createParcel(@RequestBody request: ParcelRequest): UUID =
        parcelService.createParcel(request)

    @PutMapping("/api/parcel/{parcelId}",
        consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateParcel(@RequestBody request: ParcelRequest, @PathVariable parcelId: UUID) =
        parcelService.updateParcel(parcelId, request)
}