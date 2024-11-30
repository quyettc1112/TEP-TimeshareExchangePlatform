package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.NearbyBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map.OverpassResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MapViewActivity.Adapter.NearByAdapter
import com.example.tep_timeshareexchangeplatform.databinding.DialogNearbyLocationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NearByBottomSheet (
    private val listLocation : List<OverpassResponse.Element>
) : BottomSheetDialogFragment() {

    private var _binding: DialogNearbyLocationBinding? = null
    private val binding get() = _binding!!
    val nearByAdapter = NearByAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout using View Binding
        _binding = DialogNearbyLocationBinding.inflate(inflater, container, false)
        nearByAdapter.submitList(listLocation)
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setupRecyclerView() {
        binding.rvNearbyLocation.apply {
            adapter = nearByAdapter
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }

    override fun getTheme(): Int {
        return R.style.MyBottomSheetDialogTheme // Use custom theme
    }
}