package com.arodi.surveyapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AnswerEntity(
   @PrimaryKey(autoGenerate = true)
   var id: Long?,
   var name: String?,
   var name_id: String?,
   var gender: String?,
   var gender_id: String?,
   var size: String?,
   var size_id: String?,
   var photo: String?
)