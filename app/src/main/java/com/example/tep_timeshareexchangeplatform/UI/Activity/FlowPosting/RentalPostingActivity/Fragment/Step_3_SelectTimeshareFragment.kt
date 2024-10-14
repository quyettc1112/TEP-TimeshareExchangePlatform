package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.MyTimeshareModel
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.MyTimeshareDetailAcitivity.MyTimeshareDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Adapter.MyTimeshareAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.RentalPostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ViewModel.RentalPostingViewModel
import com.example.tep_timeshareexchangeplatform.databinding.FragmentSelectTimeshareBinding

class Step_3_SelectTimeshareFragment : BaseFragment(R.layout.fragment_select_timeshare) {

    private lateinit var binding: FragmentSelectTimeshareBinding
    private var myTimeshareAdapter = MyTimeshareAdapter()
    private val rentalPostingViewModel: RentalPostingViewModel by activityViewModels()
    private lateinit var selectMyTimeshareResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectTimeshareBinding.inflate(layoutInflater, container, false)
        initRecyclerView()
        initActivityResultLauncher()
        setEventItemClick()
        return binding.root
    }

    private fun initAdapter() {
        myTimeshareAdapter.submitList(Constant.listMyTimeShare)
    }

    private fun initRecyclerView() {
        // Set adapter for recyclerview
        binding.recyclerView.let {
            it.adapter = myTimeshareAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setEventItemClick(){
        // Item click
        myTimeshareAdapter.setItemOnclickListener {
            val intent = Intent(requireContext(), MyTimeshareDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_SELECTION_MY_TIMESHARE, it)
            selectMyTimeshareResultLauncher.launch(intent)
        }

        // Select button click
        myTimeshareAdapter.onItemClick = {
            (activity as RentalPostingActivity).showConfirmDialog(
                title = "Confirm",
                message = "Are you sure you want to select this Timeshare?",
                positiveButtonTitle = "Yes",
                negativeButtonTitle = "No",
                textButton = null,
                object : ConfirmDialog.ConfirmCallback {
                    override fun negativeAction() {

                    }
                    override fun positiveAction() {
                        rentalPostingViewModel.updateMyTimeshareModel(it)
                        Toast.makeText(requireContext(), "Selected", Toast.LENGTH_SHORT).show()
                        rentalPostingViewModel.updateStep(4)
                    }
                }
            )
        }
    }

    private fun initActivityResultLauncher(){
        selectMyTimeshareResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedMyTimeshare: MyTimeshareModel? = data?.getParcelableExtra(Constant.DEFAULT_SELECTION_MY_TIMESHARE)
                if (selectedMyTimeshare != null) {
                    rentalPostingViewModel.updateMyTimeshareModel(selectedMyTimeshare)
                    Toast.makeText(requireContext(), "Selected", Toast.LENGTH_SHORT).show()
                    rentalPostingViewModel.updateStep(4)
                }
            }
        }
    }


}