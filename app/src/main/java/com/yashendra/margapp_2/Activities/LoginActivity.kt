package com.yashendra.margapp_2.Activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.yashendra.margapp_2.MainActivity
import com.yashendra.margapp_2.R
import com.yashendra.margapp_2.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private val RC_SIGN_IN: Int = 123
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        mAuth = FirebaseAuth.getInstance()
        auth=mAuth
        binding.btnlogin.setOnClickListener{
            signIn()
        }
        binding.signup.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.googlebutton.setOnClickListener {
            signInUsingGoogle()
        }

    }

    private fun signInUsingGoogle() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
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
            Log.d(ContentValues.TAG,"firebaseAuthwithGoogle:"+account.id)
            fireAuthWithGoogle(account.idToken!!)
        }catch (e: ApiException){
            Log.d(ContentValues.TAG,"sign in result failcode"+e.stackTrace)
        }
    }
    private fun fireAuthWithGoogle(idToken: String) {
        val credential= GoogleAuthProvider.getCredential(idToken,null)
        binding.googlebutton.isEnabled=false

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
            binding.googlebutton.isEnabled=true

        }
    }

    private fun signIn() {
        val email= binding.email.text.toString()
        val password = binding.password.text.toString()
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }
        GlobalScope.launch(Dispatchers.IO) {
            try {
                mAuth.signInWithEmailAndPassword(email, password).await()
                val user = mAuth.currentUser
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, ClassSelectActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Handle any exceptions here
            }
        }
    }
}