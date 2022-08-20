package com.satyasoft.myschoolavhiyan.activity.ui.restration

import android.provider.Settings.Global.getString
import androidx.annotation.NonNull
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.adapter.StudentDetails
import com.satyasoft.myschoolavhiyan.utils.CustomDialogs
import com.satyasoft.myschoolavhiyan.utils.ResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel(),
    LifecycleObserver {
        private val LOG_TAG = "RegistrationViewModel"
        private var auth: FirebaseAuth? = null
        private lateinit var storage: FirebaseStorage
        private lateinit var storageReference: StorageReference
        private lateinit var rootNode: FirebaseDatabase
        private lateinit var reference: DatabaseReference
        private var database: DatabaseReference = Firebase.database.reference

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
    val saveResult: LiveData<ResultOf<String>> = _saveResult
    fun saveTaxDetails(userId: String,studentDetails: StudentDetails){
        loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO){
            val errorCode = -1
            try{
                    val id: String? = reference.push().key

                    reference.addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(@NonNull snapshot: DataSnapshot) {
                        if (id != null) {
                           reference.child(id).setValue(studentDetails)
                        }

                        _saveResult.postValue(ResultOf.Success("Data Saved Successfully"))
                        loading.postValue(false)
                    }
                    override fun onCancelled(@NonNull error: DatabaseError) {
                        _saveResult.postValue(ResultOf.Success("Data Save Failed"))
                        loading.postValue(false)
                    }
                })
            }catch (e:Exception){
                e.printStackTrace()
                loading.postValue(false)
                if(errorCode != -1){
                    _saveResult.postValue(ResultOf.Failure(
                        "Failed with Error Code $errorCode ",
                        "error"
                    ))
                }else{
                    _saveResult.postValue(ResultOf.Failure(
                        "Failed with Exception ${e.message} ",
                        "error"
                    ))
                }


            }
        }
    }
}