package com.sergio.monthlyne.entity

import java.io.Serializable

data class TimelineInformation(
        var timelineId :String = "",
        var timelineDate : String = ""
) : Serializable
