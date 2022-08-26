package com.satyasoft.myschoolavhiyan.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StudentDetailRegister")
data class StudentDetails(
    @PrimaryKey(autoGenerate = true) val id: Int =0,
    @ColumnInfo(name = "Name") var name: String? = "",
    @ColumnInfo(name = "Email Id") var emailId: String? = "",
    @ColumnInfo(name = "Phone Number") var phoneNumber: String? = "",
    @ColumnInfo(name = "YearOfPass") var yearOfPass: String? = "",
    @ColumnInfo(name = "amount") var amount: String? = "",
)

