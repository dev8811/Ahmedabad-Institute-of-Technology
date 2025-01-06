package com.example.ahmedabadinstituteoftechnology.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahmedabadinstituteoftechnology.databinding.ItemTimetableBinding

class TimetableAdapter(
    private val onDownloadClick: (fileName: String, downloadUrl: String) -> Unit
) : RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder>() {

    private val timetableList = mutableListOf<Pair<String, String>>()

    /**
     * Updates the data in the adapter and refreshes the RecyclerView.
     */
    fun updateData(newList: List<Pair<String, String>>) {
        timetableList.clear()
        timetableList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableViewHolder {
        val binding = ItemTimetableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimetableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimetableViewHolder, position: Int) {
        holder.bind(timetableList[position])
    }

    override fun getItemCount(): Int = timetableList.size

    /**
     * ViewHolder class for timetable items.
     */
    inner class TimetableViewHolder(private val binding: ItemTimetableBinding) :
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
