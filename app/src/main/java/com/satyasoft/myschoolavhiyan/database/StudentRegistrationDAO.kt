package com.satyasoft.myschoolavhiyan.database

import androidx.room.*
@Dao
interface StudentRegistrationDAO {
    @Query("SELECT * FROM `StudentDetailRegister`")
    fun getAllStudentRecord(): MutableList<StudentDetails>

//    @Query("SELECT * FROM StudentDetailRegister WHERE `YearOfPass` LIKE :YearOfPass")
//    fun findEachStudentRecord(YearOfPass: String): StudentDetails
//
//    @Query("SELECT YearOfPass, SUM(amount)  AS amount  FROM StudentDetailRegister  GROUP BY  YearOfPass")
//    fun getAccountDetailYearWise() : MutableList<AccountDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStudentRecord(vararg studentDetails: StudentDetails)

    @Delete
    fun deleteStudentRecord(studentDetails: StudentDetails)

    @Update
    fun updatedStudentRegistrationTable(vararg studentDetails: StudentDetails)

}