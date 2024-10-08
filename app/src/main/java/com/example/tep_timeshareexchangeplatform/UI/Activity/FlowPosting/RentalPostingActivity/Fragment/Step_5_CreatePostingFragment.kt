package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ViewModel.RentalPostingViewModel
import com.example.tep_timeshareexchangeplatform.databinding.FragmentCreatePostingBinding

class Step_5_CreatePostingFragment : BaseFragment(R.layout.fragment_create_posting) {

    private lateinit var binding: FragmentCreatePostingBinding
    private val rentalPostingViewModel: RentalPostingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePostingBinding.inflate(inflater, container, false)
        binding.includeMyTimeshare.btnSelect.visibility = View.GONE
        observeViewModel()
        setEventChangeMyTimeshare()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun observeViewModel() {
        rentalPostingViewModel.myTimeshareModelSelected.observe(viewLifecycleOwner) { myTimeshareModel ->
            if (myTimeshareModel == null) {
                binding.includeMyTimeshare.root.visibility = View.GONE
                binding.ivAddMyTimeshare.visibility = View.VISIBLE
            } else {
                binding.includeMyTimeshare.root.visibility = View.VISIBLE
                binding.ivAddMyTimeshare.visibility = View.GONE
                binding.includeMyTimeshare.apply {
                    tvResortName.text = myTimeshareModel.name
                    tvRoomType.text = myTimeshareModel.roomName
                    tvCheckinDate.text = "${myTimeshareModel.checkInDate} - ${myTimeshareModel.checkOutDate}"
                    tvNumberOfNight.text =  " | ${myTimeshareModel.numberOfNight.toString()} đêm"
                    tvPrice.text = myTimeshareModel.price.toString()
                    Glide.with(binding.root.context).load(myTimeshareModel.image).into(imResortImage)
                }
            }

        }
    }

    private fun setEventChangeMyTimeshare() {
        binding.btnChangeMyTimeshare.setOnClickListener {
            rentalPostingViewModel.updateStep(3)
        }

    }

}