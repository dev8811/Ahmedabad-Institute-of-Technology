package com.example.ahmedabadinstituteoftechnology

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ahmedabadinstituteoftechnology.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    // Declare the binding object
    private lateinit var binding: ActivityMainBinding

    // Firestore instance
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    // SharedPreferences key
    private val PREF_NAME = "AIT_Preferences"
    private val ENROLLMENT_KEY = "enrollment_number"
    private val LOGGED_IN_KEY = "logged_in"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if the user is already logged in
        if (isUserLoggedIn()) {
            navigateToMainActivity()
            return
        }

        // Initialize the binding object
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup onClickListener for the login button
        binding.btnLogin.setOnClickListener {
            val enrollmentNumber = binding.etEnrollment.text.toString()
            val password = binding.etPassword.text.toString()

            // Validate inputs
            if (enrollmentNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Authenticate user
                authenticateUser(enrollmentNumber, password)
            }
        }

        // Forgot password action
        binding.tvforgotpassword.setOnClickListener {
            Toast.makeText(this, "Forgot password clicked!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun authenticateUser(enrollmentNumber: String, password: String) {
        firestore.collection("Student")
            .document(enrollmentNumber)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val storedPassword = documentSnapshot.getString("password")

                    if (storedPassword != null && storedPassword == password) {
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                        // Save enrollment number and login state in SharedPreferences
                        saveLoginState(enrollmentNumber)

                        // Navigate to main activity
                        navigateToMainActivity()
                    } else {
                        Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveLoginState(enrollmentNumber: String) {
        val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(ENROLLMENT_KEY, enrollmentNumber)
        editor.putBoolean(LOGGED_IN_KEY, true)
        editor.apply()
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(LOGGED_IN_KEY, false)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, B_navigation_Activity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        fun getEnrollmentNumber(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences("AIT_Preferences", Context.MODE_PRIVATE)
            return sharedPreferences.getString("enrollment_number", null)
        }

        fun logoutUser(context: Context) {
            val sharedPreferences = context.getSharedPreferences("AIT_Preferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("logged_in", false)
            editor.remove("enrollment_number")
            editor.apply()
        }
    }
}
