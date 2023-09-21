package com.yashendra.margapp_2.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yashendra.margapp_2.R
import com.yashendra.margapp_2.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Retrieve the user's score from the intent
        val score = intent.getIntExtra("score", 0)
        val classes=intent.getStringExtra("class")
        val values=intent.getStringExtra("questions")
        if (values != null) {
            Log.d("values",values)
        }
        // Display the score in a TextView
        binding.scoreTextView.text = "Your Score: $score"

        binding.proceedButton.setOnClickListener {
            val intent=Intent(this,InterestActivity::class.java)
            intent.putExtra("score",score)
            intent.putExtra("class",classes)
            startActivity(intent)
        }
    }
}