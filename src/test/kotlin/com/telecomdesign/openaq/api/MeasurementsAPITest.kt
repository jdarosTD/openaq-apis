package com.telecomdesign.openaq.api

import com.google.gson.*
import com.telecomdesign.openaq.core.OAQ
import com.telecomdesign.openaq.model.Measurement
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

import  com.telecomdesign.openaq.model.Result as OpenResult

/**
 * Created by Jonathan DA ROS on 03/06/2019.
 */
class MeasurementsAPITest {



    class DateSerializer : JsonDeserializer<Instant?> {

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Instant? {
            return   json?.let { Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(it.asString))}
        }
//
//        override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
//            return JsonPrimitive(src?.time)
//        }
    }


    @Test
    fun test1() {

        val gson = GsonBuilder()
            .registerTypeAdapter(Instant::class.java, MeasurementsAPITest.DateSerializer())
            .create()

        val apis = Retrofit.Builder().baseUrl(OAQ.OAQ_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(MeasurementsAPI::class.java)

        val subscriber: TestObserver<com.telecomdesign.openaq.model.Result> = TestObserver.create()

        val scheduler = Schedulers.trampoline()
        val from = Instant.now().minusSeconds(60 * 60 *24)
        val to = Instant.now()

        val obs = Observable.create(ObservableOnSubscribe<com.telecomdesign.openaq.model.Result> { emitter ->
            try {
                val resp= apis.get(
                    coordinates = "44.841225,-0.580036", radius = 5000,
                    dateFrom =DateTimeFormatter.ISO_INSTANT.format(from) ,
                    dateTo = DateTimeFormatter.ISO_INSTANT.format(to),

                    orderBy = arrayOf("date"), sort = arrayOf("desc")
                ).execute()
                if(resp.isSuccessful && resp.body() != null)  {
                    emitter.onNext(resp.body()!!)
                } else emitter.onError(Throwable("Code $" + resp.code() + "Message : " + resp.message()))
            } catch(e : Exception){
                emitter.onError(e)
            }
        })


        obs.subscribeOn(scheduler).subscribe(subscriber)
        subscriber.assertValue {
            it != null
        }

    }

    @Test
    fun test2() {
        val oaq = OAQ(null)

        val from = Instant.now().minusSeconds(60 * 60 *24)
        val to = Instant.now()

        val measureFuture = oaq.getMeasurementsByCoord("44.841225", "-0.580036",
            start = from, end= to)

//        val subscriber: TestObserver<List<Measurement>> = TestObserver.create()
//        val scheduler = Schedulers.trampoline()
//        val obs= Observable.fromFuture(measureFuture).subscribeOn(scheduler)
//        obs.subscribe(subscriber)

//        subscriber.assertValue {
//            it.isNotEmpty()
//        }

        assert(measureFuture.isNotEmpty())
    }

}