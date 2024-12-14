package com.example.ahmedabadinstituteoftechnology.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ahmedabadinstituteoftechnology.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.button110.setOnClickListener {addMultipleStudents()  }

        return root
    }



    data class Student(
        val enrollmentNo: String,
        val name: String,
        val email: String,
        val password: String,
        val semester: String
    )

    fun addMultipleStudents() {
        val db = FirebaseFirestore.getInstance()

        // List of students to add
        val students = listOf(
            Student("240023107018", "John", "john@gmail.com", "123456", "3"),
            Student("240023107019", "Alice", "alice@gmail.com", "123456", "3"),
            Student("240023107020", "Bob", "bob@gmail.com", "123456", "3"),
            Student("240023107021", "Charlie", "charlie@gmail.com", "123456", "3")
        )

        // Iterate and add each student to Firestore
        for (student in students) {
            db.collection("Student")
                .document(student.enrollmentNo) // Use enrollment number as document ID
                .set(student)
                .addOnSuccessListener {
                    println("Successfully added student: ${student.name}")
                }
                .addOnFailureListener { e ->
                    println("Error adding student: ${e.message}")
                }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}