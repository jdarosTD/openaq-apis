package com.telecomdesign.openaq.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Jonathan DA ROS on 03/06/2019.
 */

data class Measurement(

    @field:SerializedName("date") val date : OaqDate,
    @field:SerializedName("parameter") val parameter : String,
    @field:SerializedName("value") val value : Float,
    @field:SerializedName("unit") val unit : String,
    @field:SerializedName("location") val location : String,
    @field:SerializedName("country") val country : String,
    @field:SerializedName("city") val city : String,
    @field:SerializedName("coordinates") val coordinates : Coordinates,
    @field:SerializedName("sourceName") val sourceName : String

)



