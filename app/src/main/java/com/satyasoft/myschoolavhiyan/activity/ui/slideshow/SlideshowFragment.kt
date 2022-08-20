package com.satyasoft.myschoolavhiyan.activity.ui.slideshow

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.satyasoft.myschoolavhiyan.adapter.StudentDetails
import com.satyasoft.myschoolavhiyan.databinding.FragmentSlideshowBinding
import com.satyasoft.myschoolavhiyan.utils.ResultOf


class SlideshowFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    var coursesArrayList: ArrayList<String>? = null
    var reference: DatabaseReference? = null
    private var _binding: FragmentSlideshowBinding? = null
    val studentDetails: ArrayList<StudentDetails?>? = null
    private val binding get() = _binding!!
    private var adapter: CustomAdapter? = null
    private lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)

        val root: View = binding.root
        recyclerView = binding.studentList
        recyclerView!!.setHasFixedSize(true);
        recyclerView!!.layoutManager = LinearLayoutManager(activity);
        progressBar = binding.progressBarLarge
         slideshowViewModel.studentDetails()

        progressBar.visibility = View.VISIBLE
            slideshowViewModel.taxInfoMutableLiveDataList.observe(viewLifecycleOwner, Observer {result ->
                result?.let {
                    when(it){
                        is ResultOf.Success ->{
                            val response = it.value
                            if(response.size > 0) {
                                populateAdapter(response)
                                progressBar.visibility = View.GONE
                            }
                        }
                        is ResultOf.Failure -> {
                            val failedMessage =  it.message ?: "Unknown Error"
                            Toast.makeText(requireContext(),"Data fetch  failed $failedMessage", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })



        return root
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun populateAdapter(studentInfoList : MutableList<StudentDetails>){
       adapter =  CustomAdapter(studentInfoList)
        recyclerView?.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}