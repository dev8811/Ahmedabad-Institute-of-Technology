package com.example.ahmedabadinstituteoftechnology

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.ahmedabadinstituteoftechnology.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // List of mock enrollment numbers for testing
    private val mockEnrollmentNumbers = listOf(
        "230020107001", // 3rd Semester Regular Student
        "220020107054", // 5th Semester Regular Student
        "210020107029", // 7th Semester Regular Student
        "240023107017", // 3rd Semester D2D Student
        "240022107045", // 5th Semester D2D Student
        "240021107011"  // 7th Semester D2D Student
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Login button click
        binding.btnLogin.setOnClickListener {
            val enrollmentNumber = binding.etEnrollment.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (enrollmentNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (enrollmentNumber in mockEnrollmentNumbers && password == "123456") { // Mock credentials
                // Determine the semester based on the enrollment number
                val semester = determineSemester(enrollmentNumber)

                // Determine the student type based on the 6th digit (1 for regular, 3 for D2D)
                val studentType = if (enrollmentNumber[5] == '1') "Regular" else "D2D"

                // Show login success with student type and semester
                Toast.makeText(this, "Login Successful - $studentType - $semester", Toast.LENGTH_SHORT).show()

                // Proceed to the next screen (you can pass the semester and student type info if needed)
              //  startActivity(Intent(this, SomeNextActivity::class.java))
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        // Forgot Password click
        binding.tvforgotpassword.setOnClickListener {
            Toast.makeText(this, "Forgot Password", Toast.LENGTH_SHORT).show()

        }
    }

    // Logic to determine the semester based on enrollment number
    private fun determineSemester(enrollmentNumber: String): String {
        // Get the first 2 digits of the enrollment number which indicate the year
        val year = enrollmentNumber.take(2).toInt()

        // Determine the semester based on the year
        return when (year) {
            23 -> "3rd Semester"
            22 -> "5th Semester"
            21 -> "7th Semester"
            24 -> "3rd Semester" // D2D students also have similar semester pattern
            else -> "Unknown Semester"
        }
    }
}
