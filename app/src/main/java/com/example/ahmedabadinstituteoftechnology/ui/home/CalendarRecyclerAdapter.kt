package com.example.ahmedabadinstituteoftechnology.ui.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ahmedabadinstituteoftechnology.R

class CalendarRecyclerAdapter(
    private val context: Context,
    private val daysList: List<DayItem>
) : RecyclerView.Adapter<CalendarRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayText: TextView = view.findViewById(R.id.day_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.calendar_day_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayItem = daysList[position]

        holder.dayText.text = dayItem.day

        when (dayItem.status) {
            "present" -> {
                holder.dayText.setBackgroundColor(Color.parseColor("#4CAF50")) // Green
                holder.dayText.setTextColor(Color.WHITE)
            }
            "absent" -> {
                holder.dayText.setBackgroundColor(Color.parseColor("#F44336")) // Red
                holder.dayText.setTextColor(Color.WHITE)
            }
            "holiday" -> {
                holder.dayText.setBackgroundColor(Color.parseColor("#9E9E9E")) // Gray
                holder.dayText.setTextColor(Color.WHITE)
            }
            "empty" -> {
                holder.dayText.setBackgroundColor(Color.TRANSPARENT)
                holder.dayText.text = ""
            }
        }
    }

    override fun getItemCount(): Int = daysList.size
}

data class DayItem(
    val day: String,
    val status: String
)
