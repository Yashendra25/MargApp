package com.yashendra.margapp_2.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.yashendra.margapp_2.Adapters.QuestionAdapter
import com.yashendra.margapp_2.Model.Question
import com.yashendra.margapp_2.Model.Quiz
import com.yashendra.margapp_2.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    lateinit var TestAdapter: QuestionAdapter
    lateinit var firestore: FirebaseFirestore
    var quizzes: MutableList<Quiz>? = null
    var questions: MutableMap<String,Question>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTestBinding.inflate(layoutInflater)
        val view=binding.root

        setContentView(view)

        val classValue = intent.getStringExtra("class")

        // Now, you can use the "classValue" as needed in your TestActivity
        if (classValue != null) {
            // Do something with the classValue
            setupFirestore(classValue)
        }
        // Set a click listener for the submit button
        binding.submitButton.setOnClickListener {
            // Calculate the user's score
            val score = calculateScore()

            // Navigate to the result activity and pass the score
//            val intent = Intent(this, ResultActivity::class.java)
//            intent.putExtra("score", score)
//            intent.putExtra("class",classValue)
//            startActivity(intent)
//            finish() // Finish the current activity to prevent going back to it
            val intent = Intent(this, MaintalAbilityTest::class.java)
            intent.putExtra("score", score)
            intent.putExtra("class",classValue)
            startActivity(intent)
            finish() // Finish the current activity to prevent going back to it
        }

    }

    private fun calculateScore(): Int {
        // Iterate through the questions and compare user's answers with correct answers
        var correctAnswers = 0
        questions?.forEach { (_, question) ->
            if (question.userAnswer == question.answer) {
                correctAnswers++
            }
        }
        Log.d("TestActivity", questions.toString())
        return correctAnswers
    }



    private fun setupFirestore(classValue: String) {

            val firestore= FirebaseFirestore.getInstance()
            firestore.collection("Quizzes").whereEqualTo("title",classValue)
                .get()
                .addOnSuccessListener {
                    if (it!=null && !it.isEmpty){
                        quizzes=it.toObjects(Quiz::class.java)
                        questions=quizzes!![0].questions

                        TestAdapter= QuestionAdapter(this,questions)
                        binding.questionRecyclerView.layoutManager= LinearLayoutManager(this)
                        binding.questionRecyclerView.adapter=TestAdapter
                    }
                }
        }
}