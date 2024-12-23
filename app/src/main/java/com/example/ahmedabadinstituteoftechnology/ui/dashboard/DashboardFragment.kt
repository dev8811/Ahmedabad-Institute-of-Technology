package com.example.ahmedabadinstituteoftechnology.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahmedabadinstituteoftechnology.databinding.FragmentDashboardBinding
import com.google.firebase.firestore.FirebaseFirestore

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    private lateinit var noticeAdapter: NoticeAdapter
    private val noticeList = mutableListOf<Notice>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        firestore = FirebaseFirestore.getInstance()
        // Initialize RecyclerView
        noticeAdapter = NoticeAdapter(noticeList)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = noticeAdapter
        }

        // Fetch data from Firestore
        fetchNotices()

        return root
    }

    private fun fetchNotices() {
        firestore.collection("notices").get()
            .addOnSuccessListener { documents ->
                noticeList.clear() // Clear existing data
                for (document in documents) {
                    val notice = document.toObject(Notice::class.java)
                    noticeList.add(notice)
                }

                // Sort the notices by date in descending order (newest first)
                noticeList.sortByDescending { it.date?.seconds }

                noticeAdapter.notifyDataSetChanged() // Refresh adapter
            }
            .addOnFailureListener { e ->
                // Handle error
                e.printStackTrace()
            }
    }

    data class Notice(
        val Title: String = "",
        val Semester: String = "",
        val Classname: String = "",
        val author: String = "",
        val date: Timestamp? = null // Use Timestamp here
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
