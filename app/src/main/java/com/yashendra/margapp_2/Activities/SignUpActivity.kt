package com.yashendra.margapp_2.Activities


import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.yashendra.margapp_2.MainActivity
import com.yashendra.margapp_2.Model.User
import com.yashendra.margapp_2.R
import com.yashendra.margapp_2.databinding.ActivityLoginBinding
import com.yashendra.margapp_2.databinding.ActivitySignUpBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.*


class SignUpActivity : AppCompatActivity() {
    private val scope = CoroutineScope(Dispatchers.Main)
    private val RC_SIGN_IN: Int = 123
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()




        binding.signinnow.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        signupUsinggoogle()
        binding.btnsignup.setOnClickListener{
            sinupUsingEmail()
        }
        binding.dateOfBirth.setOnClickListener {
            showDatePickerDialog()
        }
    }
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val formattedDate = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth)
                val dateOfBirthInput = findViewById<EditText>(R.id.date_of_birth)
                dateOfBirthInput.setText(formattedDate)
            },
            // Initial year, month, day for date picker
            2000, 0, 1
        )
        datePickerDialog.show()
    }
    private fun sinupUsingEmail() {

        val signupButton = findViewById<Button>(R.id.btnsignup)
        signupButton.setOnClickListener(View.OnClickListener {
            val firstName = findViewById<EditText>(R.id.first_name).text.toString()
            val lastName = findViewById<EditText>(R.id.last_name).text.toString()
            val email = findViewById<EditText>(R.id.email).text.toString()
            val phone = findViewById<EditText>(R.id.phone_no).text.toString()
            val dob = findViewById<EditText>(R.id.date_of_birth).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.cnfirm_password).text.toString()
            val acceptTerms = findViewById<CheckBox>(R.id.accept_terms_checkbox).isChecked

            if (isValidData(firstName, lastName, email, phone, dob, password, confirmPassword, acceptTerms)) {
                val user = User(firstName, lastName, email, phone, dob, password)
                val db = FirebaseFirestore.getInstance()

                // Use Kotlin coroutines to run Firestore write operation
                scope.launch(Dispatchers.IO) {
                    try {
                        binding.progressBar.visibility=View.VISIBLE
                        binding.progressBar.requestFocus()
                        val documentReference = db.collection("users").add(user).await()

                        // Now, create the user with email and password
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this@SignUpActivity) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    val user = auth.currentUser
                                    Toast.makeText(this@SignUpActivity, "Account created successfully", Toast.LENGTH_SHORT).show()
                                    binding.progressBar.visibility=View.GONE
                                    // Start the new activity
                                    val intent = Intent(this@SignUpActivity, ClassSelectActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                                    binding.progressBar.visibility=View.GONE
                                }
                            }

                        launch(Dispatchers.Main) {
                            Toast.makeText(this@SignUpActivity, "User registered successfully with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()

                            // Perform any other necessary operations
                        }
                    } catch (e: Exception) {
                        launch(Dispatchers.Main) {
                            Toast.makeText(this@SignUpActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility=View.GONE
                        }
                    }
                }
            } else {
                // Handle invalid data (e.g., show error messages)
            }
        })
    }

    private fun isValidData(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        dob: String,
        password: String,
        confirmPassword: String,
        acceptTerms: Boolean
    ): Boolean {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || dob.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            // Show an error message or handle the empty fields
            return false
        }

        if (password != confirmPassword) {
            // Passwords don't match, show an error message
            return false
        }

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!email.matches(emailPattern.toRegex())) {
            // Invalid email format, show an error message
            return false
        }

        if (!acceptTerms) {
            // Terms not accepted, show an error message or handle it
            return false
        }

        return true
    }
    private fun signupUsinggoogle() {
        binding.google.setOnClickListener {
            // Configure Google Sign In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(this, gso)

            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //result returnned from launching the activity
        if(requestCode==RC_SIGN_IN)
        {
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try{
            val account=task.getResult(ApiException::class.java)!!
            Log.d(TAG,"firebaseAuthwithGoogle:"+account.id)
            fireAuthWithGoogle(account.idToken!!)
        }catch (e:ApiException){
            Log.d(TAG,"sign in result failcode"+e.stackTrace)
        }
    }

    private fun fireAuthWithGoogle(idToken: String) {
        val credential=GoogleAuthProvider.getCredential(idToken,null)
        binding.google.visibility= View.GONE
        binding.progressBar.visibility=View.VISIBLE
        GlobalScope.launch(Dispatchers.IO){
            val auth=auth.signInWithCredential(credential).await()
            val firebaseuser=auth.user
            withContext(Dispatchers.Main){
                updateUI(firebaseuser)
            }
        }
    }
    private fun updateUI(firebaseuser: FirebaseUser?) {
        if (firebaseuser!=null){
//            val user= User(firebaseuser.uid,firebaseuser.displayName,firebaseuser.photoUrl.toString())
//            val userDao=UserDao()
//            userDao.addusers(user)

            val mainActivityIntent=Intent(this,ClassSelectActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
        }
        else{
            binding.btnsignup.visibility= View.VISIBLE
            binding.progressBar.visibility=View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the coroutine scope when the activity is destroyed
        scope.cancel()
    }


}