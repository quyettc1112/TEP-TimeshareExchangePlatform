package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.ResortDetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.RoomSelectionDialog.UnitTypeDataDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortDetailModelResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeBase
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImagePostingAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Common.Adapter.SpannedGridLayoutManager.SpannedGridLayoutManager
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.FeedbackListActivity.FeedbackListActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MapViewActivity.MapViewActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.ResortAmenityAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.ReviewAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.UnitTypeResortAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.PostingOfResortListActivity.PostingOfResortActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.ActivityResortDetailBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class ResortDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityResortDetailBinding
    private var imagePostingAdapter = ImagePostingAdapter()
    private var unitTypeAdapter = UnitTypeResortAdapter()
    private var amenitiesAdapter = AmenitiesAdapter()
    private var reviewAdapter = ReviewAdapter()
    private val resortDetailViewModel: ResortDetailViewModel by viewModels()

    private var featuresAdapter = ResortAmenityAdapter()
    private var policyAdapter = ResortAmenityAdapter()
    private var nearbyAdapter = ResortAmenityAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResortDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val resortId = intent.getIntExtra(Constant.DEFAULT_RESORT_ID, 0)
        if (resortId != 0) {
            resortDetailViewModel.getResortDetail(resortId)
        } else {
            finish()
        }

        // Init Adapter
        initAdapter()

        eventClickViewAllFeedback()
        eventClickBack()
        eventClickShowMaps()

        // Observe Data
        observeData()
    }

    private fun observeData() {
        resortDetailViewModel.resortDetail.observe(this) { resources ->
            when (resources.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    resources.data?.let { resortDetail ->
                        // Set Resort Detail Info
                        bindDataResortInfo(resortDetailViewModel.resortDetail.value?.data!!)

                        bindDataListImage(resortDetail.imageUrls)

                        bindDataUnitType(resortDetail.unitTypeDtoList)
                        bindDataReviewResort(resortDetail.feedbackList)
                        bindDataAmenities(resortDetail)

                        // Action Event
                        setTypeRoomClickAction()
                        setButtonSelectRoomClick()
                    }
                    hideLoadingWaiting()
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorDialog(resources.message.toString(), "")

                }
            }
        }
    }

    private fun initAdapter() {
        unitTypeAdapter.submitList(listOf())
        amenitiesAdapter.submitList(listOf())
        reviewAdapter.submitList(listOf())
    }

    private fun setTypeRoomClickAction() {
        unitTypeAdapter.apply {
            onItemClick = {
                val unitTypeBase = mapToUnitTypeBase(it)
                val unitTypeDataDialog = UnitTypeDataDialog.newInstance(unitTypeBase)
                unitTypeDataDialog.show(supportFragmentManager, "UnitTypeDataDialog")
            }

            onViewDetailClick = {
                val unitTypeBase = mapToUnitTypeBase(it)
                val unitTypeDataDialog = UnitTypeDataDialog.newInstance(unitTypeBase)
                unitTypeDataDialog.show(supportFragmentManager, "UnitTypeDataDialog")
            }
        }


    }

    private fun setButtonSelectRoomClick() {
        binding.btnSelectRoom.setOnClickListener {
            val intent = Intent(this, PostingOfResortActivity::class.java)
            intent.putExtra(
                Constant.RESORT_NAME,
                resortDetailViewModel.resortDetail.value?.data?.resortName
            )
            intent.putExtra(Constant.RESORT_ID, resortDetailViewModel.resortDetail.value?.data?.id)
            startActivity(intent)
        }

    }

    private fun eventClickViewAllFeedback() {
        binding.tvSeeMoreReview.setOnClickListener {
            val intent = Intent(this, FeedbackListActivity::class.java)
            intent.putExtra(
                Constant.DEFAULT_RESORT_ID,
                resortDetailViewModel.resortDetail.value?.data?.id
            )
            intent.putExtra(
                Constant.AVG_RATING,
                resortDetailViewModel.resortDetail.value?.data?.averageRating
            )
            intent.putExtra(
                Constant.TOTAL_RATING,
                resortDetailViewModel.resortDetail.value?.data?.totalRating
            )
            startActivity(intent)
        }
        binding.tvSeeMoreReviewTop.setOnClickListener {
            val intent = Intent(this, FeedbackListActivity::class.java)
            intent.putExtra(
                Constant.DEFAULT_RESORT_ID,
                resortDetailViewModel.resortDetail.value?.data?.id
            )
            intent.putExtra(
                Constant.AVG_RATING,
                resortDetailViewModel.resortDetail.value?.data?.averageRating
            )
            intent.putExtra(
                Constant.TOTAL_RATING,
                resortDetailViewModel.resortDetail.value?.data?.totalRating
            )
            startActivity(intent)
        }
    }

    private fun eventClickBack() {
        binding.cvBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun eventClickShowMaps() {
        binding.llSeeMap.setOnClickListener {
            val intent = Intent(this, MapViewActivity::class.java)
            intent.putExtra(Constant.RESORT_LATITUDE, resortDetailViewModel.resortDetail.value?.data?.location?.latitude)
            intent.putExtra(Constant.RESORT_LONGITUDE, resortDetailViewModel.resortDetail.value?.data?.location?.latitude)
            startActivity(intent)
        }
    }

    // Binding Data Group Function
    private fun bindDataResortInfo(resortDetailModelResponse: ResortDetailModelResponse) {
        binding.apply {
            tvResortName.text = resortDetailViewModel.resortDetail.value?.data?.resortName
            tvLocation.text = resortDetailViewModel.resortDetail.value?.data?.location?.displayName ?: "Không Có Dữ Liệu"
            tvMinPrice.text =
                "${formatPrice(resortDetailViewModel.resortDetail.value?.data?.minPrice!!)} VND / 1 đêm"
            tvDescription.text =
                resortDetailViewModel.resortDetail.value?.data?.description.toString()
            if (resortDetailModelResponse.isActive) {
                llVerify.visibility = View.VISIBLE
            } else {
                llVerify.visibility = View.GONE
            }

            tvAvgRating.text = resortDetailModelResponse.averageRating.toString()
            tvTotalRating.text = resortDetailModelResponse.totalRating.toString() + " đánh giá"
            tvAvgRatingTop.text = resortDetailModelResponse.averageRating.toString()
            tvRatingCountTop.text = resortDetailModelResponse.totalRating.toString() + " đánh giá"
        }
    }

    private fun bindDataListImage(imageList: List<String>) {
        // List Destination

        val manager = SpannedGridLayoutManager(
            object : SpannedGridLayoutManager.GridSpanLookup {
                override fun getSpanInfo(position: Int): SpannedGridLayoutManager.SpanInfo {
                    // Conditions for 2x2 items
                    return when (position) {
                        0 -> SpannedGridLayoutManager.SpanInfo(2, 2)
                        1 -> SpannedGridLayoutManager.SpanInfo(2, 2)
                        2 -> SpannedGridLayoutManager.SpanInfo(1, 1)
                        3 -> SpannedGridLayoutManager.SpanInfo(1, 1)
                        4 -> SpannedGridLayoutManager.SpanInfo(1, 1)
                        5 -> SpannedGridLayoutManager.SpanInfo(1, 1)
                        else -> {
                            SpannedGridLayoutManager.SpanInfo(1, 1)
                        }
                    }
                }
            },
            4,  // number of columns
            1f // how big is default item
        )

        imagePostingAdapter.submitList(imageList)
        if (imageList.size == 1) {
            val layoutManagerCheck =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewResortImage.apply {
                adapter = imagePostingAdapter
                layoutManager = layoutManagerCheck
            }
        } else {
            binding.recyclerViewResortImage.apply {
                adapter = imagePostingAdapter
                layoutManager = manager
            }
        }

        imagePostingAdapter.onItemClickListener = { position ->
            val intent = Intent(this@ResortDetailActivity, ImageListActivity::class.java)
            intent.putExtra(Constant.IMAGE_POSITION, position)
            intent.putStringArrayListExtra(
                Constant.IMAGE_LIST,
                ArrayList(imageList)
            )
            startActivity(intent)
        }

    }

    private fun bindDataUnitType(resorts: List<ResortDetailModelResponse.UnitTypeDto>) {
        unitTypeAdapter.submitList(resorts)
        binding.rvResortRoomType.apply {
            adapter = unitTypeAdapter
            layoutManager =
                LinearLayoutManager(this@ResortDetailActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun bindDataAmenities(data: ResortDetailModelResponse) {
        featuresAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.resortAmenityList))
        policyAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.resortAmenityList))
        nearbyAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.resortAmenityList))

        val binding = binding.includeAmenities

        binding.title3.visibility = View.GONE
        binding.rvKitchen.visibility = View.GONE

        binding.title1.text = "Tiện Nghi"
        binding.title2.text = "Điểm Tham Quan Gần Đây"
        binding.title4.text = "Chính Sách"


        binding.rvFeatures.apply {
            featuresAdapter.filterByAmenityTypes(AmenityType.AMENITIES)
            Log.d("Checklasasdasda", "bindDataAmenities: ${featuresAdapter.differ.currentList}")
            layoutManager = FlexboxLayoutManager(this@ResortDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = featuresAdapter
        }

        binding.rvAmenitiesEntertainment.apply {
            nearbyAdapter.filterByAmenityTypes(AmenityType.NEARBY_ATTRACTIONS)
            layoutManager = FlexboxLayoutManager(this@ResortDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP            // Cho phép các mục xuống dòng nếu không đủ chỗ
            }
            adapter = nearbyAdapter
        }


        binding.rvPolicy.apply {
            policyAdapter.filterByAmenityTypes(AmenityType.POLICY)
            layoutManager = FlexboxLayoutManager(this@ResortDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = policyAdapter
        }
    }

    private fun bindDataReviewResort(listReview: List<ResortDetailModelResponse.Feedback>) {
        reviewAdapter.submitList(listReview)
        binding.rvReview.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(this@ResortDetailActivity)
        }
    }


    private fun mapToUnitTypeBase(unitTypeDto: ResortDetailModelResponse.UnitTypeDto): UnitTypeBase {
        return UnitTypeBase(
            id = unitTypeDto.id,
            title = unitTypeDto.title,
            area = unitTypeDto.area,
            bathrooms = unitTypeDto.bathrooms,
            bedrooms = unitTypeDto.bedrooms,
            bedsFull = unitTypeDto.bedsFull,
            bedsKing = unitTypeDto.bedsKing,
            bedsSofa = unitTypeDto.bedsSofa,
            bedsMurphy = unitTypeDto.bedsMurphy,
            bedsQueen = unitTypeDto.bedsQueen,
            bedsTwin = unitTypeDto.bedsTwin,
            buildingsOption = unitTypeDto.buildingsOption,
            price = unitTypeDto.price,
            description = unitTypeDto.description,
            kitchen = unitTypeDto.kitchen,
            photos = unitTypeDto.photos,
            resortId = unitTypeDto.resortId,
            sleeps = unitTypeDto.sleeps,
            view = unitTypeDto.view,
            isActive = unitTypeDto.isActive,
            unitTypeAmenitiesDTOS = unitTypeDto.unitTypeAmenitiesList.map { amenity ->
                UnitTypeBase.UnitTypeAmenitiesDTOS(
                    name = amenity?.name ?: "",
                    type = amenity?.type,
                    isActive = amenity?.isActive
                )
            }
        )
    }

    private fun mapRoomAmenitiesToAmenitiesModel(roomAmenities: List<ResortDetailModelResponse.ResortAmenity>): List<AmenitiesModel> {
        return roomAmenities.map { roomAmenity ->
            AmenitiesModel(
                name = roomAmenity.name,
                type = roomAmenity.type,
                isChecked = roomAmenity.free
            )
        }
    }

    private fun formatPrice(price: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(price)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}