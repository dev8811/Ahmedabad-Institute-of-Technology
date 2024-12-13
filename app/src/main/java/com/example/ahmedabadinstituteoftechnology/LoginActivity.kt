package com.example.ahmedabadinstituteoftechnology

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        firestore.collection("Users")
            .document(enrollmentNumber)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val storedPassword = documentSnapshot.getString("password")

                    if (storedPassword != null && storedPassword == password) {
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                        // Navigate to the next screen
                        startActivity(Intent(this, B_navigation_Activity::class.java))
                        finish()
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
}
