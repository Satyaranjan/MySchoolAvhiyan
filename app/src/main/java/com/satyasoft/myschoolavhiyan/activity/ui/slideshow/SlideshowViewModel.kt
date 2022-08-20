package com.satyasoft.myschoolavhiyan.activity.ui.slideshow

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.satyasoft.myschoolavhiyan.adapter.StudentDetails
import com.satyasoft.myschoolavhiyan.utils.ResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SlideshowViewModel : ViewModel( ),LifecycleObserver {
    private val LOG_TAG = "SlideshowViewModel"
    private var  auth: FirebaseAuth? = null
    private var storage: FirebaseStorage
    private var storageReference: StorageReference
    private  var rootNode: FirebaseDatabase
    private  var reference: DatabaseReference
    var loading: MutableLiveData<Boolean> = MutableLiveData()
    init {

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        rootNode = FirebaseDatabase.getInstance()
        reference = rootNode.getReference("studentDetails")
        loading.postValue(false)

    }
    private val _saveResult = MutableLiveData<ResultOf<String>>()
    private val _taxInfoMutableLiveDataList = MutableLiveData<ResultOf<MutableList<StudentDetails>>>()
    val taxInfoMutableLiveDataList: LiveData<ResultOf<MutableList<StudentDetails>>> = _taxInfoMutableLiveDataList
    fun studentDetails() {
        loading.postValue(true)
        val studentInfoList = mutableListOf<StudentDetails>()
        viewModelScope.launch(Dispatchers.IO) {
            var errorCode = -1
            try {

                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (postSnapshot in dataSnapshot.children) {
                            val studentInfo= postSnapshot.getValue(StudentDetails::class.java)
                            if (studentInfo != null) {
                                studentInfoList.add(studentInfo)
                            }
                        }
                        _taxInfoMutableLiveDataList.postValue(ResultOf.Success(studentInfoList))

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w("FireBaseViewModel", "loadPost:onCancelled", databaseError.toException())
                        _taxInfoMutableLiveDataList.postValue(ResultOf.Failure("Failed with Error Code $errorCode ", "error"))
                        loading.postValue(false)
                    }
                })

            } catch (e: Exception) {
                e.printStackTrace()
                loading.postValue(false)
                if (errorCode != -1) {
                    _saveResult.postValue(
                        ResultOf.Failure(
                            "Failed with Error Code $errorCode ",
                            "error",
                        )
                    )
                } else {
                    _saveResult.postValue(
                        ResultOf.Failure(
                            "Failed with Exception ${e.message} ",
                            "error",
                        )
                    )
                }


            }

        }
    }


}


