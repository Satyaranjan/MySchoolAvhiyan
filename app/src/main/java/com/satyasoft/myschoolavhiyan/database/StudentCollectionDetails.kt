package com.satyasoft.myschoolavhiyan.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StudentCollectionDetail")
data class StudentCollectionDetails(
    @PrimaryKey(autoGenerate = true) var id: Int =0,
    @ColumnInfo(name = "NAME") var name: String? = "",
    @ColumnInfo(name = "BATCH") var batch: String? = "",
    @ColumnInfo(name = "AMOUNT") var amount: String? = "",
    @ColumnInfo(name = "DATE") var date: String? = "",
    @ColumnInfo(name = "Payment Method") var paymentMethod: String? = "",
    @ColumnInfo(name = "Msg Received From") var msgReceivedFrom: String? = "",
    @ColumnInfo(name = "Account Status") var accountStatus: String? = "",
    @ColumnInfo(name = "Contact No") var contactNo: String? = "",
    @ColumnInfo(name = "Remark") var remark: String? = "",
    @ColumnInfo(name = "Extra") var extra: String? = "",
)
