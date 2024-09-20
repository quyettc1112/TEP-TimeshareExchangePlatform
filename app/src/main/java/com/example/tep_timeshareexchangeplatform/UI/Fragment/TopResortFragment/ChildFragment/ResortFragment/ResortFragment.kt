package com.example.tep_timeshareexchangeplatform.UI.Fragment.TopResortFragment.ChildFragment.ResortFragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tep_timeshareexchangeplatform.R

class ResortFragment : Fragment() {

    companion object {
        fun newInstance() = ResortFragment()
    }

    private val viewModel: ResortViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_resort, container, false)
    }
}