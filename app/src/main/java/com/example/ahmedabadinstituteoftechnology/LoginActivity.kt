package com.example.ahmedabadinstituteoftechnology


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ahmedabadinstituteoftechnology.databinding.ActivityMainBinding
import com.example.ahmedabadinstituteoftechnology.ui.home.HomeFragment

class LoginActivity : AppCompatActivity() {

    // Declare the binding object
    private lateinit var binding: ActivityMainBinding

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
                val currentSemester = getCurrentSemester(enrollmentNumber)
                if (currentSemester != null) {
                    Toast.makeText(this, "Current Semester: $currentSemester", Toast.LENGTH_LONG).show()
                  startActivity(Intent(this, B_navigation_Activity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Invalid Enrollment Number", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Forgot password action
        binding.tvforgotpassword.setOnClickListener {
            Toast.makeText(this, "Forgot password clicked!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentSemester(enrollmentNumber: String): Int? {
        if (enrollmentNumber.length != 12) return null // Ensure enrollment number is valid

        val admissionYear = enrollmentNumber.substring(0, 2).toInt() + 2000 // First 2 digits
        val instituteCode = enrollmentNumber.substring(2, 5)
        val studentTypeCode = enrollmentNumber.substring(5, 8)

        // Check for valid institute and student type codes
        if (instituteCode != "002" || (studentTypeCode != "010" && studentTypeCode != "310")) {
            return null
        }

        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val currentMonth = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1

        // Semester calculation logic
        val yearDifference = currentYear - admissionYear
        val semesterOffset = if (currentMonth in 1..6) 0 else 1 // Add 1 if after June

        return when (studentTypeCode) {
            "010" -> (yearDifference * 2) + semesterOffset - 0 // Regular students
            "310" -> ((yearDifference - 1) * 2) + semesterOffset + 4 // D to D students
            else -> null
        }
    }
}
