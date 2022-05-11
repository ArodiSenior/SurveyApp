package com.arodi.surveyapp.dao

import androidx.room.*
import com.arodi.surveyapp.entities.AnswerEntity
import com.arodi.surveyapp.entities.QuestionEntity


@Dao
interface  AnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(answerEntity: AnswerEntity): Long

    @Query("SELECT * FROM AnswerEntity")
    fun fetchAll(): List<AnswerEntity>

    @Query("DELETE FROM AnswerEntity")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM AnswerEntity)")
    fun isExists(): Boolean

    @Update
    fun update(answerEntity: AnswerEntity): Int
}