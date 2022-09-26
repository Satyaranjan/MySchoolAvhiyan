package com.satyasoft.myschoolavhiyan.activity.ui.graphDetails

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.database.AccountDetails
import com.satyasoft.myschoolavhiyan.database.SchoolMasterDatabase
import com.satyasoft.myschoolavhiyan.database.StudentCollectionDetails


class StudentDetailsInGraph : Fragment() {

    private var years = mutableListOf<String>()
    var amount = mutableListOf<Float>()
    private val studentInfoList = mutableListOf<StudentCollectionDetails>()
    private val yearWiseCollection = mutableListOf<AccountDetails>()
    private var barData: BarData? =null
    lateinit var barDataSet: BarDataSet
    private lateinit var barChart: BarChart
    private lateinit var totalAmount :TextView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

       val view : View = inflater.inflate(R.layout.fragment_graph, container, false)

        barChart = view.findViewById(R.id.barChart)
        totalAmount = view.findViewById(R.id.totalAmount)

        val sumTotalAmount = SchoolMasterDatabase.getSchoolMasterDataBase(requireContext())
           .studentCollectionRegistrationDAO().getTotalAmount()
       val totalAccount : String = getString(R.string.total_amount)+ sumTotalAmount.get(0).amount.toString()
       totalAmount.text = totalAccount

        val getUserId = SchoolMasterDatabase.getSchoolMasterDataBase(requireContext())
            .studentCollectionRegistrationDAO().getAllStudentCollectionRecord()

        studentInfoList.clear()
        if (getUserId.isNotEmpty()) {
            studentInfoList.addAll(getUserId)
        }

        val accountDetails = SchoolMasterDatabase.getSchoolMasterDataBase(requireContext())
            .studentCollectionRegistrationDAO().getAccountDetailYearWise()

       if(accountDetails.isNotEmpty()){
            yearWiseCollection.addAll(accountDetails)
        }

        for (items in yearWiseCollection.indices){
            yearWiseCollection[items].BATCH?.let { years.add(it.toString()) }
            yearWiseCollection[items].AMOUNT?.let { amount.add(it.toFloat()) }
       }

        populateBarChart()
        return view
    }

    private fun populateBarChart() {
        //adding values
        val ourBarEntries: ArrayList<BarEntry> = ArrayList()

        for ((i, _) in yearWiseCollection.withIndex()) {
            val value = yearWiseCollection[i].AMOUNT?.toFloat()
            value?.let { BarEntry(i.toFloat(), it) }?.let { ourBarEntries.add(it) }
        }


        val barDataSet = BarDataSet(ourBarEntries, "Mo School Abhiyan")
        //set a template coloring
       // barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
       // barDataSet.color = Color.WHITE;
        barDataSet.color = Color.rgb(60, 220, 78)
        barDataSet.valueTextColor = Color.rgb(100, 210, 90)
        barDataSet.valueTextSize = 14f;
        val data = BarData(barDataSet)
        barChart.data = data
        //setting the x-axis
        val xAxis: XAxis = barChart.xAxis

        xAxis.valueFormatter = IndexAxisValueFormatter(years)
        xAxis.setCenterAxisLabels(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.setDrawGridLines(true)
        // xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        barChart.isDragEnabled = true
        xAxis.textSize = 16f;
        barChart.setVisibleXRangeMaximum(5f)
        barChart.legend.isEnabled = true
        barData?.barWidth ?: 0.10f
      //  barChart.xAxis.axisMinimum = 1f
        barChart.animateY(5000)
        barChart.description.isEnabled = false
        val rightYAxis = barChart.axisRight
        rightYAxis.isEnabled = false
        barChart.axisLeft.textSize = 16f
        barChart.axisLeft.textColor = resources.getColor(R.color.white)
        barChart.xAxis.textColor = resources.getColor(R.color.white)
        barChart.legend.textColor = resources.getColor(R.color.white)
        barChart.description.textColor = resources.getColor(R.color.white)
        barChart.invalidate()
        //https://www.section.io/engineering-education/android-kotlin-mpandroidcharts-sqlite/
    }

}