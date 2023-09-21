package com.yashendra.margapp_2.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yashendra.margapp_2.Adapters.RoadmapAdapter
import com.yashendra.margapp_2.Adapters.SubjectAdapter
import com.yashendra.margapp_2.Model.RoadmapItem
import com.yashendra.margapp_2.Model.SubjectItem
import com.yashendra.margapp_2.R

class Dashboard : AppCompatActivity() {
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private val subjectItemList = listOf(
        SubjectItem(R.drawable.artandcraft, "Art and Craft"),
        SubjectItem(R.drawable.hindi, "Hindi"),
        SubjectItem(R.drawable.biology, "Biology"),
        SubjectItem(R.drawable.chemistry, "Chemistry"),
        SubjectItem(R.drawable.english, "English"),
        SubjectItem(R.drawable.maths, "Maths"),
        // Add more items as needed
    )

    private val roadmapItemList = listOf(
        RoadmapItem(R.drawable.engineer, "Engineering"),
        RoadmapItem(R.drawable.doctor, "Doctor"),
        RoadmapItem(R.drawable.teaching, "Teacher"),
        // Add more items as needed
    )
    private lateinit var subjectRecyclerView: RecyclerView
    private lateinit var roadmapRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        subjectRecyclerView = findViewById(R.id.subjectrecyclerview)
        roadmapRecyclerView = findViewById(R.id.roadmaprecyclerview)
        setupviews()
        settupdrawerlayout()

    }

    private fun setupviews() {

        // Set up Subject RecyclerView horizontally
        val subjectLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        subjectRecyclerView.layoutManager = subjectLayoutManager
        val subjectAdapter = SubjectAdapter(subjectItemList)
        subjectRecyclerView.adapter = subjectAdapter

        // Set up Roadmap RecyclerView horizontally
        val roadmapLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        roadmapRecyclerView.layoutManager = roadmapLayoutManager
        val roadmapAdapter = RoadmapAdapter(roadmapItemList)
        roadmapRecyclerView.adapter = roadmapAdapter
    }
    private fun settupdrawerlayout() {
        val drawer_layout=findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
        val navigationview=findViewById<com.google.android.material.navigation.NavigationView>(R.id.navigationview)
        val AppBar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.AppBar)
        setSupportActionBar(AppBar)
        actionBarDrawerToggle = ActionBarDrawerToggle(this,drawer_layout,
            R.string.app_name,
            R.string.app_name
        )
        actionBarDrawerToggle.syncState()
        navigationview.setNavigationItemSelectedListener {
//            val intent= Intent(this,ProfileActivity::class.java)
//            startActivity(intent)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}