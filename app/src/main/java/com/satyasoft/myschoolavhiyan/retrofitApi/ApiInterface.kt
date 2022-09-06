package com.satyasoft.myschoolavhiyan.retrofitApi

import com.satyasoft.myschoolavhiyan.database.StaffRegistration
import com.satyasoft.myschoolavhiyan.database.StudentDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface ApiInterface {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET(ApiEndpoints.GET_ORDERS)
    fun getOrder(
            @Query("key") key: String?,
            @Query("delivery_date") delivery_date: String?
    ): Call<List<StudentDetails>>

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET(ApiEndpoints.LOGIN)
    fun getUserId(
            @Query("email") email:String?
    ): Call<StaffRegistration>

}