package com.yashendra.margapp_2.Activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.yashendra.margapp_2.R

class SplashActivity : AppCompatActivity() {
    private lateinit var circularBackground: ImageView
    private lateinit var floatingButton: FloatingActionButton
    private lateinit var full:RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        circularBackground = findViewById(R.id.circular_background)

        floatingButton = findViewById(R.id.floatingbutton)
        full=findViewById(R.id.full)

        // Set a click listener on the floating button
        floatingButton.setOnClickListener {
            // Start the background color change animation
            startColorChangeAnimation()
        }
    }

    private fun startColorChangeAnimation() {
        // Define the start and end colors
        val startColor = resources.getColor(R.color.grad3)
        val endColor = resources.getColor(R.color.grad1)

        // Create an ObjectAnimator to change the background color
        val colorAnimator = ObjectAnimator.ofObject(
            full,
            "backgroundColor",
            ArgbEvaluator(),
            startColor,
            endColor
        )

        colorAnimator.duration = 1000 // Animation duration in milliseconds
        colorAnimator.interpolator = AccelerateDecelerateInterpolator()

        // Set an animation listener to start the new activity after the animation ends
        colorAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // Animation ended, start the new activity
                val intent=Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish() // Close the splash screen activity
            }
        })

        // Start the color change animation
        colorAnimator.start()
    }
}