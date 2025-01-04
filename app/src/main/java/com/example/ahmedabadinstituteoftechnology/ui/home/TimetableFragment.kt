package com.example.ahmedabadinstituteoftechnology.ui.home

import android.app.ProgressDialog
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
import com.example.ahmedabadinstituteoftechnology.databinding.FragmentTimetableBinding
import com.example.ahmedabadinstituteoftechnology.ui.home.adapter.TimetableAdapter
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream

class TimetableFragment : Fragment() {

    private var _binding: FragmentTimetableBinding? = null
    private val binding get() = _binding!!
    private val timetableAdapter = TimetableAdapter { fileName, downloadUrl -> downloadPDF(fileName, downloadUrl) }
    private val storageReference = FirebaseStorage.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimetableBinding.inflate(inflater, container, false)

        // Set up RecyclerView
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = timetableAdapter
        }

        // Set up semester Spinner
        val semesters = listOf("Select Semester", "Semester 1", "Semester 2", "Semester 3", "Semester 4")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, semesters)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.semesterSpinner.adapter = spinnerAdapter

        binding.semesterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    val selectedSemester = semesters[position]
                    fetchTimetablesForSemester(selectedSemester)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        return binding.root
    }

    private fun fetchTimetablesForSemester(semester: String) {
        val progressDialog = ProgressDialog(requireContext()).apply {
            setTitle("Fetching Timetables")
            setMessage("Please wait...")
            setCancelable(false)
            show()
        }

        storageReference.child("timetables/$semester").listAll()
            .addOnSuccessListener { listResult ->
                val timetables = mutableListOf<Pair<String, String>>()
                val tasks = listResult.items.map { item ->
                    item.downloadUrl.addOnSuccessListener { uri ->
                        timetables.add(item.name to uri.toString())
                        if (timetables.size == listResult.items.size) {
                            progressDialog.dismiss()
                            timetableAdapter.updateData(timetables)
                        }
                    }
                }

                // Handle any errors
                tasks.forEach { task ->
                    task.addOnFailureListener { exception ->
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun downloadPDF(fileName: String, downloadUrl: String) {
        val progressDialog = ProgressDialog(requireContext()).apply {
            setTitle("Downloading $fileName")
            setMessage("Please wait...")
            setCancelable(false)
            show()
        }

        storageReference.child("timetables/$fileName").getBytes(Long.MAX_VALUE)
            .addOnSuccessListener { bytes ->
                progressDialog.dismiss()

                try {
                    val downloadDir =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file = File(downloadDir, fileName)

                    FileOutputStream(file).use { output ->
                        output.write(bytes)
                    }

                    Toast.makeText(requireContext(), "Downloaded to ${file.absolutePath}", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Download failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
