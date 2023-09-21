package com.yashendra.margapp_2.Model

data class Quiz(
    var id:String="",
    var title:String="",
    var questions:MutableMap<String,Question> = mutableMapOf()
)

