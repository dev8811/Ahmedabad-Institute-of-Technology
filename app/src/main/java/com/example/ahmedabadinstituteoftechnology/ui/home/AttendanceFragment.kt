package com.example.ahmedabadinstituteoftechnology.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ahmedabadinstituteoftechnology.databinding.FragmentAttendanceBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private val attendanceData = mutableMapOf<String, String>()
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)

        // Setup RecyclerView
        binding.calendarRecycler.layoutManager = GridLayoutManager(requireContext(), 7) // 7 days in a week
        updateMonthText()
        loadAttendanceData()

        binding.prevMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateMonthText()
            loadAttendanceData()
        }

        binding.nextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateMonthText()
            loadAttendanceData()
        }

        return binding.root
    }

    private fun updateMonthText() {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        binding.monthText.text = dateFormat.format(calendar.time)
    }

    private fun loadAttendanceData() {
        val dateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val monthKey = dateFormat.format(calendar.time)

        firestore.collection("attendance").document(monthKey)
            .get()
            .addOnSuccessListener { document ->
                attendanceData.clear()
                attendanceData.putAll(document?.data?.mapValues { it.value.toString() } ?: emptyMap())
                setupCalendar()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                attendanceData.clear()
                setupCalendar()
            }
    }

    private fun setupCalendar() {
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfMonth = calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }.get(Calendar.DAY_OF_WEEK) - 1
        val daysList = mutableListOf<DayItem>()

        // Add "empty" items for days before the first day of the month
        repeat(firstDayOfMonth) {
            daysList.add(DayItem("", "empty"))
        }

        // Add days of the month with attendance statuses
        for (day in 1..daysInMonth) {
            val dayKey = day.toString().padStart(1, '0')
            val status = attendanceData[dayKey] ?: "holiday"
            daysList.add(DayItem(day.toString(), status))
        }

        // Bind data to RecyclerView
        val adapter = CalendarRecyclerAdapter(requireContext(), daysList)
        binding.calendarRecycler.adapter = adapter

        updateSummary()
    }

    private fun updateSummary() {
        val presentCount = attendanceData.values.count { it == "present" }
        val absentCount = attendanceData.values.count { it == "absent" }
        val totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val holidaysCount = totalDays - (presentCount + absentCount)

        binding.summaryPresent.text = "Present: $presentCount"
        binding.summaryAbsent.text = "Absent: $absentCount"
        binding.summaryHolidays.text = "Holidays: $holidaysCount"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
