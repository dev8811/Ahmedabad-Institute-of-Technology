package com.example.ahmedabadinstituteoftechnology.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahmedabadinstituteoftechnology.databinding.ItemTimetableBinding

class CourseAdapter(
    private val onDownloadClick: (fileName: String, downloadUrl: String) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    private val CourseList = mutableListOf<Pair<String, String>>()

    /**
     * Updates the data in the adapter and refreshes the RecyclerView.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Pair<String, String>>) {
        CourseList.clear()
        CourseList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding =
            ItemTimetableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(CourseList[position])
    }

    override fun getItemCount(): Int = CourseList.size

    /**
     * ViewHolder class for timetable items.
     */
    inner class CourseViewHolder(private val binding: ItemTimetableBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the data to the views.
         */
        fun bind(item: Pair<String, String>) {
            val (fileName, downloadUrl) = item
            binding.tvTimetableName.text = fileName
            binding.btnDownload.setOnClickListener { onDownloadClick(fileName, downloadUrl) }
        }
    }
}
