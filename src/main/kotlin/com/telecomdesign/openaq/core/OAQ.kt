package com.telecomdesign.openaq.core

import com.google.gson.GsonBuilder
import com.telecomdesign.openaq.api.MeasurementsAPI
import com.telecomdesign.openaq.model.Measurement
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit



/**
 * Created by Jonathan DA ROS on 03/06/2019.
 */
class OAQ {

    companion object {
         val OAQ_URL = "https://api.openaq.org"

        private val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()
    }


    private var retrofit4Measurements: MeasurementsAPI

    init {

        val okHttpClientBuilder = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)

        retrofit4Measurements = Retrofit.Builder().baseUrl(OAQ.OAQ_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(MeasurementsAPI::class.java)
    }


    fun getMeasurementsByCoord(latitude : String, longitude : String, radius : Int?= null,
                                start : Date?= null, end : Date?= null, limit: Int?= null,
                                page : Int?= null, parameters : Array<String>?= null) : Future<List<Measurement>> {

        val coord = "$latitude,$longitude"
        return retrofit4Measurements.get(coordinates = coord,
            dateFrom = start,
            dateTo = end,
            limit = limit,
            orderBy = arrayOf("date"),
            parameters = parameters,
            sort = arrayOf("desc"),
            radius = radius,
            page = page).map { it -> it.results }.toFuture()
    }

}