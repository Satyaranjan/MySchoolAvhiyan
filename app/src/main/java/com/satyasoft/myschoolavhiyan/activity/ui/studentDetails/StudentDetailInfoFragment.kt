package com.satyasoft.myschoolavhiyan.activity.ui.studentDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider.getUriForFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opencsv.CSVWriter
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.adapter.CustomAdapter
import com.satyasoft.myschoolavhiyan.database.StudentCollectionDetails
import com.satyasoft.myschoolavhiyan.database.StudentDetails
import com.satyasoft.myschoolavhiyan.databinding.FragmentSlideshowBinding
import com.satyasoft.myschoolavhiyan.pdfService.AppPermission
import com.satyasoft.myschoolavhiyan.pdfService.AppPermission.Companion.permissionGranted
import com.satyasoft.myschoolavhiyan.pdfService.AppPermission.Companion.requestPermission
import com.satyasoft.myschoolavhiyan.utils.ResultOf
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*


class StudentDetailInfoFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var _binding: FragmentSlideshowBinding? = null
    private  var studentInfoLists : MutableList<StudentCollectionDetails>? = null
    private val binding get() = _binding!!
    private var adapter: CustomAdapter? = null
    private lateinit var progressBar: ProgressBar

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,

        ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(StudentDetailInfoViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root
      //  if (!activity?.let { permissionGranted(it) }!!) requestPermission(activity)

        setHasOptionsMenu(true)
        recyclerView = binding.studentList
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        progressBar = binding.progressBarLarge
        activity?.let { slideshowViewModel.studentDetails(it) }

        progressBar.visibility = View.VISIBLE
            slideshowViewModel.taxInfoMutableLiveDataList.observe(viewLifecycleOwner, Observer {result ->
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
                studentInfoLists?.let { exportCSV(it) }
                sharedBySocialMedia()
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

//    @Deprecated("Deprecated in Java")
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String?>,
//        grantResults: IntArray,
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == AppPermission.REQUEST_PERMISSION) {
//            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                requestPermission(activity as FragmentActivity)
//                toastErrorMessage("Permission should be allowed")
//            }
//        }
//    }
//
//    private fun toastErrorMessage(s: String) {
//        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
//    }

    private fun filter(text: String) {
        val filterList: ArrayList<StudentCollectionDetails> = ArrayList()
        if (studentInfoLists != null) {
            for (item in studentInfoLists!!) {
                if (item != null) {
                    if (item.batch?.toLowerCase(Locale.ROOT)?.contains(text.lowercase(Locale.ROOT)) == true) {
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

    private fun sharedBySocialMedia(){
        val file =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/" + "/CSV/MoSchoolAbhiyan.csv")
        if (file.exists()) {
            val uri = getUriForFile(requireContext(), "com.satyasoft.myschoolavhiyan" + ".provider", file)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setDataAndType(uri, "application/pdf")
            shareIntent.setDataAndType(uri,"text/html")
            shareIntent.setDataAndType(uri,"message/rfc822")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("satya.igu@gmail.com"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title_csv))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.gmail_message))
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION
            requireContext().startActivity(shareIntent)
        }

    }
    private fun exportCSV(studentInfoList: MutableList<StudentCollectionDetails>){
        if(studentInfoList.isNotEmpty()) {
            val exportDir =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString(), "/CSV")// your path where you want save your file
            if (!exportDir.exists()) {
                exportDir.mkdirs()
            }

            try {
                val mySchoolStudentDetails = "MoSchoolAbhiyan"
                val file = File(exportDir, "$mySchoolStudentDetails.csv")
                file.createNewFile()
                val writer = CSVWriter(FileWriter(file))
                val messageTitle = arrayOf(getString(R.string.title_csv))
                writer.writeNext(messageTitle)
                val schoolDetails = arrayOf(getString(R.string.school_address))
                writer.writeNext(schoolDetails)

                val header = arrayOf("Id", "NAME", "Date", "ContactNo", "BATCH", "AMOUNT")
                writer.writeNext(header)

                val data: MutableList<Array<String?>> = ArrayList()
                for (index in 0 until studentInfoList.size) {
                    arrayOf(studentInfoList[index].id.toString(),
                        studentInfoList[index].name,
                        studentInfoList[index].date,
                        studentInfoList[index].contactNo,
                        studentInfoList[index].batch,
                        studentInfoList[index].amount).let {
                        data.add(it)
                    }
                }
                writer.writeAll(data)
                writer.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}