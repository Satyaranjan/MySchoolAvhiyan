package com.satyasoft.myschoolavhiyan.activity.ui.studentGallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StudentGalleryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Gallery Fragment Coming Soon ---??"
    }
    val text: LiveData<String> = _text
}