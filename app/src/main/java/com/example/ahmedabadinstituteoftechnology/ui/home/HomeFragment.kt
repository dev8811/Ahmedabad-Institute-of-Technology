package com.example.ahmedabadinstituteoftechnology.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ahmedabadinstituteoftechnology.R
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ahmedabadinstituteoftechnology.LoginActivity
import com.example.ahmedabadinstituteoftechnology.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Fetch enrollment number from SharedPreferences
        val enrollmentNumber = LoginActivity.getEnrollmentNumber(requireContext())
        if (enrollmentNumber != null) {
            homeViewModel.setEnrollmentNumber(enrollmentNumber)
        } else {
            Toast.makeText(context, "Enrollment number not found", Toast.LENGTH_SHORT).show()
        }

        // Observe data from ViewModel
        observeViewModel()

        // Set click listener for timetableIMG
        binding.timetableIMG.setOnClickListener {
            // Use NavController to navigate
            findNavController().navigate(R.id.action_navigation_home_to_Timetable_fragment)
        }

        return root
    }

    private fun observeViewModel() {
        homeViewModel.studentData.observe(viewLifecycleOwner) { student ->
            binding.profileName.text = "Name: ${student.name}"
            binding.profileSemester.text = "Semester: ${student.semester}"
        }

        homeViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}
data class Student(
    val name: String = "",
    val semester: String = ""
)
