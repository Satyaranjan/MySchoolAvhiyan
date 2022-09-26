package com.satyasoft.myschoolavhiyan.retrofitApi

import com.satyasoft.myschoolavhiyan.database.StaffRegistration
import com.satyasoft.myschoolavhiyan.database.StudentDetails
import com.satyasoft.myschoolavhiyan.model.StudentCollectionDetailsData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface ApiInterface {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET(ApiEndpoints.GET_SPREADSHEET)
    fun getSpreadSheet(): Call<List<StudentCollectionDetailsData>>

}