package com.arodi.surveyapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arodi.surveyapp.dao.AnswerDao
import com.arodi.surveyapp.dao.QuestionDao
import com.arodi.surveyapp.entities.AnswerEntity
import com.arodi.surveyapp.entities.QuestionEntity

@Database(entities = [QuestionEntity::class, AnswerEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun answerDao(): AnswerDao
}