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
            val profileCollectionRef = firestore.collection("Student").document(enrollmentNumber).collection("Profile")

            profileCollectionRef.get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        // Assuming you only want to fetch the first document in the Profile collection
                        val document = querySnapshot.documents.firstOrNull()
                        document?.let {
                            val name = it.getString("Name")
                            val email = it.getString("Email")
                            val semester = it.getString("semester")
                            val enrollment = it.getString("enrollmentNo")
                            val DOB = it.getString("DOB")
                            val contactNumber = it.getString("contact_number")
                            val parentNumber = it.getString("parent_Number")
                            val branch = it.getString("branch")
                            val abcId = it.getString("ABCID")

                            _profileData.value = ProfileData(
                                name = name ?: "N/A",
                                email = email ?: "N/A",
                                semester = semester ?: "N/A",
                                enrollment = enrollment ?: "N/A",
                                dateOfBirth = DOB ?: "N/A",
                                contactNumber = contactNumber ?: "N/A",
                                parentNumber = parentNumber ?: "N/A",
                                branch = branch ?: "N/A",
                                abcId = abcId ?: "N/A"
                            )
                        } ?: run {
                            _error.value = "No document found in Profile collection"
                        }
                    } else {
                        _error.value = "Profile collection is empty"
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
    val parentNumber: String,
    val branch: String,
    val abcId: String
)
