package com.telecomdesign.openaq.model

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.util.*

/**
 * Created by Jonathan DA ROS on 03/06/2019.
 */
data class OaqDate(@field:SerializedName("utc")   val utc : Instant,
                   @field:SerializedName("local") val local :  Date)