package com.satyasoft.myschoolavhiyan.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.database.StudentCollectionDetails
import com.satyasoft.myschoolavhiyan.interfaces.ClickListener
import java.util.*


open class CustomAdapter(private var context: Context, private var studentList: MutableList<StudentCollectionDetails>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>(){
    lateinit var clickListener: ClickListener
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_list_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val studentDetails = studentList[position]
        holder.name.text = studentDetails.name
        holder.year.text = studentDetails.batch
        holder.mobile.text = studentDetails.contactNo
        holder.amount.text = studentDetails.amount
        holder.date.text = studentDetails.date
        holder.paymentMode.text = studentDetails.paymentMethod
        holder.paymentStatus.text = studentDetails.accountStatus

        holder.phoneCall.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:".plus(studentDetails.contactNo))
            context.startActivity(intent)
        }

        holder.whatsAppCall.setOnClickListener{
            val installed: Boolean = appInstalledOrNot("com.whatsapp")
            val message = "Hi I need your support to implement our school website. Can you all join the coming sunday meeting."
            if (!installed) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data =
                    Uri.parse("http://api.whatsapp.com/send?phone=+91${studentDetails.contactNo}&text=$message")
                context.startActivity(intent)
            } else {
                Toast.makeText(
                    context,
                    "Whats app not installed on your device",
                    Toast.LENGTH_SHORT
                ).show()
            }
               }
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterList: ArrayList<StudentCollectionDetails>) {
        studentList = filterList
        notifyDataSetChanged()
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val name: TextView = itemView.findViewById(R.id.name)
        val year: TextView = itemView.findViewById(R.id.year)
        val mobile: TextView = itemView.findViewById(R.id.mobileNo)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val date: TextView = itemView.findViewById(R.id.date)
        val paymentMode: TextView = itemView.findViewById(R.id.paymentMode)
        val paymentStatus: TextView = itemView.findViewById(R.id.paymentStatus)
        val phoneCall : TextView = itemView.findViewById(R.id.callMe)
        val whatsAppCall : TextView = itemView.findViewById(R.id.whatsApp)

    }

    open fun appInstalledOrNot(url: String): Boolean {
        val packageManager: PackageManager = context.getPackageManager()
        val app_installed: Boolean
        app_installed = try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return app_installed
    }
}


