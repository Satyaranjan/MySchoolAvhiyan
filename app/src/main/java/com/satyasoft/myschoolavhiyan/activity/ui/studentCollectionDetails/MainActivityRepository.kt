package com.satyasoft.myschoolavhiyan.activity.ui.studentCollectionDetails


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.satyasoft.myschoolavhiyan.model.StudentCollectionDetailsData
import com.satyasoft.myschoolavhiyan.retrofitApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MainActivityRepository {

    private val serviceSetterGetter = MutableLiveData<StudentCollectionDetailsData>()

    fun getServicesApiCall(): MutableLiveData<StudentCollectionDetailsData> {

        val call = ApiClient.getClient.getSpreadSheet()

        with(call) {

            class StudentCollectionDetailsDataListCallback :
                Callback<List<StudentCollectionDetailsData>> {
                    override fun onFailure(call: Call<List<StudentCollectionDetailsData>>, t: Throwable) {
                        // TODO("Not yet implemented")
                        Log.v("DEBUG : ", t.message.toString())
                    }

                    override fun onResponse(
                        call: Call<List<StudentCollectionDetailsData>>,
                        response: Response<List<StudentCollectionDetailsData>>
                    ) {

                        Log.v("DEBUG : ", response.body().toString())

                        val data = response.body()

                        val msg = data!![0].date

                      //  serviceSetterGetter.value = StudentCollectionDetailsData(id = msg.toString())
                    }
                }

            enqueue(StudentCollectionDetailsDataListCallback())
        }

        return serviceSetterGetter
    }
}