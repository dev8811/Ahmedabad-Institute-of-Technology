package com.example.ahmedabadinstituteoftechnology.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ahmedabadinstituteoftechnology.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AttendanceFragment : Fragment() {

    private lateinit var calendarGridView: GridView
    private lateinit var firestore: FirebaseFirestore
    private val attendanceData = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_attendance_, container, false)
        calendarGridView = view.findViewById(R.id.calendarGridView)

        // Fetch attendance data and setup calendar
        fetchAttendanceData { fetchedData ->
            attendanceData.putAll(fetchedData)
            setupCalendar()
        }

        return view
    }

    private fun fetchAttendanceData(onComplete: (Map<String, String>) -> Unit) {
        // Fetch attendance for December 2024
        firestore.collection("attendance").document("2024-12")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val data = document.data?.mapValues { it.value.toString() } ?: emptyMap()
                    onComplete(data)
                } else {
                    onComplete(emptyMap())
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                onComplete(emptyMap())
            }
    }

    private fun setupCalendar() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        calendar.set(Calendar.YEAR, 2024)

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val daysList = mutableListOf<Date>()

        // Generate a list of all dates in the month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        while (daysList.size < daysInMonth) {
            daysList.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // Set the adapter for the GridView
        val adapter = CalendarAdapter(requireContext(), daysList, attendanceData)
        calendarGridView.adapter = adapter
    }
}
