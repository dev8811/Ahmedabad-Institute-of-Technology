package com.example.ahmedabadinstituteoftechnology.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel() {

    private val _studentData = MutableLiveData<Student?>()
    val studentData: LiveData<Student> get() = _studentData as LiveData<Student>

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun setEnrollmentNumber(enrollmentNumber: String) {
        fetchStudentData(enrollmentNumber)
    }

    private fun fetchStudentData(enrollmentNumber: String) {
        FirebaseFirestore.getInstance().collection("Student")
            .document(enrollmentNumber)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val student = document.toObject(Student::class.java)
                    _studentData.postValue(student)
                } else {
                    _error.postValue("Student data not found")
                }
            }
            .addOnFailureListener { exception ->
                _error.postValue("Error: ${exception.message}")
            }
    }
}


