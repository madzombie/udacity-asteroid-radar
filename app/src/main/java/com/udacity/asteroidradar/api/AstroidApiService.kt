package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


// define a builded Json moshi object
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// define a builded Retrofit object
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()


private val retrofitImg = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()
private val retroImage = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface AstroidApiService {
   // @GET("neo/rest/v1/feed?start_date=START_DATE&end_date=END_DATE&api_key=YOUR_API_KEY")

    // added start_date and end_date in query finally, added api_key in end of the url
    @GET("neo/rest/v1/feed")
    suspend fun getProperty(@Query("start_date")start_date:String,@Query("end_date")end_date:String,@Query("api_key")api_key:String):String

    @GET("planetary/apod")
    suspend fun getPicOfDay(@Query("api_key")api_key: String):PictureOfDay

}

// created public retrofit object, we will call the object in viewModel
object AstroidApi {
    val retrofitService: AstroidApiService by lazy {
        retrofit.create(AstroidApiService::class.java)
    }
    val retroFitImgService: AstroidApiService by lazy {
        retrofit.create(AstroidApiService::class.java)
    }
}