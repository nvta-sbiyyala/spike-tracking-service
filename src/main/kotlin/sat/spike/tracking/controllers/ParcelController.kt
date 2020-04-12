package sat.spike.tracking.controllers

import java.util.UUID
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sat.spike.tracking.services.ParcelService

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

    @GetMapping("/api/parcel/{parcelId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getParcel(@PathVariable parcelId: UUID) =
        parcelService.getParcel(parcelId)

    @GetMapping("/api/parcel/{parcelId}/history",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getParcelHistory(@PathVariable parcelId: UUID) =
        parcelService.getParcelHistory(parcelId)
}
