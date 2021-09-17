package com.example.templatebaze.dao

import com.example.templatebaze.model.Student

interface DAO {
    fun insert( s : Student ) : Boolean
    fun update( s : Student ) : Boolean
    fun delete( s : Student ) : Boolean
    fun getAll( )             : List<Student>
}