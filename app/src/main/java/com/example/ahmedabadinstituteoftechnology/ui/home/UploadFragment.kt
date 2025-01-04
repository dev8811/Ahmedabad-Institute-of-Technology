package com.example.ahmedabadinstituteoftechnology.ui.home

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.ahmedabadinstituteoftechnology.databinding.FragmentMyCourseBinding
import com.google.firebase.storage.FirebaseStorage

class UploadFragment : Fragment() {

    private var _binding: FragmentMyCourseBinding? = null
    private val binding get() = _binding!!
    private val storageRef by lazy { FirebaseStorage.getInstance().reference }
    private val semesters = listOf("Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6")
    private var selectedSemester: String = ""

    private val pdfPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { uploadPdfToFirebase(it, selectedSemester) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMyCourseBinding.inflate(inflater, container, false)

        setupSemesterSpinner()
        setupUploadButton()

        return binding.root
    }

    private fun setupSemesterSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, semesters)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSemesters.adapter = adapter

        binding.spinnerSemesters.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSemester = semesters[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedSemester = ""
            }
        }
    }

    private fun setupUploadButton() {
        binding.btnUpload.setOnClickListener {
            if (selectedSemester.isEmpty()) {
                Toast.makeText(requireContext(), "Please select a semester!", Toast.LENGTH_SHORT).show()
            } else {
                pdfPicker.launch("application/pdf")
            }
        }
    }

    private fun uploadPdfToFirebase(uri: Uri, semester: String) {
        val progressDialog = ProgressDialog(requireContext()).apply {
            setTitle("Uploading Timetable")
            setMessage("Please wait...")
            setCancelable(false)
            show()
        }

        val fileName = "${System.currentTimeMillis()}.pdf"
        val pdfRef = storageRef.child("timetables/$semester/$fileName")

        pdfRef.putFile(uri)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "PDF uploaded successfully!", Toast.LENGTH_SHORT).show()
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
