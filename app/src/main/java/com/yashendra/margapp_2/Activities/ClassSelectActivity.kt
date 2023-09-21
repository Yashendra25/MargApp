package com.yashendra.margapp_2.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.yashendra.margapp_2.Adapters.CustomArrayAdapter
import com.yashendra.margapp_2.MainActivity
import com.yashendra.margapp_2.R
import com.yashendra.margapp_2.databinding.ActivityClassSelectBinding

class ClassSelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClassSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassSelectBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val spinner = binding.spinner
        val spinnerValues = resources.getStringArray(R.array.spinner_values)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            spinnerValues
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Set the default selection to "9"
        val defaultSelection = "9"
        val defaultIndex = spinnerValues.indexOf(defaultSelection)
        if (defaultIndex != -1) {
            spinner.setSelection(defaultIndex)
        }

        binding.btnstartTest.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            intent.putExtra("class", spinner.selectedItem.toString())
            startActivity(intent)
            finish()
        }
    }
}