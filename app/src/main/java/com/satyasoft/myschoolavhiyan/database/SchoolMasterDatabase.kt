package com.satyasoft.myschoolavhiyan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database( entities = [StaffRegistration::class,StudentDetails::class], version = 1,exportSchema = true)
abstract class SchoolMasterDatabase : RoomDatabase(){
    abstract fun staffRegistrationDAO() : StaffRegistrationDAO

    abstract fun studentRegistrationDAO() :StudentRegistrationDAO

    companion object {
        @Volatile private var instance: SchoolMasterDatabase? = null
        private val LOCK = Any()

        fun getSchoolMasterDataBase(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            SchoolMasterDatabase::class.java, "SchoolMasterDatabase.db").allowMainThreadQueries()
            .build()
    }
}