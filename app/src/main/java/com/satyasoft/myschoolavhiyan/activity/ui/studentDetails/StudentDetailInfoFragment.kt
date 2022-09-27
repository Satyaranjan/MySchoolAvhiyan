package com.satyasoft.myschoolavhiyan.activity.ui.studentDetails

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.adapter.CustomAdapter
import com.satyasoft.myschoolavhiyan.database.StudentCollectionDetails
import com.satyasoft.myschoolavhiyan.databinding.FragmentSlideshowBinding
import com.satyasoft.myschoolavhiyan.utils.DataImportAndExportCsvFile
import com.satyasoft.myschoolavhiyan.utils.DataImportAndExportExcelSheet
import com.satyasoft.myschoolavhiyan.utils.ResultOf
import java.util.*


class StudentDetailInfoFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var _binding: FragmentSlideshowBinding? = null
    private  var studentInfoLists : MutableList<StudentCollectionDetails>? = null
    private val binding get() = _binding!!
    private var adapter: CustomAdapter? = null
    private lateinit var progressBar: ProgressBar
    var  studentDetails : StudentCollectionDetails? = null

    private val EXCEL_FILE_NAME = "Mo School Abhiyan.xls"



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,

        ): View {
        val studentDetailViewModel =
            ViewModelProvider(this).get(StudentDetailInfoViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setHasOptionsMenu(true)
        recyclerView = binding.studentList
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        progressBar = binding.progressBarLarge
        activity?.let { studentDetailViewModel.studentDetails(it) }

        progressBar.visibility = View.VISIBLE


        studentDetailViewModel.taxInfoMutableLiveDataList.observe(viewLifecycleOwner, Observer {result ->
                result?.let {
                    when(it){
                        is ResultOf.Success ->{
                            val response = it.value
                            if(response.size > 0) {
                                populateAdapter(response)
                                progressBar.visibility = View.GONE

                            }else{
                                progressBar.visibility = View.GONE
                                Toast.makeText(requireContext(),"No Data Found !!!", Toast.LENGTH_LONG).show()
//                                val studentInfoList = mutableListOf<StudentCollectionDetails>()
//                                val getUserId = SchoolMasterDatabase.getSchoolMasterDataBase(requireContext())
//                                    .studentCollectionRegistrationDAO().getAllStudentCollectionRecord()
//                                studentInfoList.clear()
//                                if (getUserId.isNotEmpty()) {
//                                    studentInfoList.addAll(getUserId)
//                                }
//
//                                for (index in 0 until studentInfoList.size) {
//                                    val studentDetail = StudentCollectionDetails(
//                                          studentInfoList[index].id,
//                                        studentInfoList[index].name,
//                                        studentInfoList[index].batch,
//                                        studentInfoList[index].amount,
//                                        studentInfoList[index].date,
//                                        studentInfoList[index].paymentMethod,
//                                        studentInfoList[index].msgReceivedFrom,
//                                        studentInfoList[index].accountStatus,
//                                        studentInfoList[index].contactNo,
//                                        studentInfoList[index].emailId,
//                                        studentInfoList[index].remarks)
//                                    studentDetailViewModel.saveStudentCollectionsDetails(MainActivity.userId,
//                                        studentDetail)
//                                }

                            }
                        }
                        is ResultOf.Failure -> {
                            val failedMessage =  it.message ?: "Unknown Error"
                            Toast.makeText(requireContext(),"Data fetch  failed $failedMessage", Toast.LENGTH_LONG).show()
                        }
                    }
                }

//            studentDetailViewModel.saveResult.observe(viewLifecycleOwner) {result ->
//                result?.let {
//                    when(it){
//                        is ResultOf.Success ->{
//                            if(it.value.equals("Data Saved Successfully",ignoreCase = true)){
//                                Toast.makeText(requireContext(),"Saved Successfully", Toast.LENGTH_LONG).show()
//                            }else{
//                                CustomDialogs.commonDialog(
//                                    activity,
//                                    getString(R.string.loader_message),
//                                    getString(R.string.data_not_saved_fb),
//                                    getString(R.string.dialog_ok_button)
//                                )
//
//                            }
//                        }
//                        is ResultOf.Failure -> {
//                            val failedMessage =  it.message ?: "Unknown Error"
//                            Toast.makeText(requireContext(),"Save failed $failedMessage", Toast.LENGTH_LONG).show()
//                        }
//                    }
//                }
//
//            }
            })


        return root
    }
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged")
    private fun populateAdapter(studentInfoList : MutableList<StudentCollectionDetails>){
       adapter =  CustomAdapter(studentInfoList)
        recyclerView?.adapter = adapter
        adapter!!.notifyDataSetChanged()
        this.studentInfoLists = studentInfoList
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.activity_student_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.queryHint = "Please Enter Year"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(searchItem: String): Boolean {
                filter(searchItem)
                return false
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> {
            if(studentInfoLists?.isNotEmpty() == true) {
                studentInfoLists?.let { DataImportAndExportCsvFile.exportDataInToCSV(it,requireContext()) }
                DataImportAndExportCsvFile.sharedBySocialMedia(requireContext())
                studentInfoLists?.let {
                    DataImportAndExportExcelSheet.exportDataIntoExcelWorkbook(requireContext(),EXCEL_FILE_NAME,
                        it)
                }
            }
            true
        }
        R.id.action_search -> {
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun filter(text: String) {
        val filterList: ArrayList<StudentCollectionDetails> = ArrayList()
        if (studentInfoLists != null) {
            for (item in studentInfoLists!!) {
                if (item != null) {
                    if (item.batch?.lowercase(Locale.ROOT)?.contains(text.lowercase(Locale.ROOT)) == true || item.name?.lowercase(Locale.ROOT)?.contains(text.lowercase(Locale.ROOT)) == true) {
                        filterList.add(item)
                    }
                }
            }
        }
        if (filterList.isEmpty()) {
            Toast.makeText(requireContext(), "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            adapter?.filterList(filterList)
        }
    }
}