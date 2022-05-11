package com.arodi.surveyapp.models

data class Questions(
    var id: String?,
    var question_type: String?,
    var answer_type: String?,
    var question_text: String?,
    var options: ArrayList<Options>?,
    var next: String?

)