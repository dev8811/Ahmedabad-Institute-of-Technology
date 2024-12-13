package com.example.ahmedabadinstituteoftechnology.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ahmedabadinstituteoftechnology.databinding.FragmentNotificationsBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Retrieve the data passed from LoginActivity
        val currentSemester = arguments?.getInt("CURRENT_SEMESTER", -1)
        val enrollmentNumber = arguments?.getString("ENROLLMENT_NUMBER")

        // Display the enrollment number and current semester
        binding.tvEnrollmentNumber.text = "Enrollment Number: $enrollmentNumber"
        binding.tvCurrentSemester.text = "Current Semester: $currentSemester"

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
