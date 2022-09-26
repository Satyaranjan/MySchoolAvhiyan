package com.satyasoft.myschoolavhiyan.utils

import com.satyasoft.myschoolavhiyan.model.StudentCollectionDetailsData

object ValidationUtil {

    fun validateMovie(movie: StudentCollectionDetailsData) : Boolean {
        if (movie.name!!.isNotEmpty() && movie.date!!.isNotEmpty()) {
            return true
        }
        return false
    }
}