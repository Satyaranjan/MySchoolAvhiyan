package com.satyasoft.myschoolavhiyan.activity.ui.slideshow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.adapter.StudentDetails
import com.satyasoft.myschoolavhiyan.interfaces.ClickListener

class CustomAdapter(private var mList: List<StudentDetails?>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    lateinit var clickListener: ClickListener
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_list_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val studentDetails = mList[position]

        // sets the text to the textview from our itemHolder class
        if (studentDetails != null) {
            holder.name.text = studentDetails.name
            holder.year.text = studentDetails.year
            holder.mobile.text = studentDetails.mobileNo
            holder.amount.text = studentDetails.amount
            holder.email.text = studentDetails.emailId


        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }
    //Sets Data
    fun setData(studentInfoList:  MutableList<StudentDetails>){
        this.mList = studentInfoList

    }

//    fun setInterface(clickListener: FragmentActivity?){
//        this.clickListener = clickListener
//    }
    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val name: TextView = itemView.findViewById(R.id.name)

        val year: TextView = itemView.findViewById(R.id.year)
        val mobile: TextView = itemView.findViewById(R.id.mobileNo)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val email: TextView = itemView.findViewById(R.id.email)

    }
}


