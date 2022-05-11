package com.arodi.surveyapp.dao

import androidx.room.*
import com.arodi.surveyapp.entities.QuestionEntity


@Dao
interface  QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(questionEntity: QuestionEntity): Long

    @Query("SELECT * FROM QuestionEntity")
    fun fetchAll(): QuestionEntity

    @Query("DELETE FROM QuestionEntity")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM QuestionEntity)")
    fun isExists(): Boolean

    @Update
    fun update(questionEntity: QuestionEntity): Int
}