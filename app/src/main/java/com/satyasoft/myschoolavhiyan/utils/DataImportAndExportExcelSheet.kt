package com.satyasoft.myschoolavhiyan.utils

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.database.StudentCollectionDetails
import com.satyasoft.myschoolavhiyan.utils.DataImportAndExportExcelSheet.Companion.cell
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DataImportAndExportExcelSheet {

    // Global Variables

    /**
     * Export Data into Excel Workbook
     *
     * @param context  - Pass the application context
     * @param fileName - Pass the desired fileName for the output excel Workbook
     * @param dataList - Contains the actual data to be displayed in excel
     */
    companion object {

        private var cell: Cell? = null
        private var sheet: Sheet? = null
        private const val EXCEL_SHEET_NAME = "Sheet1"
        private var workbook: Workbook?=null
        fun exportDataIntoExcelWorkbook(
            context: Context, fileName: String,
            dataList: List<StudentCollectionDetails>,
        ): Boolean {


            // Check if available and not read only
            if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                Log.e("TAG", "Storage not available or read only")
                return false
            }

            // Creating a New HSSF Workbook (.xls format)
            workbook = HSSFWorkbook()
            setHeaderCellStyle()

            // Creating a New Sheet and Setting width for each column
            sheet = workbook!!.createSheet(EXCEL_SHEET_NAME)
            sheet!!.setColumnWidth(0, 15 * 100)
            sheet!!.setColumnWidth(1, 15 * 400)
            sheet!!.setColumnWidth(2, 15 * 200)
            sheet!!.setColumnWidth(3, 15 * 200)
            sheet!!.setColumnWidth(4, 15 * 300)
            sheet!!.setColumnWidth(5, 15 * 300)
            sheet!!.setColumnWidth(6, 15 * 300)
            sheet!!.setColumnWidth(7, 15 * 400)
            sheet!!.setColumnWidth(8, 15 * 400)
            sheet!!.setColumnWidth(9, 15 * 400)
            sheet!!.setColumnWidth(10, 15 * 400)
            setHeaderRow(context)
            fillDataIntoExcel(dataList)
            return storeExcelInStorage(context, fileName)
        }

        /**
         * Checks if Storage is READ-ONLY
         *
         * @return boolean
         */
        private fun isExternalStorageReadOnly(): Boolean {
            val externalStorageState = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED_READ_ONLY == externalStorageState
        }
//https://medium.com/geekculture/creating-an-excel-in-android-cd9c22198619
        /**
         * Checks if Storage is Available
         *
         * @return boolean
         */
        private fun isExternalStorageAvailable(): Boolean {
            val externalStorageState = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == externalStorageState
        }

        /**
         * Setup header cell style
         */
        private fun setHeaderCellStyle() {

            val headerCellStyle: CellStyle? = workbook!!.createCellStyle()
            headerCellStyle?.fillForegroundColor = HSSFColor.YELLOW.index
            headerCellStyle?.fillPattern = HSSFCellStyle.SOLID_FOREGROUND
            headerCellStyle?.alignment = CellStyle.ALIGN_CENTER
        }

        /**
         * Setup Header Row
         */
        private fun setHeaderRow(context: Context) {
            // Generate column headings
            val headerCellStyle = workbook!!.createCellStyle()
            headerCellStyle.alignment = CellStyle.ALIGN_CENTER
            headerCellStyle?.fillForegroundColor = HSSFColor.YELLOW.index
            headerCellStyle?.fillPattern = HSSFCellStyle.SOLID_FOREGROUND
            val font = workbook!!.createFont()
            font.boldweight = Font.BOLDWEIGHT_BOLD
            headerCellStyle?.setFont(font)
            val row0: Row = sheet!!.createRow(0).apply {
                cell = createCell(1)
            }

            cell?.setCellValue(context.getString(R.string.mo_school_abhiyan))
            cell?.cellStyle = headerCellStyle
            cell = row0.createCell(2)
            val row1: Row = sheet!!.createRow(1).apply {
                cell = createCell(1)
            }
            cell?.setCellValue(context.getString(R.string.school_address))
            cell?.cellStyle = headerCellStyle
            cell = row1.createCell(2)

            val row: Row = sheet!!.createRow(4).apply {
                cell = createCell(0)
            }
            cell?.setCellValue("ID")
            cell?.cellStyle = headerCellStyle
            cell = row.createCell(1)
            cell?.setCellValue("NAME")
            cell?.cellStyle = headerCellStyle
            cell = row.createCell(2)
            cell?.setCellValue("BATCH")
            cell?.cellStyle = headerCellStyle
            cell = row.createCell(3)
            cell?.setCellValue("AMOUNT")
            cell?.cellStyle = headerCellStyle
            cell = row.createCell(4)
            cell?.setCellValue("DATE")
            cell?.cellStyle = headerCellStyle
            cell = row.createCell(5)
            cell?.setCellValue("Payment Method")
            cell?.cellStyle = headerCellStyle
            cell = row.createCell(6)
            cell?.setCellValue("Msg Received From")
            cell?.cellStyle = headerCellStyle
            cell = row.createCell(7)
            cell?.setCellValue("Account Status")
            cell?.cellStyle = headerCellStyle
            cell = row.createCell(8)
            cell?.setCellValue("Contact No")
            cell?.cellStyle = headerCellStyle
            cell = row.createCell(9)
            cell?.setCellValue("Email")
            cell?.cellStyle = headerCellStyle
            cell = row.createCell(10)
            cell?.setCellValue("Remarks")
            cell?.cellStyle = headerCellStyle
            cell = row.createCell(11)
        }

        /**
         * Fills Data into Excel Sheet
         *
         *
         * NOTE: Set row index as i+1 since 0th index belongs to header row
         *
         * @param dataList - List containing data to be filled into excel
         */
        private fun fillDataIntoExcel(dataList: List<StudentCollectionDetails>) {
            for (i in dataList.indices) {
                // Create a New Row for every new entry in list
                val rowData = sheet!!.createRow(i + 5)
                cell?.cellStyle?.verticalAlignment = CellStyle.ALIGN_JUSTIFY
                // Create Cells for each row
                cell = rowData.createCell(0)
                cell!!.setCellValue(dataList[i].id.toString())
                cell = rowData.createCell(1)
                cell!!.setCellValue(dataList[i].name)
                cell = rowData.createCell(2)
                cell!!.setCellValue(dataList[i].batch)
                cell = rowData.createCell(3)
                cell!!.setCellValue(dataList[i].amount)
                cell = rowData.createCell(4)
                cell!!.setCellValue(dataList[i].date)
                cell = rowData.createCell(5)
                cell!!.setCellValue(dataList[i].paymentMethod)
                cell = rowData.createCell(6)
                cell!!.setCellValue(dataList[i].msgReceivedFrom)
                cell = rowData.createCell(7)
                cell!!.setCellValue(dataList[i].accountStatus)
                cell = rowData.createCell(8)
                cell!!.setCellValue(dataList[i].contactNo)
                cell = rowData.createCell(9)
                cell!!.setCellValue(dataList[i].emailId)
                cell = rowData.createCell(10)
                cell!!.setCellValue(dataList[i].remarks)
            }
        }

        /**
         * Store Excel Workbook in external storage
         *
         * @param context  - application context
         * @param fileName - name of workbook which will be stored in device
         * @return boolean - returns state whether workbook is written into storage or not
         */
        private fun storeExcelInStorage(context: Context, fileName: String): Boolean {
            var isSuccess: Boolean
            val file: File =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString(), fileName)
            var fileOutputStream: FileOutputStream? = null
            try {
                fileOutputStream = FileOutputStream(file)
                workbook!!.write(fileOutputStream)
                Log.e("TAG", "Writing file$file")
                isSuccess = true
            } catch (e: IOException) {
                Log.e("TAG", "Error writing Exception: ", e)
                isSuccess = false
            } catch (e: Exception) {
                Log.e("TAG", "Failed to save file due to Exception: ", e)
                isSuccess = false
            } finally {
                try {
                    fileOutputStream?.close()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            return isSuccess
        }

        fun sharedBySocialMedia(context: Context) {
            val file =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString()  + "/"+"Mo School Abhiyan.xls")
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