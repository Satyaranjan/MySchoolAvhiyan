package com.satyasoft.myschoolavhiyan.activity.ui.studentCollectionDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.satyasoft.myschoolavhiyan.model.StudentCollectionDetailsData

class StudentCollectionViewModel : ViewModel() {
    var servicesLiveData: MutableLiveData<StudentCollectionDetailsData>? = null

    fun getUser() : LiveData<StudentCollectionDetailsData>? {
        servicesLiveData = MainActivityRepository.getServicesApiCall()
        return servicesLiveData
    }
}