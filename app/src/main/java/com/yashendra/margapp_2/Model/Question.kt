package com.yashendra.margapp_2.Model


data class Question(

    val description: String="",
    val option1: String="",
    val option2: String="",
    val option3: String="",
    val option4: String="",
    val answer: String="",
    var userAnswer: String=""
)
