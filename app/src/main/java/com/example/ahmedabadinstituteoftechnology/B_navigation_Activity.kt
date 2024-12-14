package com.example.ahmedabadinstituteoftechnology

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ahmedabadinstituteoftechnology.databinding.ActivityBnavigationBinding

class B_navigation_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityBnavigationBinding
    private var isNotificationsNavigated = false // Flag to prevent infinite loop

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up view binding
        binding = ActivityBnavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        // Retrieve enrollment number from Intent
        val enrollmentNumber = intent.getStringExtra("enrollment_number")

        val navController: NavController = findNavController(R.id.nav_host_fragment_activity_bnavigation)

        // Pass enrollment number as arguments to the notifications destination
        val bundle = Bundle()
        bundle.putString("enrollment_number", enrollmentNumber)

        // Set a listener to navigate to notifications only once
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_notifications && !isNotificationsNavigated) {
                isNotificationsNavigated = true // Set flag to true after first navigation
                navController.navigate(R.id.navigation_notifications, bundle)
            }
        }

        // Set up navigation
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
       // setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
