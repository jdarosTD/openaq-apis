package com.telecomdesign.openaq.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Jonathan DA ROS on 03/06/2019.
 */
data class Result(@field:SerializedName("meta") val meta : Meta, @field:SerializedName("results") val results : List<Measurement>)

