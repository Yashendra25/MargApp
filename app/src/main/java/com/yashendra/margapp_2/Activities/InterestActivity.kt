package com.yashendra.margapp_2.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yashendra.margapp_2.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class InterestActivity : AppCompatActivity() {
    // Define arrays for career choices under each category
    // Define arrays for career choices under each category
    val scienceChoices = arrayOf(
        "None",
        "Doctor",
        "Engineer",
        "Scientist",
        "Pharmacist",
        "Dentist",
        "Veterinarian",
        "Architect",
        "Pilot",
        "Physiotherapist",
        "Nutritionist",
        "Agricultural scientist",
        "Data scientist"
    )
    val commerceChoices = arrayOf(
        "None",
        "Chartered Accountant (CA)",
        "Company Secretary (CS)",
        "Cost and Management Accountant (CMA)",
        "Banker",
        "Financial analyst",
        "Accountant",
        "Marketing manager",
        "Human resource manager",
        "Business analyst",
        "Entrepreneur",
        "Economist"
    )
    val artsChoices = arrayOf(
        "None",
        "Lawyer",
        "Journalist",
        "Teacher",
        "Professor",
        "Writer",
        "Editor",
        "Translator",
        "Graphic designer",
        "Web designer",
        "Artist",
        "Musician",
        "Dancer"
    )
    val otherChoices = arrayOf(
        "None",
        "Fashion designer",
        "Interior designer",
        "Hotel management",
        "Tourism management",
        "Aviation management",
        "Event management",
        "Sports management",
        "Armed forces",
        "Civil services"
    )
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest)

        auth = FirebaseAuth.getInstance()
        val categorySpinner = findViewById<Spinner>(R.id.categorySpinner)
        val choicesSpinner = findViewById<Spinner>(R.id.choicesSpinner)
        val editTextOtherHobbies = findViewById<EditText>(R.id.editTextOtherHobbies)

        val proceedButton = findViewById<Button>(R.id.proceedButton)
        val chipGroupHobbies = findViewById<ChipGroup>(R.id.chipGroupHobbies)
        val editTextOthercarreroption=findViewById<EditText>(R.id.editTextOthercarreroption)

        // Retrieve intent values
        // Retrieve intent values
        val classValue = intent.getStringExtra("class").toString()
        val scoreValue = intent.getIntExtra("score", 0)

        Log.d("InterestActivity", "Class: $classValue")
        Log.d("InterestActivity", "Score: $scoreValue")

        val intent=Intent(this,Dashboard::class.java)


        proceedButton.setOnClickListener {
            val selectedCategory = categorySpinner.selectedItem?.toString() ?: ""
            val selectedChoice = choicesSpinner.selectedItem?.toString() ?: ""
            val otherHobbies = editTextOtherHobbies.text?.toString() ?: ""
            val othercarreroption = editTextOthercarreroption.text?.toString() ?: ""

            // Initialize an empty list to store data for all three years
            val yearsData = mutableListOf<Map<String, Any>>()

            for (year in 1..3) { // Loop through Year 1, Year 2, and Year 3

                val mathMarks = findViewById<EditText>(resources.getIdentifier("editText_math_year$year", "id", packageName)).text.toString()
                val physicsMarks = findViewById<EditText>(resources.getIdentifier("editText_physics_year$year", "id", packageName)).text.toString()
                val chemistryMarks = findViewById<EditText>(resources.getIdentifier("editText_chemistry_year$year", "id", packageName)).text.toString()
                val englishMarks = findViewById<EditText>(resources.getIdentifier("editText_english_year$year", "id", packageName)).text.toString()
                val biologyMarks = findViewById<EditText>(resources.getIdentifier("editText_biology_year$year", "id", packageName)).text.toString()

                // Create a data map for the current year
                val yearData = HashMap<String, Any>()
                yearData["year"] = "Year $year"
                yearData["mathMarks"] = mathMarks
                yearData["physicsMarks"] = physicsMarks
                yearData["chemistryMarks"] = chemistryMarks
                yearData["englishMarks"] = englishMarks
                yearData["biologyMarks"] = biologyMarks

                // Add the year's data to the list
                yearsData.add(yearData)
            }

            val userEmail = auth.currentUser?.email // Get the current user's email

            // Create a data map to store in Firestore
            val userHobbiesData = HashMap<String, Any>()
            userHobbiesData["selectedCategory"] = selectedCategory
            userHobbiesData["selectedChoice"] = selectedChoice
            userHobbiesData["otherHobbies"] = otherHobbies
            userHobbiesData["othercarreroption"]=othercarreroption
            userHobbiesData["yearsData"] = yearsData

            // Store the user's email
            userEmail?.let {
                userHobbiesData["userEmail"] = it
            }
            // Add the class and score values to the data map
            userHobbiesData["class"] = classValue
            userHobbiesData["score"] = scoreValue

            // Get the selected chips from ChipGroup
            val selectedChips = mutableListOf<String>()
            for (i in 0 until chipGroupHobbies.childCount) {
                val chip = chipGroupHobbies.getChildAt(i) as Chip
                if (chip.isChecked) {
                    selectedChips.add(chip.text.toString())
                }
            }
            userHobbiesData["selectedChips"] = selectedChips

            // Add this data to Firestore using the user's email as document ID
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    // Use the email as the document ID
                    userEmail?.let { userEmail ->
                        db.collection("careerdata")
                            .document(userEmail)
                            .set(userHobbiesData)
                            .await() // Wait for the operation to complete
                    }
                    // Data added successfully
                    showToast("Data saved to Firestore")
                    startActivity(intent)
                } catch (e: Exception) {
                    // Error occurred while adding data
                    showToast("Error: $e")
                }
            }
        }

        // Define an ArrayAdapter for the category Spinner
        val categoryAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.categories,
            android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter for the category Spinner
        categorySpinner.adapter = categoryAdapter

        // Define ArrayAdapter for career choices
        // Define ArrayAdapter for career choices
        val choicesAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item)


        // Set the adapter for the choices Spinner
        choicesSpinner.adapter = choicesAdapter

        // Handle item selection in the category Spinner
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                // Clear the choicesAdapter when a new category is selected
                choicesAdapter.clear()

                // Populate the choicesAdapter based on the selected category
                val selectedCategory = parentView?.getItemAtPosition(position).toString()
                when (selectedCategory) {
                    "Science" -> choicesAdapter.addAll(*scienceChoices)
                    "Commerce" -> choicesAdapter.addAll(*commerceChoices)
                    "Arts" -> choicesAdapter.addAll(*artsChoices)
                    "Other" -> choicesAdapter.addAll(*otherChoices)
                }

                // Notify the adapter that the data has changed
                choicesAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing here
            }
        }
    }
    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@InterestActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}