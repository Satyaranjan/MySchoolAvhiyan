package com.satyasoft.myschoolavhiyan.activity.ui.studentCollectionDetails

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.satyasoft.myschoolavhiyan.R

class StudentCollectionFragment : Fragment() {

    companion object {
        fun newInstance() = StudentCollectionFragment()
    }

    private lateinit var viewModel: StudentCollectionViewModel

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_student_collection, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lateinit var context: Context

        viewModel = ViewModelProvider(this).get(StudentCollectionViewModel::class.java)
        // TODO: Use the ViewModel
        viewModel.getUser()!!.observe(viewLifecycleOwner, Observer { serviceSetterGetter ->

            val msg = serviceSetterGetter.date
            msg?.let { Log.d("Message", it) }

        })


    }

}