package com.example.ahmedabadinstituteoftechnology.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ahmedabadinstituteoftechnology.databinding.ItemTimetableBinding

class TimetableAdapter(
    private val onDownloadClick: (String, String) -> Unit
) : RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder>() {

    private val timetableList = mutableListOf<Pair<String, String>>()

    fun updateData(newList: MutableList<Pair<String, String>>) {
        timetableList.clear()
        timetableList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableViewHolder {
        val binding = ItemTimetableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimetableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimetableViewHolder, position: Int) {
        val (fileName, downloadUrl) = timetableList[position]
        holder.bind(fileName, downloadUrl)
    }

    override fun getItemCount() = timetableList.size

    inner class TimetableViewHolder(private val binding: ItemTimetableBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fileName: String, downloadUrl: String) {
            binding.tvTimetableName.text = fileName
            binding.btnDownload.setOnClickListener { onDownloadClick(fileName, downloadUrl) }
        }
    }
}
