package com.telecomdesign.openaq.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Jonathan DA ROS on 03/06/2019.
 */
data class Coordinates(@field:SerializedName("latitude")   val latitude :  Double,
                       @field:SerializedName("longitude") val longitude :  Double)