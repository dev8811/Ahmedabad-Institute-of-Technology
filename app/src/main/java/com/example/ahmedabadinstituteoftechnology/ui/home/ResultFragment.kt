package com.example.ahmedabadinstituteoftechnology.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ahmedabadinstituteoftechnology.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)

        // Set up click listeners for the GTU RESULT IMG and CARD
        binding.GTURESULTCard.setOnClickListener {
            navigateToGtuResults()
        }

        binding.GTURESULTIMG.setOnClickListener {
            navigateToGtuResults()
        }

        return binding.root
    }

    private fun navigateToGtuResults() {
        val gtuUrl = "https://www.gturesults.in/"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(gtuUrl))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}
