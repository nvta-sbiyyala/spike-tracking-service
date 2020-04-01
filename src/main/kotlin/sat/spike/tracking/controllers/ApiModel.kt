package sat.spike.tracking.controllers

import com.fasterxml.jackson.annotation.JsonCreator

data class NewParcelRequest @JsonCreator constructor(val contents: String)