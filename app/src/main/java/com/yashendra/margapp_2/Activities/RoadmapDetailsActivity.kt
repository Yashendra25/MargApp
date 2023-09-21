package com.yashendra.margapp_2.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.yashendra.margapp_2.R

class RoadmapDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roadmap_details)
        // Find the TextViews for Academics, Skills, and Exploration
        val academicsDescription = findViewById<TextView>(R.id.academicsDescription)
        val skillsDescription = findViewById<TextView>(R.id.skillsDescription)
        val explorationDescription = findViewById<TextView>(R.id.explorationDescription)
        val collapsingToolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        val imageView = findViewById<ImageView>(R.id.roadmapimage)
        val linearProgressIndicator = findViewById<LinearProgressIndicator>(R.id.progressbar_linear)

        linearProgressIndicator.visibility=android.view.View.VISIBLE
        val data=intent.getStringExtra("itemName")
        collapsingToolbarLayout.title=data.toString()
        imageView.setImageResource(R.drawable.engineer)
        // Sample text containing sections
        val text = """
            Academics
            Focus on your math and science subjects, as these are the foundation for engineering.
            Take advanced math and science courses, if available.
            Get good grades in all of your subjects, not just math and science.
            Participate in extracurricular activities related to engineering, such as math club, science club, or robotics club.
            Attend engineering summer camps or workshops.

            Skills
            Develop your problem-solving skills.
            Learn to code. There are many resources available online and in libraries to learn coding languages such as Python, C++, and Java.
            Practice your math and science skills by working on practice problems and taking practice tests.
            Improve your communication and teamwork skills.

            Exploration
            Research different engineering disciplines to learn more about their different specialties and career paths.
            Talk to engineers about their work and how they got into engineering.
            Practice your math and science skills by working on practice problems and taking practice tests.
            Visit engineering schools and companies to get a feel for what it's like to study and work in the field.
        """.trimIndent()

        // Split the text into sections
        val sections = text.split("\n\n")

        // Check if there are enough sections (Academics, Skills, and Exploration)
        if (sections.size >= 3) {
            academicsDescription.text = sections[0]
            skillsDescription.text = sections[1]
            explorationDescription.text = sections[2]
        }
        linearProgressIndicator.visibility=android.view.View.GONE
    }
}
