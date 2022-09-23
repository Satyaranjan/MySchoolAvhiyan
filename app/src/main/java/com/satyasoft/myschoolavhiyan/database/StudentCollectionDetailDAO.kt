package com.satyasoft.myschoolavhiyan.database

import androidx.room.*
import com.satyasoft.myschoolavhiyan.database.StudentCollectionDetails as StudentCollectionDetails

@Dao
interface StudentCollectionDetailDAO {
    @Query("SELECT * FROM `StudentCollectionDetail`")
    fun getAllStudentCollectionRecord(): MutableList<StudentCollectionDetails>

    @Query("SELECT * FROM StudentCollectionDetail WHERE `BATCH` LIKE:BATCH ")
    fun findEachStudentRecord(BATCH: String): StudentCollectionDetails

    @Query("SELECT BATCH, SUM(AMOUNT)  AS AMOUNT  FROM StudentCollectionDetail  GROUP BY  BATCH")
    fun getAccountDetailYearWise() : MutableList<AccountDetails>

    @Query("SELECT id, SUM(AMOUNT) AS AMOUNT FROM StudentCollectionDetail")

    fun getTotalAmount() :  MutableList<StudentCollectionDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStudentCollectionRecord(vararg studentCollectionDetails: StudentCollectionDetails)

    @Delete
    fun deleteStudentRecord(studentCollectionDetails: StudentCollectionDetails)

    @Update
    fun updatedStudentRegistrationTable(vararg studentCollectionDetails: StudentCollectionDetails)

}