package com.satyasoft.myschoolavhiyan.activity.ui.studentDetails

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.satyasoft.myschoolavhiyan.database.SchoolMasterDatabase
import com.satyasoft.myschoolavhiyan.database.StudentCollectionDetails
import com.satyasoft.myschoolavhiyan.database.StudentDetails
import com.satyasoft.myschoolavhiyan.utils.NetworkConnectionStatus
import com.satyasoft.myschoolavhiyan.utils.ResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class StudentDetailInfoViewModel : ViewModel( ),LifecycleObserver {
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
    private val _taxInfoMutableLiveDataList = MutableLiveData<ResultOf<MutableList<StudentCollectionDetails>>>()
    val taxInfoMutableLiveDataList: LiveData<ResultOf<MutableList<StudentCollectionDetails>>> = _taxInfoMutableLiveDataList

    fun studentDetails(context:Context) {
        loading.postValue(true)
        val studentInfoList = mutableListOf<StudentCollectionDetails>()
          viewModelScope.launch(Dispatchers.IO) {
            val errorCode = -1
            try {
//                if(NetworkConnectionStatus.checkConnection(context)) {
//                    studentInfoList.clear()
//                    reference.addValueEventListener(object : ValueEventListener {
//                        override fun onDataChange(dataSnapshot: DataSnapshot) {
//                            for (postSnapshot in dataSnapshot.children) {
//                                val studentInfo =
//                                    postSnapshot.getValue(StudentCollectionDetails::class.java)
//                                if (studentInfo != null) {
//                                    studentInfoList.add(studentInfo)
//                                }
//
//                            }
//                            _taxInfoMutableLiveDataList.postValue(ResultOf.Success(studentInfoList))
//
//                        }
//
//                        override fun onCancelled(databaseError: DatabaseError) {
//                            // Getting Post failed, log a message
//                            Log.w("FireBaseViewModel",
//                                "loadPost:onCancelled",
//                                databaseError.toException())
//                            _taxInfoMutableLiveDataList.postValue(ResultOf.Failure("Failed with Error Code $errorCode ",
//                                "error"))
//                            loading.postValue(false)
//                        }
//                    })
//                }else{
                    viewModelScope.launch(Dispatchers.IO) {
                        val getUserId = SchoolMasterDatabase.getSchoolMasterDataBase(context)
                            .studentCollectionRegistrationDAO().getAllStudentCollectionRecord()
                        studentInfoList.clear()
                        if (getUserId.isNotEmpty()) {
                            studentInfoList.addAll(getUserId)
                        }
                    }
                    _taxInfoMutableLiveDataList.postValue(ResultOf.Success(studentInfoList))
                //}
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
//    val saveResult: LiveData<ResultOf<String>> = _saveResult
//    fun saveStudentCollectionsDetails(userId: String,studentDetails: StudentCollectionDetails){
//        loading.postValue(true)
//        viewModelScope.launch(Dispatchers.IO){
//            val errorCode = -1
//            try{
//                val id: String? = reference.push().key
//
//                reference.addValueEventListener(object : ValueEventListener {
//
//                    override fun onDataChange(@NonNull snapshot: DataSnapshot) {
//                        if (id != null) {
//                            reference.child(id).setValue(studentDetails)
//                        }
//
//                        _saveResult.postValue(ResultOf.Success("Data Saved Successfully"))
//                        loading.postValue(false)
//                    }
//                    override fun onCancelled(@NonNull error: DatabaseError) {
//                        _saveResult.postValue(ResultOf.Success("Data Save Failed"))
//                        loading.postValue(false)
//                    }
//                })
//            }catch (e:Exception){
//                e.printStackTrace()
//                loading.postValue(false)
//                if(errorCode != -1){
//                    _saveResult.postValue(ResultOf.Failure(
//                        "Failed with Error Code $errorCode ",
//                        "error"
//                    ))
//                }else{
//                    _saveResult.postValue(ResultOf.Failure(
//                        "Failed with Exception ${e.message} ",
//                        "error"
//                    ))
//                }
//
//
//            }
//        }
//    }

}


