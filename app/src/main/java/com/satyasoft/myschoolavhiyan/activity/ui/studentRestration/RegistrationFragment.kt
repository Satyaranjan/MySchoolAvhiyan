package com.satyasoft.myschoolavhiyan.activity.ui.studentRestration

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.activity.MainActivity
import com.satyasoft.myschoolavhiyan.database.SchoolMasterDatabase
import com.satyasoft.myschoolavhiyan.database.StudentDetails
import com.satyasoft.myschoolavhiyan.databinding.FragmentHomeBinding
import com.satyasoft.myschoolavhiyan.utils.CustomDialogs
import com.satyasoft.myschoolavhiyan.utils.ResultOf


class RegistrationFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var studentDetails: StudentDetails
    private var selectedImageUri: Uri? = null
    private val binding get() = _binding!!
     private  val studentIdDetails: ArrayList<StudentDetails?>? = null
     @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
         inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: Bundle?,

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
                val studentIdList = mutableListOf<StudentDetails>()
                val getUserId = SchoolMasterDatabase.getSchoolMasterDataBase(requireContext()).studentRegistrationDAO().getAllStudentRecord()
                studentIdList.clear()
                var lastId : Int = 0
                if (getUserId.isNotEmpty()) {
                    studentIdList.addAll(getUserId)
                    lastId = studentIdList.last().id
                }
                val studentDetails = StudentDetails(lastId+1,name.text.toString(),empEmail.text.toString(),mobileNo.text.toString(),year.text.toString(), amount.text.toString())
                homeViewModel.saveTaxDetails(MainActivity.userId,studentDetails)
                activity?.let { it1 ->
                    SchoolMasterDatabase.getSchoolMasterDataBase(it1)
                        .studentRegistrationDAO().insertAllStudentRecord(
                            StudentDetails(0,
                                name.text.toString(),
                                empEmail.text.toString(),
                                mobileNo.text.toString(),
                                year.text.toString(),
                                amount.text.toString())
                        )
                }
                val recipient = empEmail.text.toString()
                val subject = "Mo School Abhiyan"
                val message = "Hi"+ " "+ name.text.toString() +" ,"+ "\n"+"\n" + "Thanks to support Mo School Abhiyan Program" +  "\n"+"\n" +
                        "Regards" +","+ "\n"+"\n" + "H.M. PBBP, Shibapura"

                //method call for email intent with these inputs as parameters
                sendEmail(recipient, subject, message)
                sendSMS(mobileNo.text.toString(),message)

            }
        }

        return root
    }
    @SuppressLint("IntentReset")
    private fun sendEmail(recipient: String, subject: String, message: String) {

        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, message)
        try {
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
        }
    }
    private fun sendSMS(phoneNo: String?, msg: String?) {

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val smsMgrVar = SmsManager.getDefault()
                smsMgrVar.sendTextMessage(phoneNo, null, msg, null, null)
                Toast.makeText(requireContext(), "Message Sent",
                    Toast.LENGTH_LONG).show()
            } catch (ErrVar: Exception) {
                Toast.makeText(requireContext(),
                    ErrVar.message.toString(),
                    Toast.LENGTH_LONG).show()
                ErrVar.printStackTrace()
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.SEND_SMS), 10)
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}