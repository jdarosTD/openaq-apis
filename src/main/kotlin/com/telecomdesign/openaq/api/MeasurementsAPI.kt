package com.telecomdesign.openaq.api

import com.telecomdesign.openaq.model.Measurement
import com.telecomdesign.openaq.model.Result
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

/**
 * Created by Jonathan DA ROS on 03/06/2019.
 */

interface MeasurementsAPI {
//
    @GET("v1/measurements")
    fun get(
        @Query("country") country : String?= null,
        @Query("city") city : String?= null,
        @Query("location") location : String?= null,
        @Query("parameter") parameters : Array<String>?= null,
        @Query("has_geo") hasGeo : Boolean?= null,
        @Query("coordinates") coordinates : String?= null,
        @Query("radius") radius : Int?= null,
        @Query("values_from") valuesFrom: Int?= null,
        @Query("value_to") valueTo : Int?= null,
        @Query("date_from") dateFrom : String?= null,
        @Query("date_to") dateTo : String?= null,
        @Query("order_by") orderBy : Array<String>?= null,
        @Query("sort") sort : Array<String>?= null,
        @Query("include_fields") includeFields : Array<String>?= null,
        @Query("limit") limit : Int?= null,
        @Query("page") page : Int?= null,
        @Query("format") format : String?= null) : Call<Result>
}