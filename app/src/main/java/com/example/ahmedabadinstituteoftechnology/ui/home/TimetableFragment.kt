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
import com.example.ahmedabadinstituteoftechnology.databinding.FragmentTimetableBinding
import com.example.ahmedabadinstituteoftechnology.ui.home.adapter.TimetableAdapter
import com.google.firebase.storage.FirebaseStorage

class TimetableFragment : Fragment() {

    private var _binding: FragmentTimetableBinding? = null
    private val binding get() = _binding!!
    private val timetableAdapter =
        TimetableAdapter { fileName, downloadUrl -> downloadPDF(fileName, downloadUrl) }
    private val storageReference = FirebaseStorage.getInstance().reference

    // Branch mapping based on enrollment number
    private val branchMap = mapOf(
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
        _binding = FragmentTimetableBinding.inflate(inflater, container, false)

        // Fetch enrollment number from LoginActivity
        val enrollmentNumber = LoginActivity.getEnrollmentNumber(requireContext()) ?: "0000000000"
        val branchName = getBranchFromEnrollment(enrollmentNumber)

        if (branchName == null) {
            Toast.makeText(requireContext(), "Invalid enrollment number", Toast.LENGTH_SHORT).show()
        }

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = timetableAdapter

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
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, semesters)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.semesterSpinner.adapter = spinnerAdapter

        // Set Item Selected Listener for Spinner
        binding.semesterSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    if (position != 0 && branchName != null) {
                        val selectedSemester = semesters[position]
                        fetchTimetablesForBranchAndSemester(branchName!!, selectedSemester)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        return binding.root
    }

    private fun getBranchFromEnrollment(enrollmentNumber: String): String? {
        return if (enrollmentNumber.length >= 9) {
            val branchCode = enrollmentNumber.substring(7, 9)
            branchMap[branchCode]
        } else {
            null
        }
    }

    private fun fetchTimetablesForBranchAndSemester(branch: String, semester: String) {
        val dialogView = layoutInflater.inflate(R.layout.custom_progress_dialog, null)
        val progressDialog = AlertDialog.Builder(requireContext()).apply {
            setView(dialogView)
            setCancelable(false)
        }.create()

        progressDialog.show()

        val path = "timetables/$branch/$semester/"
        // Encoding spaces in path

        storageReference.child(path).listAll().addOnSuccessListener { listResult ->
            val timetables = mutableListOf<Pair<String, String>>()

            if (listResult.items.isEmpty()) {
                progressDialog.dismiss()
                Toast.makeText(
                    requireContext(),
                    "No timetables available for $semester",
                    Toast.LENGTH_SHORT
                ).show()
                timetableAdapter.updateData(emptyList())
                return@addOnSuccessListener
            }

            // Loop through all items and fetch download URLs
            listResult.items.forEach { item ->
                item.downloadUrl.addOnSuccessListener { uri ->
                    timetables.add(item.name to uri.toString())

                    // Once all items have been processed, update the adapter
                    if (timetables.size == listResult.items.size) {
                        progressDialog.dismiss()
                        timetableAdapter.updateData(timetables)
                    }
                }.addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Error: ${exception.message}",
                        Toast.LENGTH_SHORT
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

        // Monitoring the download status
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
                                    requireContext(), "Downloaded to ${
                                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
                                    }/$fileName", Toast.LENGTH_LONG
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
