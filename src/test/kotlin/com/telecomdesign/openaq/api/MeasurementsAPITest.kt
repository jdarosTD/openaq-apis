package com.telecomdesign.openaq.api

import com.telecomdesign.openaq.model.Measurement
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import com.telecomdesign.openaq.core.OAQ
import io.reactivex.Observable


/**
 * Created by Jonathan DA ROS on 03/06/2019.
 */
class MeasurementsAPITest {


    @Test
    fun test1() {

        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()

        val apis = Retrofit.Builder().baseUrl(OAQ.OAQ_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(MeasurementsAPI::class.java)

        val subscriber: TestObserver<com.telecomdesign.openaq.model.Result> = TestObserver.create()

        val scheduler = Schedulers.trampoline()
        val obs = apis.get(
            coordinates = "44.841225,-0.580036", radius = 5000,
            orderBy = arrayOf("date"), sort = arrayOf("desc")
        )
            .subscribeOn(scheduler)

        obs.subscribe(subscriber)
        subscriber.assertValue {
            it != null
        }

    }

    @Test
    fun test2() {
        val oaq = OAQ(null)
        val measureFuture = oaq.getMeasurementsByCoord("44.841225", "-0.580036")

        val subscriber: TestObserver<List<Measurement>> = TestObserver.create()
        val scheduler = Schedulers.trampoline()
        val obs= Observable.fromFuture(measureFuture).subscribeOn(scheduler)
        obs.subscribe(subscriber)

        subscriber.assertValue {
            it.isNotEmpty()
        }
    }

}