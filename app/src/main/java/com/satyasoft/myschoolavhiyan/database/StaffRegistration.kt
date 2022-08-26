package com.satyasoft.myschoolavhiyan.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "StaffRegistrationRegister")
data class StaffRegistration(
    @PrimaryKey(autoGenerate = true) val id: Long =0,
    @ColumnInfo(name = "EmailId") var emailId: String?= "",
    @ColumnInfo(name = "Password") var password: String?= "",
)
