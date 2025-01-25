package com.example.ahmedabadinstituteoftechnology.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahmedabadinstituteoftechnology.LoginActivity
import com.example.ahmedabadinstituteoftechnology.R
import com.example.ahmedabadinstituteoftechnology.databinding.FragmentMyCourseBinding
import com.example.ahmedabadinstituteoftechnology.ui.home.adapter.TimetableAdapter
import com.google.firebase.storage.FirebaseStorage

class My_Course_Fragment : Fragment() {

    private var _binding: FragmentMyCourseBinding? = null
    private val binding get() = _binding!!
    private val courseAdapter =
        TimetableAdapter { fileName, downloadUrl -> downloadPDF(fileName, downloadUrl) }
    private val storageReference = FirebaseStorage.getInstance().reference

    // Map of branch codes to branch names
    private val branchCodeMap = mapOf(
        "07" to "Computer Engineering",
        "16" to "Information Technology",
        "19" to "Mechanical Engineering",
        "06" to "Civil Engineering",
        "09" to "Electrical Engineering",
        "11" to "Electronics and Communication Engineering",
        "05" to "Chemical Engineering",
        "02" to "Automobile Engineering",
        "03" to "Biomedical Engineering"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyCourseBinding.inflate(inflater, container, false)

        // Set up RecyclerView
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = courseAdapter
        }

        // Set up semester Spinner
        val semesters = listOf(
            "Select Semester",
            "Semester 1",
            "Semester 2",
            "Semester 3",
            "Semester 4",
            "Semester 5",
            "Semester 6",
            "Semester 7",
            "Semester 8"
        )

        val spinnerAdapterSemesters =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, semesters)
        spinnerAdapterSemesters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.courseSemesterSpinner.adapter = spinnerAdapterSemesters

        spinnerAdapterSemesters.setDropDownViewResource(R.layout.spinner_item) // Use same layout for dropdown
        binding.courseSemesterSpinner.adapter = spinnerAdapterSemesters

        // Automatically determine branch from enrollment number
        val enrollmentNumber =
            LoginActivity.getEnrollmentNumber(requireContext()) // Retrieve enrollment number
        val branchCode = enrollmentNumber?.substring(7, 9) // Extract 8th and 9th digits
        val branchName = branchCodeMap[branchCode]


        // Semester selection logic
        binding.courseSemesterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
             override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0 && branchName != null) {
                    val selectedSemester = semesters[position]
                    fetchTimetablesForBranchAndSemester(branchName, selectedSemester)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No action required
            }
        }


        return binding.root
    }

    private fun fetchTimetablesForBranchAndSemester(branch: String, semester: String) {
        val dialogView = layoutInflater.inflate(R.layout.custom_progress_dialog, null)
        val progressDialog = AlertDialog.Builder(requireContext()).apply {
            setView(dialogView)
            setCancelable(false)
        }.create()

        progressDialog.show()

        // Construct the path based on selected branch and semester
        val path = "Courses/$branch/$semester"

        storageReference.child(path).listAll().addOnSuccessListener { listResult ->
            val courses = mutableListOf<Pair<String, String>>()

            if (listResult.items.isEmpty()) {
                progressDialog.dismiss()
                Toast.makeText(
                    requireContext(),
                    "No courses available for $branch $semester",
                    Toast.LENGTH_SHORT
                ).show()
                courseAdapter.updateData(emptyList())
                return@addOnSuccessListener
            }

            val tasks = listResult.items.map { item ->
                item.downloadUrl.addOnSuccessListener { uri ->
                    courses.add(item.name to uri.toString())
                    if (courses.size == listResult.items.size) {
                        progressDialog.dismiss()
                        courseAdapter.updateData(courses)
                    }
                }
            }

            tasks.forEach { task ->
                task.addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Toast.makeText(
                        requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.addOnFailureListener { exception ->
            progressDialog.dismiss()
            Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    @SuppressLint("Range")
    private fun downloadPDF(fileName: String, downloadUrl: String) {
        val progressDialog = ProgressDialog(requireContext()).apply {
            setTitle("Downloading $fileName")
            setMessage("Please wait...")
            setCancelable(false)
            show()
        }

        val request = DownloadManager.Request(Uri.parse(downloadUrl)).apply {
            setTitle("Downloading $fileName")
            setDescription("Downloading file...")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        }

        val downloadManager =
            requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)

        Thread {
            var isDownloading = true
            while (isDownloading) {
                val query = DownloadManager.Query().setFilterById(downloadId)
                val cursor = downloadManager.query(query)

                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            isDownloading = false
                            requireActivity().runOnUiThread {
                                progressDialog.dismiss()
                                Toast.makeText(
                                    requireContext(),
                                    "$fileName downloaded successfully!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        DownloadManager.STATUS_FAILED -> {
                            isDownloading = false
                            requireActivity().runOnUiThread {
                                progressDialog.dismiss()
                                Toast.makeText(
                                    requireContext(), "Download failed", Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
                cursor.close()
                Thread.sleep(1000)
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
