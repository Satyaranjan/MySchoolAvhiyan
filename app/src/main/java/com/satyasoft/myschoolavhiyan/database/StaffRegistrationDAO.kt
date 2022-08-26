package com.satyasoft.myschoolavhiyan.database

import androidx.room.*


@Dao
interface StaffRegistrationDAO {

    @Query("SELECT * FROM `StaffRegistrationRegister`")
    fun getAll(): List<StaffRegistration>

    @Query("SELECT * FROM StaffRegistrationRegister WHERE `EmailId` LIKE  :emailId  AND `Password` LIKE :password ")
    fun findByUserID(emailId: String,password:String): StaffRegistration

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg staffRegistration: StaffRegistration)

    @Delete
    fun delete(staffRegistration: StaffRegistration)

    @Update
    fun updatedRRegistrationTable(vararg staffRegistration: StaffRegistration)

}