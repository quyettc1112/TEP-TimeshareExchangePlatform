package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.LocationActivity
import com.example.tep_timeshareexchangeplatform.databinding.FragmentCreateTimeshareBinding

class CreateTimeshareFragment : BaseFragment(R.layout.fragment_create_timeshare) {

    private lateinit var binding: FragmentCreateTimeshareBinding

    private lateinit var locationResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTimeshareBinding.inflate(inflater, container, false)

        initActivityLauncher()

        binding.btnTest.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            intent.putExtras(Bundle().apply {
                putString(Constant.DEFAULT_SELECTION_LOCATION_KEY_POSTING_FLOW, "getResortLocation")
            })
            locationResultLauncher.launch(intent)
        }



        return binding.root
    }

    private fun initActivityLauncher() {
        locationResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val selectedLocation = data?.getStringExtra(Constant.DEFAULT_SELECTION_LOCATION_KEY_POSTING_FLOW)
                    selectedLocation?.let {
                        binding.btnTest.text = selectedLocation
                    }
                }
            }

    }


}