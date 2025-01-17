package com.example.ahmedabadinstituteoftechnology.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ahmedabadinstituteoftechnology.LoginActivity
import com.example.ahmedabadinstituteoftechnology.R
import com.example.ahmedabadinstituteoftechnology.databinding.FragmentHomeBinding

class HomeFragment : Fragment()
{

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Fetch enrollment number and set it in ViewModel
        LoginActivity.getEnrollmentNumber(requireContext())?.let { enrollmentNumber ->
            homeViewModel.setEnrollmentNumber(enrollmentNumber)
        } ?: run {
            Toast.makeText(context, "Enrollment number not found", Toast.LENGTH_SHORT).show()
        }

        // Observe data from ViewModel
        observeViewModel()

        // Set up navigation
        setupNavigation()

        return binding.root
    }

    private fun setupNavigation() {
        // Map views to navigation actions
        val navigationMap = mapOf(

            binding.timetableIMG to R.id.action_navigation_home_to_Timetable_fragment,
            binding.timeTableCard to R.id.action_navigation_home_to_Timetable_fragment,
            binding.attendanceCARD to R.id.action_navigation_home_to_navigation_attendance,
            binding.attendanceIMG to R.id.action_navigation_home_to_navigation_attendance,
            binding.resultsIMG to R.id.action_navigation_home_to_navigation_result,
            binding.MyCourseCARD to R.id.action_navigation_home_to_my_Course_Fragment,
            binding.MyCourseIMG to R.id.action_navigation_home_to_my_Course_Fragment,
            binding.EventsCARD to R.id.action_navigation_home_to_eventFragment,
            binding.EventsIMG to R.id.action_navigation_home_to_eventFragment,
            binding.AssignmentsCARD to R.id.action_navigation_home_to_assignnmentsFragment,
            binding.AssignmentsIMG to R.id.action_navigation_home_to_assignnmentsFragment,
            binding.ExamScheduleCARD to R.id.action_navigation_home_to_exam_scheduleFragment,
            binding.ExamScheduleIMG to R.id.action_navigation_home_to_exam_scheduleFragment
        )

        // Assign click listeners using a loop
        navigationMap.forEach { (view, actionId) ->
            view.setOnClickListener {
                findNavController().navigate(actionId)
            }
        }
    }

    private fun observeViewModel() {
        homeViewModel.studentData.observe(viewLifecycleOwner) { student ->
            student?.let {
                binding.profileName.text = "Name: ${it.Name}"
                binding.profileSemester.text = "Semester: ${it.semester}"
            }
        }

        homeViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}

data class Student(
    val Name: String = "",
    val semester: String = ""
)