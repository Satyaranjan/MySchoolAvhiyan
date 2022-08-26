package com.satyasoft.myschoolavhiyan.database

import androidx.room.*
@Dao
interface StudentRegistrationDAO {
    @Query("SELECT * FROM `StudentDetailRegister`")
    fun getAllStudentRecord(): MutableList<StudentDetails>

    @Query("SELECT * FROM StudentDetailRegister WHERE `YearOfPass` LIKE :YearOfPass")
    fun findEachStudentRecord(YearOfPass: String): StudentDetails


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStudentRecord(vararg studentDetails: StudentDetails)

    @Delete
    fun deleteStudentRecord(studentDetails: StudentDetails)

    @Update
    fun updatedStudentRegistrationTable(vararg studentDetails: StudentDetails)

}