package com.example.mobile_development.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://uselessfacts.jsph.pl/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface FactApiService {

    @GET("random.json?language=en")
    suspend fun getFact(): Fact
}

object FactApi {
    val retrofitService : FactApiService by lazy { retrofit.create(FactApiService::class.java) }
}