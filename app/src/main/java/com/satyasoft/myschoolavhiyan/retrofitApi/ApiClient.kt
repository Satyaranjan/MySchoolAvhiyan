package com.satyasoft.myschoolavhiyan.retrofitApi

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL: String = ApiEndpoints.API_BASE_URL
    val getClient: ApiInterface
        get() {
             val client = OkHttpClient.Builder().build()
             val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiInterface::class.java)
        }

}

