package com.example.ahmedabadinstituteoftechnology.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        // Set up the logout button
        binding.btnLogout.setOnClickListener {
            logoutUser()
        }

        observeViewModel()

        return root
    }

    private fun logoutUser() {
        LoginActivity.logoutUser(requireContext())
        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()

        // Navigate back to the LoginActivity
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
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
