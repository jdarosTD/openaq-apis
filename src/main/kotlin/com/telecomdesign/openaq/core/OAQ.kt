package com.telecomdesign.openaq.core

import com.google.gson.*
import com.telecomdesign.openaq.api.APIException
import com.telecomdesign.openaq.api.MeasurementsAPI
import com.telecomdesign.openaq.model.Measurement
import com.telecomdesign.openaq.util.SystemTools
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import javax.swing.text.DateFormatter
import java.time.ZoneId




/**
 * Created by Jonathan DA ROS on 03/06/2019.
 */
class OAQ constructor(var okHttpClient: OkHttpClient?) {

    class DateSerializer : JsonDeserializer<Instant?> {

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Instant? {
            return   json?.let { Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(it.asString))}
        }
//
//        override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
//            return JsonPrimitive(src?.time)
//        }
    }

    companion object {
        val OAQ_URL = "https://api.openaq.org"

        private val gson = GsonBuilder()
            .registerTypeAdapter(Instant::class.java, DateSerializer())
            .create()

        val log = LoggerFactory.getLogger(OAQ::class.java.simpleName)
    }


    private var retrofit4Measurements: MeasurementsAPI

    init {
        if(this.okHttpClient == null) {
            this.okHttpClient = OkHttpClient.Builder()
                //.readTimeout(20, TimeUnit.SECONDS)
////            .connectTimeout(20, TimeUnit.SECONDS)
////            .writeTimeout(20, TimeUnit.SECONDS)
////            .cache(null)
////            .proxy(SystemTools.getSystemProxy())
                .addInterceptor { chain ->
                    val request = chain.request()
                    val response = chain.proceed(request)

                    // todo deal with the issues the way you need to
                    if (response.code() != 200) {
                        log.info("Error on Request, code {}, reason : {}", response.code(), response.body())
                    }
                    response
                }.build()
        }
        retrofit4Measurements = Retrofit.Builder().baseUrl(OAQ.OAQ_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(MeasurementsAPI::class.java)
    }


    fun getMeasurementsByCoord(latitude : String, longitude : String, radius : Int?= null,
                               start : Instant?= null, end : Instant?= null, limit: Int?= null,
                               page : Int?= null, parameters : Array<String>?= null) : List<Measurement> {


        val call = retrofit4Measurements.get(coordinates = "$latitude,$longitude",


            dateFrom = start?.let { DateTimeFormatter.ISO_INSTANT.format(it) },
            dateTo =   end?.let { DateTimeFormatter.ISO_INSTANT.format(it) },
            limit = limit,
            orderBy = arrayOf("date"),
            parameters = parameters,
            sort = arrayOf("desc"),
            radius = radius,
            page = page)

        val resp = call.execute()
        val measure = resp.body()?.results?: arrayListOf()
        if(measure.isEmpty()){
            log.error("Error while requesting measure : {} {}", resp.code(), resp.message())
            if(!resp.isSuccessful)throw APIException(resp.code(), resp.message())
        }
        return measure
    }

}