package com.example.ahmedabadinstituteoftechnology.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ahmedabadinstituteoftechnology.databinding.FragmentNotificationsBinding
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get enrollment number from arguments
        val enrollmentNumber = arguments?.getString("enrollment_number")


        if (enrollmentNumber != null) {
            fetchProfileData(enrollmentNumber)
        } else {
            Toast.makeText(context, "Error: Enrollment number not found", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    private fun fetchProfileData(enrollmentNumber: String) {
        val studentRef = firestore.collection("Student").document(enrollmentNumber)

        studentRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name")
                    val email = document.getString("Email")
                    val semester = document.getString("semester")
                    val enrollment = document.getString("enrollmentNo")
                    val DOB = document.getString("DOB")
                    val CON_number = document.getString("contact_number")

                    binding.tvStudentName.text = name ?: "N/A"
                    binding.tvStudentEmail.text = email ?: "N/A"
                    binding.tvStudentSemester.text = "Semester: ${semester ?: "N/A"}"
                    binding.tvSEnrollment.text = "Enrollment No: ${enrollment ?: "N/A"}"
                    binding.tvSDOB.text = DOB ?: "N/A"
                    binding.tvSContactNum.text = "Contact No: ${CON_number ?: "N/A"}"

                } else {
                    Toast.makeText(context, "No such document", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
