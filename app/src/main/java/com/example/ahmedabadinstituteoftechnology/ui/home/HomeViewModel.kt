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
        // Query the Profile subcollection for the first document
        FirebaseFirestore.getInstance()
            .collection("Student")
            .document(enrollmentNumber)
            .collection("Profile")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Get the first document from the Profile subcollection
                    val firstDocument = querySnapshot.documents.firstOrNull()
                    if (firstDocument != null) {
                        // Map the document data to the Student class
                        val student = firstDocument.toObject(Student::class.java)
                        _studentData.postValue(student)
                    } else {
                        _error.postValue("No student data found in Profile subcollection")
                    }
                } else {
                    _error.postValue("Profile subcollection is empty")
                }
            }
            .addOnFailureListener { exception ->
                _error.postValue("Error: ${exception.message}")
            }
    }
}
