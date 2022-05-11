package com.arodi.surveyapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestionEntity(
   @PrimaryKey(autoGenerate = true)
   var id: Long?,
   var questionModel: String?
)