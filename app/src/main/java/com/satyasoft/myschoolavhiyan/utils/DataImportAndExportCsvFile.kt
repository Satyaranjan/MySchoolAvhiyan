package com.satyasoft.myschoolavhiyan.utils

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.database.SchoolMasterDatabase
import com.satyasoft.myschoolavhiyan.database.StudentCollectionDetails
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

class DataImportAndExportCsvFile {
    companion object {
        private const val CSV_FILE_NAME = "Mo School Abhiyan.csv"
        fun exportDataInToCSV(
            studentInfoList: MutableList<StudentCollectionDetails>,
            context: Context
        ) {
            if (studentInfoList.isNotEmpty()) {
                val exportDir =
                    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .toString(), "/CSV")// your path where you want save your file
                if (!exportDir.exists()) {
                    exportDir.mkdirs()
                }

                try {

                    val file = File(exportDir, CSV_FILE_NAME)
                    file.createNewFile()
                    val writer = CSVWriter(FileWriter(file))
                    val messageTitle = arrayOf(context.getString(R.string.title_csv))
                    writer.writeNext(messageTitle)
                    val schoolDetails = arrayOf(context.getString(R.string.school_address))
                    writer.writeNext(schoolDetails)

                    val header = arrayOf("Id",
                        "NAME",
                        "BATCH",
                        "AMOUNT",
                        "DATE",
                        "Payment Method",
                        "Msg Received From",
                        "Account Status",
                        "Contact No",
                        "Email",
                        "Remarks")
                    writer.writeNext(header)

                    val data: MutableList<Array<String?>> = ArrayList()
                    for (index in 0 until studentInfoList.size) {
                        arrayOf(studentInfoList[index].id.toString(),
                            studentInfoList[index].name,
                            studentInfoList[index].batch,
                            studentInfoList[index].amount,
                            studentInfoList[index].date,
                            studentInfoList[index].paymentMethod,
                            studentInfoList[index].msgReceivedFrom,
                            studentInfoList[index].accountStatus,
                            studentInfoList[index].contactNo,
                            studentInfoList[index].emailId,
                            studentInfoList[index].remarks).let {
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


        fun importStudentCollectionFromCSV(context: Context) {
            try {

                val file =
                    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .toString() + "/" + "/CSV/CSV_FILE_NAME")
                if (file.exists()) {

                    val fileReader = FileReader(file)
                    val reader = CSVReader(fileReader)
                    reader.skip(3)
                    var record: Array<String>?
                    var mIds: String
                    var mNames: String
                    var mBatchs: String
                    var mAmounts: String
                    var mDate: String
                    var mPaymentMethod: String
                    var mMsgReceivedFrom: String
                    var mAccountStatus: String
                    var mContactNo: String
                    var mEmail: String
                    var mRemarks: String
                    while (reader.readNext().also { record = it } != null) {
                        mIds = record!![0]
                        mNames = record!![1]
                        mBatchs = record!![2]
                        mAmounts = record!![3]
                        mDate = record!![4]
                        mPaymentMethod = record!![5]
                        mMsgReceivedFrom = record!![6]
                        mAccountStatus = record!![7]
                        mContactNo = record!![8]
                        mEmail = record!![9]
                        mRemarks = record!![10]
                        context.let { it2 ->
                            SchoolMasterDatabase.getSchoolMasterDataBase(it2)
                                .studentCollectionRegistrationDAO()
                                .insertAllStudentCollectionRecord(
                                    StudentCollectionDetails(
                                        mIds.toInt(),
                                        mNames,
                                        mBatchs,
                                        mAmounts,
                                        mDate,
                                        mPaymentMethod,
                                        mMsgReceivedFrom,
                                        mAccountStatus,
                                        mContactNo,
                                        mEmail,
                                        mRemarks
                                    )
                                )
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


        fun sharedBySocialMedia(context: Context) {
            val file =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/" + "/CSV/Mo School Abhiyan.csv")
            if (file.exists()) {
                val uri = FileProvider.getUriForFile(context,
                    "com.satyasoft.myschoolavhiyan" + ".provider", file)
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.setDataAndType(uri, "application/pdf")
                shareIntent.setDataAndType(uri, "text/html")
                shareIntent.setDataAndType(uri, "message/rfc822")
                shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("satya.igu@gmail.com"))
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.title_csv))
                shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.gmail_message))
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                shareIntent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.startActivity(shareIntent)
            }
        }
    }

}