package com.yashendra.margapp_2.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.yashendra.margapp_2.Adapters.QuestionAdapter
import com.yashendra.margapp_2.Model.Question
import com.yashendra.margapp_2.Model.Quiz
import com.yashendra.margapp_2.R
import com.yashendra.margapp_2.databinding.ActivityMaintalAbilityTestBinding
import com.yashendra.margapp_2.databinding.ActivityTestBinding

class MaintalAbilityTest : AppCompatActivity() {
    private lateinit var binding: ActivityMaintalAbilityTestBinding
    lateinit var TestAdapter: QuestionAdapter
    lateinit var firestore: FirebaseFirestore
    var quizzes: MutableList<Quiz>? = null
    var questions: MutableMap<String, Question>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMaintalAbilityTestBinding.inflate(layoutInflater)
        val view=binding.root

        setContentView(view)

        val score = intent.getIntExtra("score", 0)
        val classes=intent.getStringExtra("class")
        setupFirestore()
        binding.submitButton.setOnClickListener {
            val intent=android.content.Intent(this,ResultActivity::class.java)
            intent.putExtra("questions",questions.toString())
            intent.putExtra("score", score)
            intent.putExtra("class",classes)
            startActivity(intent)
            finish()
        }
    }

    private fun setupFirestore() {
        val firestore= FirebaseFirestore.getInstance()
        firestore.collection("Quizzes").whereEqualTo("title","mentalAbility")
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
