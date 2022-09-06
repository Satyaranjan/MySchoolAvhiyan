package com.satyasoft.myschoolavhiyan.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.interfaces.ClickListener
import java.util.*
import com.satyasoft.myschoolavhiyan.database.StudentDetails as StudentDetails


open class CustomAdapter(private var studentList: MutableList<StudentDetails>) :
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
        holder.year.text = studentDetails.yearOfPass
        holder.mobile.text = studentDetails.phoneNumber
        holder.amount.text = studentDetails.amount
        holder.email.text = studentDetails.emailId

    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterList: ArrayList<StudentDetails>) {
        studentList = filterList
        notifyDataSetChanged()
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val name: TextView = itemView.findViewById(R.id.name)
        val year: TextView = itemView.findViewById(R.id.year)
        val mobile: TextView = itemView.findViewById(R.id.mobileNo)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val email: TextView = itemView.findViewById(R.id.email)

    }
}


