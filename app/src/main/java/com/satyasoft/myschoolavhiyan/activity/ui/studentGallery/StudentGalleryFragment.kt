package com.satyasoft.myschoolavhiyan.activity.ui.studentGallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.satyasoft.myschoolavhiyan.adapter.ImageAdapter
import com.satyasoft.myschoolavhiyan.databinding.FragmentGalleryBinding
import com.satyasoft.myschoolavhiyan.utils.CustomDialogs
import com.satyasoft.myschoolavhiyan.utils.NetworkConnectionStatus


class StudentGalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private var imagelist: ArrayList<String>? = null
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var adapter: ImageAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val galleryViewModel =
            ViewModelProvider(this)[StudentGalleryViewModel::class.java]

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        imagelist = ArrayList()
        recyclerView =  binding.recyclerview
        adapter = ImageAdapter(imagelist!!, requireContext())

        recyclerView!!.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        progressBar = binding.progress

        if(NetworkConnectionStatus.checkConnection(activity)) {
            progressBar!!.visibility = View.VISIBLE
            val listRef = FirebaseStorage.getInstance().reference.child("images")

            listRef.listAll().addOnSuccessListener { listResult ->
                for (file in listResult.items) {
                    file.downloadUrl.addOnSuccessListener { uri ->
                        imagelist!!.add(uri.toString())
                        Log.e("Interval", uri.toString())
                    }.addOnSuccessListener {
                        recyclerView!!.adapter = adapter
                        progressBar!!.visibility = View.GONE
                    }
                }
            }
        }else{
            progressBar!!.visibility = View.GONE
            CustomDialogs.commonDialog(
                activity,
                getString(com.satyasoft.myschoolavhiyan.R.string.check_your_connectivity),
                getString(com.satyasoft.myschoolavhiyan.R.string.check_your_internet),
                getString(com.satyasoft.myschoolavhiyan.R.string.dialog_ok_button)
            )
        }
        galleryViewModel.text.observe(viewLifecycleOwner) {
           // textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}