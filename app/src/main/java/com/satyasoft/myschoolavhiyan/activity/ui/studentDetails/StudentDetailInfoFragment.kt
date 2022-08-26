package com.satyasoft.myschoolavhiyan.activity.ui.studentDetails

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.adapter.CustomAdapter
import com.satyasoft.myschoolavhiyan.database.StudentDetails
import com.satyasoft.myschoolavhiyan.databinding.FragmentSlideshowBinding
import com.satyasoft.myschoolavhiyan.pdfService.AppPermission
import com.satyasoft.myschoolavhiyan.pdfService.AppPermission.Companion.permissionGranted
import com.satyasoft.myschoolavhiyan.pdfService.AppPermission.Companion.requestPermission
import com.satyasoft.myschoolavhiyan.pdfService.FileHandler
import com.satyasoft.myschoolavhiyan.pdfService.PdfService
import com.satyasoft.myschoolavhiyan.utils.ResultOf
import java.io.File


class StudentDetailInfoFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    var coursesArrayList: ArrayList<String>? = null
    var reference: DatabaseReference? = null
    private var _binding: FragmentSlideshowBinding? = null
    private val studentDetails: ArrayList<StudentDetails?>? = null
    private lateinit var studentInfoLists : MutableList<StudentDetails>
    private val binding get() = _binding!!
    private var adapter: CustomAdapter? = null
    private lateinit var progressBar: ProgressBar
    private var searchView: SearchView? = null

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

        setHasOptionsMenu(true)
        recyclerView = binding.studentList
        recyclerView!!.setHasFixedSize(true);
        recyclerView!!.layoutManager = LinearLayoutManager(activity);
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
                                if (!activity?.let { permissionGranted(it) }!!) requestPermission(activity)
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
    private fun populateAdapter(studentInfoList : MutableList<StudentDetails>){
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
            createPdf(studentInfoLists)
            sharedBySocialMedia()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == AppPermission.REQUEST_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                requestPermission(activity as FragmentActivity)
                toastErrorMessage("Permission should be allowed")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createPdf(studentInfoList: MutableList<StudentDetails>) {
        val onError: (Exception) -> Unit = { toastErrorMessage(it.message.toString()) }
       // val onFinish: (File) -> Unit = { openFile(it) }
        val paragraphList = listOf(
            getString(R.string.copy_right)
        )
        val pdfService = PdfService()
        pdfService.createUserTable(
            data = studentInfoList,
            paragraphList = paragraphList,
           // onFinish = onFinish,
            onError = onError
        )
    }
  //  /storage/emulated/0/Download/MoSchoolAbhiyan.pdf
    private fun openFile(file: File) {
        val path = FileHandler().getPathFromUri(requireContext(), file.toUri())
        val pdfFile = path?.let { File(it) }
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
        val pdfIntent = Intent(Intent.ACTION_VIEW)
        if (pdfFile != null) {
            pdfIntent.setDataAndType(pdfFile.toUri(), "application/pdf")
        }
        pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        try {
            startActivity(pdfIntent)
        } catch (e: ActivityNotFoundException) {
            toastErrorMessage("Can't read pdf file")
        }

    }

    private fun toastErrorMessage(s: String) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filterList: ArrayList<StudentDetails> = ArrayList()

        // running a for loop to compare elements.
        if (studentInfoLists != null) {
            for (item in studentInfoLists) {
                if (item != null) {
                    if (item.yearOfPass?.toLowerCase()?.contains(text.toLowerCase()) == true) {
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
                .toString() + "/" + "MoSchoolAbhiyan.pdf")
        if (file.exists()) {
            val uri = getUriForFile(requireContext(), "com.satyasoft.myschoolavhiyan" + ".provider", file)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setDataAndType(uri, "application/pdf")
            shareIntent.setDataAndType(uri,"text/html")
            shareIntent.setDataAndType(uri,"message/rfc822")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("satya.igu@gmail.com"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Mo School Abhiyan")
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Thanks your support for Mo School Abhiyan \n \n  Regards, \n \n H.M PBBP,Shibapura")
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION
            requireContext().startActivity(shareIntent)
        }

    }

}