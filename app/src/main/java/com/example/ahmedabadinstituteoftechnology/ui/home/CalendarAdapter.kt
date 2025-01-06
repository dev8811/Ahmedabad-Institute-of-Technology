package com.example.ahmedabadinstituteoftechnology.ui.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(
    private val context: Context,
    private val dates: List<Date>,
    private val attendanceData: Map<String, String>
) : BaseAdapter() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dayFormat = SimpleDateFormat("d", Locale.getDefault())
    private val dayOfWeekFormat = SimpleDateFormat("EEE", Locale.getDefault())

    override fun getCount(): Int = dates.size

    override fun getItem(position: Int): Any = dates[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        val dateText = view.findViewById<TextView>(android.R.id.text1)

        val date = dates[position]
        val dateString = dateFormat.format(date)
        val dayString = dayFormat.format(date)
        val dayOfWeek = dayOfWeekFormat.format(date)

        dateText.text = dayString
        dateText.textAlignment = View.TEXT_ALIGNMENT_CENTER
        dateText.setBackgroundColor(Color.TRANSPARENT) // Default background
        dateText.setTextColor(Color.BLACK) // Default text color

        // Apply attendance color
        when {
            dayOfWeek == "Sat" || dayOfWeek == "Sun" -> {
                // Mark Saturdays and Sundays as holidays (Gray)
                dateText.setBackgroundColor(Color.GRAY)
                dateText.setTextColor(Color.WHITE)
            }
            attendanceData[dateString] == "present" -> {
                dateText.setBackgroundColor(Color.GREEN) // Present: Green
                dateText.setTextColor(Color.WHITE)
            }
            attendanceData[dateString] == "absent" -> {
                dateText.setBackgroundColor(Color.RED) // Absent: Red
                dateText.setTextColor(Color.WHITE)
            }
            else -> {
                dateText.setBackgroundColor(Color.TRANSPARENT) // Default: No background
            }
        }

        return view
    }
}
