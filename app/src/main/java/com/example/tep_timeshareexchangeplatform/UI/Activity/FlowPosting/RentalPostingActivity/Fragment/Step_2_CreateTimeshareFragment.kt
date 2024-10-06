package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ImageUploadModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.LocationModel
import com.example.tep_timeshareexchangeplatform.Common.Adapter.SpannedGridLayoutManager.SpannedGridLayoutManager
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.LocationActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Adapter.AmenitiesAdaper
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Adapter.ImageUploadAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ViewModel.RentalPostingViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.RoomTypeAdapter
import com.example.tep_timeshareexchangeplatform.databinding.FragmentCreateTimeshareBinding


class Step_2_CreateTimeshareFragment : BaseFragment(R.layout.fragment_create_timeshare) {

    private lateinit var binding: FragmentCreateTimeshareBinding
    private lateinit var locationResultLauncher: ActivityResultLauncher<Intent>
    private var roomTypeAdapter = RoomTypeAdapter(false)
    private var amenitiesAdapter = AmenitiesAdaper()
    private var amenitiesEntertamentAdapter = AmenitiesAdaper()
    private var imageUploadAdapter = ImageUploadAdapter()
    private var policyAmentitiesAdapter = AmenitiesAdaper()
    private val rentalPostingViewModel: RentalPostingViewModel by activityViewModels()
    private var isUnitTypeExpanded = false
    private var isDayCheckInExpanded = false
    private var isAmenitiesExpanded = false
    private var isImageInfoExpanded = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTimeshareBinding.inflate(inflater, container, false)
        initActivityLauncher()
        observeViewModel()
        setEventChangeLocation()
        setValueUnitRoom()
        expanedViewHandle()
        getImageFromGallery()


        return binding.root
    }
    private fun initAdapter() {
        roomTypeAdapter.submitList(Constant.listRoomType)
        amenitiesAdapter.submitList(Constant.listAmenities)
        amenitiesEntertamentAdapter.submitList(Constant.listEntertament)
        policyAmentitiesAdapter.submitList(Constant.listPolicy)
        imageUploadAdapter.submitList(listOf())
        imageUploadAdapter.onDeleteClick = {
            imageUploadAdapter.removeItem(it)
        }

    }

    // Click to Open or Close View
    private fun expanedViewHandle() {
        binding.crlUnitTypeInfo.setOnClickListener {
            isUnitTypeExpanded = !isUnitTypeExpanded
            handleViewVisibility(binding.llContentUnitType, isUnitTypeExpanded)
        }
        binding.crlDayCheckInInfo.setOnClickListener {
            isDayCheckInExpanded = !isDayCheckInExpanded
            handleViewVisibility(binding.llCheckInCheckOut, isDayCheckInExpanded)
        }
        binding.crlAmenitiesInfo.setOnClickListener {
            isAmenitiesExpanded = !isAmenitiesExpanded
            handleViewVisibility(binding.llContentAmenities, isAmenitiesExpanded)
        }
        binding.crlImageInfo.setOnClickListener {
            isImageInfoExpanded = !isImageInfoExpanded
            handleViewVisibility(binding.llImage, isImageInfoExpanded)
        }

    }
    private fun handleViewVisibility(view: View, isExpanded: Boolean) {
        view.visibility = if (isExpanded) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    // Observe Location Model
    private fun observeViewModel() {
        // Bind Data of Location Model
        rentalPostingViewModel.locationModel.observe(viewLifecycleOwner) { locationModel ->
            if (locationModel != null) {
                binding.let {
                    it.tvResortName.text = locationModel.name
                    it.tvLocation.text = locationModel.location
                    it.ivResortImage.setImageResource(locationModel.image)
                    binding.llResortLocation.visibility = View.VISIBLE
                    binding.btnSelectResortLocation.visibility = View.GONE
                }
            } else {
                binding.llResortLocation.visibility = View.GONE
                binding.btnSelectResortLocation.visibility = View.VISIBLE
            }
        }
    }

    // User click to change location of Resort
    private fun setEventChangeLocation() {
        binding.tvChangeLocation.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            intent.putExtras(Bundle().apply {
                putString(Constant.DEFAULT_SELECTION_LOCATION_KEY_POSTING_FLOW, "getResortLocation")
            })
            locationResultLauncher.launch(intent)
        }
        binding.btnSelectResortLocation.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            intent.putExtras(Bundle().apply {
                putString(Constant.DEFAULT_SELECTION_LOCATION_KEY_POSTING_FLOW, "getResortLocation")
            })
            locationResultLauncher.launch(intent)
        }
    }

    // Set Value for of Resort Location
    private fun setValueUnitRoom() {
        // Set Spinner
        var adapterSpiner = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_items, android.R.layout.simple_spinner_item
        )
        adapterSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.customSpinnerBed.adapter = adapterSpiner
        binding.customSpinnerViewDiretion.adapter = adapterSpiner

        // Set Unit Type
        binding.rvBedType.apply {
            adapter = roomTypeAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        // Set List Amenities
        binding.rvAmenities.apply {
            adapter = amenitiesAdapter
            layoutManager = GridLayoutManager(context,2,  GridLayoutManager.VERTICAL, false)
        }

        // Set List Amenities Entertament
        binding.rvAmenitiesEntertainment.apply {
            adapter = amenitiesEntertamentAdapter
            layoutManager = GridLayoutManager(context,2,  GridLayoutManager.VERTICAL, false)
        }

        // Set List Policy
        binding.rvPolicy.apply {
            adapter = policyAmentitiesAdapter
            layoutManager = GridLayoutManager(context,2,  GridLayoutManager.VERTICAL, false)
        }

        // Set List Image
        binding.rvImage.apply {
            adapter = imageUploadAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }


    }

    // Get Image Form Gallery
    private fun getImageFromGallery() {
        binding.btnAddImage.setOnClickListener {
            openGallery()
        }
    }

    private fun initActivityLauncher() {
        locationResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val selectedLocation: LocationModel? = data?.getParcelableExtra(Constant.DEFAULT_SELECTION_LOCATION_KEY_POSTING_FLOW)
                    selectedLocation?.let {
                        rentalPostingViewModel.updateLocationModel(it)
                    }
                }
            }

    }
    private val pickImagesLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (uris.isNotEmpty()) {
            val listImage = mutableListOf<ImageUploadModel>()
            for (uri in uris) {
                listImage.add(ImageUploadModel.create(uri))
            }
            imageUploadAdapter.addImage(listImage)
        }
    }
    fun openGallery() {
        pickImagesLauncher.launch("image/*")
    }


}