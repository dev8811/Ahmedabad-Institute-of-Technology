package com.example.ahmedabadinstituteoftechnology.ui.home

import android.annotation.SuppressLint
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

    private val calendar = Calendar.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private var studentId = "240023107017" // Replace with dynamic student ID if needed

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)

        setupCalendar()
        setupNavigation()

        return binding.root
    }

    private fun setupNavigation() {
        binding.prevMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            setupCalendar()
        }

        binding.nextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            setupCalendar()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupCalendar() {
        updateMonthText()
        fetchAttendanceData { attendance ->
            val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val firstDayOfMonth = calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }.get(Calendar.DAY_OF_WEEK) - 1

            val daysList = mutableListOf<DayItem>()

            // Add empty items for days before the first day of the month
            repeat(firstDayOfMonth) {
                daysList.add(DayItem("", "empty"))
            }

            // Add days of the month with attendance statuses
            for (day in 1..daysInMonth) {
                val dateKey = String.format("%04d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, day)
                val status = when {
                    isWeekend(day) -> "holiday"
                    attendance.containsKey(dateKey) -> attendance[dateKey] ?: "absent"
                    else -> "absent"
                }
                daysList.add(DayItem(day.toString(), status))
            }

            // Bind data to RecyclerView
            val adapter = CalendarRecyclerAdapter(requireContext(), daysList)
            binding.calendarRecycler.layoutManager = GridLayoutManager(requireContext(), 7)
            binding.calendarRecycler.adapter = adapter

            updateSummary(attendance, daysInMonth)
        }
    }

    private fun updateMonthText() {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        binding.monthText.text = dateFormat.format(calendar.time)
    }

    private fun fetchAttendanceData(callback: (Map<String, String>) -> Unit) {
        val monthKey = String.format("%04d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1)
        val attendanceCollection = firestore.collection("Student").document(studentId).collection("Attendance")

        attendanceCollection.document(monthKey).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Convert Firestore data to a map
                    val attendance = document.data?.mapValues { it.value.toString() } ?: emptyMap()
                    callback(attendance)
                } else {
                    callback(emptyMap())
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to fetch attendance data", Toast.LENGTH_SHORT).show()
                callback(emptyMap())
            }
    }

    private fun isWeekend(day: Int): Boolean {
        val tempCalendar = calendar.clone() as Calendar
        tempCalendar.set(Calendar.DAY_OF_MONTH, day)
        val dayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
    }

    @SuppressLint("SetTextI18n")
    private fun updateSummary(attendance: Map<String, String>, totalDays: Int) {
        val presentCount = attendance.values.count { it == "present" }
        val absentCount = attendance.values.count { it == "absent" }
        val holidaysCount = (1..totalDays).count { isWeekend(it) }

        binding.summaryPresent.text = "Present: $presentCount"
        binding.summaryAbsent.text = "Absent: $absentCount"
        binding.summaryHolidays.text = "Holidays: $holidaysCount"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
