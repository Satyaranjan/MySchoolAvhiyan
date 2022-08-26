package com.satyasoft.myschoolavhiyan.pdfService

import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import com.itextpdf.awt.geom.misc.Messages.getString
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.satyasoft.myschoolavhiyan.R
import com.satyasoft.myschoolavhiyan.database.StudentDetails
import java.io.File
import java.io.FileOutputStream


class PdfService {
    val TITLE_FONT = Font(Font.FontFamily.TIMES_ROMAN, 16f, Font.BOLD)
    val BODY_FONT = Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.NORMAL)
    private lateinit var pdf: PdfWriter

    private fun createFile(): File {
        //Prepare file
        val title = "MoSchoolAbhiyan.pdf"
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, title)
        if (!file.exists()) file.createNewFile()
        return file
    }

    private fun createDocument(): Document {
        //Create Document
        val document = Document()
        document.setMargins(24f, 24f, 32f, 32f)
        document.pageSize = PageSize.A4
        return document
    }

    private fun setupPdfWriter(document: Document, file: File) {
        pdf = PdfWriter.getInstance(document, FileOutputStream(file))
        pdf.setFullCompression()
        //Open the document
        document.open()
    }

    private fun createTable(column: Int, columnWidth: FloatArray): PdfPTable {
        val table = PdfPTable(column)
        table.widthPercentage = 100f
        table.setWidths(columnWidth)
        table.headerRows = 1
        table.defaultCell.verticalAlignment = Element.ALIGN_CENTER
        table.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
        return table
    }

    private fun createCell(content: String): PdfPCell {
        val cell = PdfPCell(Phrase(content))
        cell.horizontalAlignment = Element.ALIGN_CENTER
        cell.verticalAlignment = Element.ALIGN_MIDDLE
        //setup padding
        cell.setPadding(8f)
        cell.isUseAscender = true
        cell.paddingLeft = 4f
        cell.paddingRight = 4f
        cell.paddingTop = 8f
        cell.paddingBottom = 8f
        return cell
    }

    private fun addLineSpace(document: Document, number: Int) {
        for (i in 0 until number) {
            document.add(Paragraph(" "))
        }
    }

    private fun createParagraph(content: String): Paragraph{
        val paragraph = Paragraph(content, BODY_FONT)
        paragraph.firstLineIndent = 25f
        paragraph.alignment = Element.ALIGN_LEFT
        return paragraph
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun createUserTable(
        data: MutableList<StudentDetails>,
        paragraphList: List<String>,
        onFinish: (file: File) -> Unit,
        onError: (Exception) -> Unit
    ) {
        //Define the document
        val file = createFile()
        val document = createDocument()

        //Setup PDF Writer
        setupPdfWriter(document, file)

        //Add Title
        document.add(Paragraph("Mo School Abhiyan", TITLE_FONT))
        //Add Empty Line as necessary
        addLineSpace(document, 1)

        //Add paragraph
        paragraphList.forEach {content->
            val paragraph = createParagraph(content)
            document.add(paragraph)
        }
        addLineSpace(document, 1)
        //Add Empty Line as necessary

        //Add table title
        document.add(Paragraph("Student Details", TITLE_FONT))
        addLineSpace(document, 1)

        //Define Table
        val userIdWidth = 0.5f
        val nameWidth = 2f
        val emailWidth = 2f
        val phoneWidth = 1f
        val yearWidth = .75f
        val amountWidth = .75f
        val columnWidth = floatArrayOf(userIdWidth, nameWidth, emailWidth, phoneWidth,yearWidth,amountWidth)
        val table = createTable(6, columnWidth)
        //Table header (first row)
        val tableHeaderContent = listOf("No", "Name", "Email Id", "Phone No","Passing Yr.","Amount")
        //write table header into table
        tableHeaderContent.forEach {
            //define a cell
            val cell = createCell(it)
            //add our cell into our table
            table.addCell(cell)
        }
        //write user data into table
        data.forEach {
            val idCell = createCell(it.id.toString())
            table.addCell(idCell)

            val firstNameCell = it.name?.let { it1 -> createCell(it1) }
            firstNameCell?.horizontalAlignment = Element.ALIGN_LEFT
            table.addCell(firstNameCell)

            val secondNameCell = it.emailId?.let { it2 -> createCell(it2) }
            secondNameCell?.horizontalAlignment = Element.ALIGN_LEFT
            table.addCell(secondNameCell)

            val thirdNameCell = it.phoneNumber?.let { it3 -> createCell(it3) }
            table.addCell(thirdNameCell)

            val fourthNameCell = it.yearOfPass?.let { it4 -> createCell(it4) }
            table.addCell(fourthNameCell)

            val fifthNameCell = it.amount?.let { it5 -> createCell(it5) }
            table.addCell(fifthNameCell)
        }
        document.add(table)
        document.close()

        try {
            pdf.close()
        } catch (ex: Exception) {
            onError(ex)
        } finally {
            onFinish(file)
        }
    }
}