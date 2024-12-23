package com.example.ahmedabadinstituteoftechnology.ui.notifications

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ahmedabadinstituteoftechnology.LoginActivity
import com.example.ahmedabadinstituteoftechnology.databinding.FragmentNotificationsBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get enrollment number from SharedPreferences
        val enrollmentNumber = LoginActivity.getEnrollmentNumber(requireContext())
        if (enrollmentNumber != null) {
            profileViewModel.setEnrollmentNumber(enrollmentNumber)
        } else {
            Toast.makeText(context, "Enrollment number not found", Toast.LENGTH_SHORT).show()
        }


        observeViewModel()

        return root
    }

    private fun observeViewModel() {
        profileViewModel.profileData.observe(viewLifecycleOwner) { profile ->
            binding.tvStudentName.text = profile.name
            binding.tvStudentEmail.text = "Email : ${profile.email}"
            binding.tvStudentSemester.text = "Semester : ${profile.semester}"
            binding.tvStudentEnrollment.text = "Enrollment No : ${profile.enrollment}"
            binding.tvStudentDOB.text = "Date of Birth : ${profile.dateOfBirth}"
            binding.tvStudentContact.text = "Contact No : ${profile.contactNumber}"
            binding.tvStudentBranch.text = "Branch : ${profile.branch}"
            binding.tvPerentContact.text = "Parent Contact No : ${profile.parent_Number}"
            binding.tvStudentABCID.text = "ABC ID : ${profile.abc_id}"
        }

        profileViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
