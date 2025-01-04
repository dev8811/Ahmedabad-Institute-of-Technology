package com.example.ahmedabadinstituteoftechnology.ui.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val firestore = FirebaseFirestore.getInstance()

    private val _profileData = MutableLiveData<ProfileData>()
    val profileData: LiveData<ProfileData> get() = _profileData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun setEnrollmentNumber(enrollmentNumber: String?) {
        if (enrollmentNumber.isNullOrEmpty()) {
            _error.value = "Error: Enrollment number not found"
            return
        }
        fetchProfileData(enrollmentNumber)
    }

    private fun fetchProfileData(enrollmentNumber: String) {
        viewModelScope.launch {
            val studentRef = firestore.collection("Student").document(enrollmentNumber)

            studentRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name")
                        val email = document.getString("Email")
                        val semester = document.getString("semester")
                        val enrollment = document.getString("enrollmentNo")
                        val DOB = document.getString("DOB")
                        val contactNumber = document.getString("contact_number")
                        val parent_Number = document.getString("parent_Number")
                        val branch = document.getString("branch")
                        val abc_id = document.getString("ABCID")

                        _profileData.value = ProfileData(
                            name = name ?: "N/A",
                            email = email ?: "N/A",
                            semester = semester ?: "N/A",
                            enrollment = enrollment ?: "N/A",
                            dateOfBirth = DOB ?: "N/A",
                            contactNumber = contactNumber ?: "N/A",
                            parent_Number = parent_Number ?: "N/A",
                            branch = branch ?: "N/A",
                            abc_id = abc_id ?: "N/A",
                        )
                    } else {
                        _error.value = "No such document"
                    }
                }
                .addOnFailureListener { exception ->
                    _error.value = "Error: ${exception.message}"
                }
        }
    }
}
data class ProfileData(
    val name: String,
    val email: String,
    val semester: String,
    val enrollment: String,
    val dateOfBirth: String,
    val contactNumber: String,
    val parent_Number: String,
    val branch: String,
    val abc_id: String,

)
