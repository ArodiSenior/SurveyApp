package com.arodi.surveyapp.models

data class QuestionModel (
  var id: String?,
  var start_question_id : String?,
  var questions: ArrayList<Questions>?,
  var strings: Strings?
)