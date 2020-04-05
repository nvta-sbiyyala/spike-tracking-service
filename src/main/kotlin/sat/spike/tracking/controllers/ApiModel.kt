package sat.spike.tracking.controllers

import com.fasterxml.jackson.annotation.JsonCreator

data class ParcelRequest @JsonCreator constructor(val contents: String)