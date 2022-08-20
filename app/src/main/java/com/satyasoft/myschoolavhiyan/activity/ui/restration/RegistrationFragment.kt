package com.satyasoft.myschoolavhiyan.activity.ui.restration

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.FirebaseDatabase
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.activity.MainActivity
import com.satyasoft.myschoolavhiyan.adapter.StudentDetails
import com.satyasoft.myschoolavhiyan.databinding.FragmentHomeBinding
import com.satyasoft.myschoolavhiyan.utils.CustomDialogs
import com.satyasoft.myschoolavhiyan.utils.ResultOf

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class RegistrationFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var studentDetails: StudentDetails
    private var param1: String? = null
    private var param2: String? = null
    private var selectedImageUri: Uri? = null

    private val binding get() = _binding!!

     @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
       val  homeViewModel =
            ViewModelProvider(this).get(RegistrationViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val  name = _binding!!.name
        val  empEmail = _binding!!.emailid
        val  mobileNo = _binding!!.phoneNo
        val  year = _binding!!.yearOfPass
        val  amount = _binding!!.amount
        val saveButton = _binding!!.save

        homeViewModel.saveResult.observe(viewLifecycleOwner) {result ->
            result?.let {
                when(it){
                    is ResultOf.Success ->{
                        if(it.value.equals("Data Saved Successfully",ignoreCase = true)){
                           // Toast.makeText(requireContext(),"Tax Details Saved",Toast.LENGTH_LONG).show()
                            name.text?.clear()
                            empEmail.text?.clear()
                            mobileNo.text?.clear()
                            year.text?.clear()
                            amount.text?.clear()

                        }else{
                           // println("Data failed to save")
                            CustomDialogs.commonDialog(
                                activity,
                                getString(R.string.loader_message),
                                getString(R.string.data_not_saved_fb),
                                getString(R.string.dialog_ok_button)
                            )

                        }
                    }
                    is ResultOf.Failure -> {
                        val failedMessage =  it.message ?: "Unknown Error"
                        Toast.makeText(requireContext(),"Save failed $failedMessage", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }

        saveButton.setOnClickListener {
            if(TextUtils.isEmpty(name.text.toString()) || TextUtils.isEmpty(empEmail.text.toString())||TextUtils.isEmpty(mobileNo.text.toString()) ||
                TextUtils.isEmpty(year.text.toString()) || TextUtils.isEmpty(amount.text.toString())){
                Toast.makeText(requireContext(),"Please Provide proper details for all fields",Toast.LENGTH_LONG).show()
            }else {
                val studentDetails = StudentDetails(name.text.toString(),empEmail.text.toString(),mobileNo.text.toString(),year.text.toString(), amount.text.toString())
                homeViewModel.saveTaxDetails(MainActivity.userId,studentDetails)

            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}