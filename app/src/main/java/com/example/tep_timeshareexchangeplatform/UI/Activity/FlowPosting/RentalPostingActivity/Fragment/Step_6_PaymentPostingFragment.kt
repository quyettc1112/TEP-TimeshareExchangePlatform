package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.MyTimeshareModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MemberShipActivity.Adapter.BenefitAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ViewModel.RentalPostingViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.MyPostingActivity
import com.example.tep_timeshareexchangeplatform.databinding.FragmentPaymentPostingBinding
import java.text.DecimalFormat


class Step_6_PaymentPostingFragment : BaseFragment(R.layout.fragment_payment_posting) {

    private lateinit var binding: FragmentPaymentPostingBinding
    private val rentalPostingViewModel: RentalPostingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentPostingBinding.inflate(layoutInflater, container, false)
        observeViewModel()
        setEventChangePackage()
        setEventDonePayment()

        return binding.root
    }

    // Observe ViewModel
    private fun observeViewModel() {
        rentalPostingViewModel.packageStep4.observe(viewLifecycleOwner) { packageModel ->
            if (packageModel != null) {
                bindDataPackagePosting(packageModel)
            }
        }

        rentalPostingViewModel.myTimeshareModelSelected.observe(viewLifecycleOwner) { myTimeshareModel ->
            rentalPostingViewModel.dateRange.observe(viewLifecycleOwner) { dateRange ->
                if (myTimeshareModel != null && dateRange != null) {
                    bindDataTimeshareInfo(myTimeshareModel, dateRange)
                }
            }
        }
    }

    // Funtion to Change Pakage
    private fun setEventChangePackage() {
        binding.btnChangeMyPackage.setOnClickListener {
            rentalPostingViewModel.updateStep(4)
        }
    }

    // Funtion to done Payment
    private fun setEventDonePayment() {
        binding.ctrRequestButton.setOnClickListener {
            // Nạp layout của dialog
            val inflater = LayoutInflater.from(requireContext())
            val dialogView = inflater.inflate(R.layout.dialog_success, null)

            // Tạo dialog với layout tuỳ chỉnh
            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            // Ánh xạ các view từ dialog
            val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)


            // Thiết lập sự kiện khi bấm nút "Xác nhận"
            btnConfirm.setOnClickListener {
                // Xử lý thanh toán
                dialog.dismiss() // Đóng dialog sau khi xử lý
                startActivity(Intent(requireContext(), MyPostingActivity::class.java))
                requireActivity().finish()
            }


            // Hiển thị dialog
            dialog.show()
        }

    }


    // Funtion to Bind data to UI
    private fun bindDataPackagePosting(packageModel: PackageModel) {
        var benefitAdapter = BenefitAdapter()
        benefitAdapter.submitList(packageModel.listBenefit)
        // Change Layout
        binding.includePackegePosting.clContainer.layoutParams.height =
            ViewGroup.LayoutParams.WRAP_CONTENT

        // Hide Unnecessary UI
        binding.includePackegePosting.tvTitle.visibility = View.GONE
        binding.includePackegePosting.tvPackageDescription.visibility = View.GONE

        binding.includePackegePosting.tvPackageName.text = packageModel.name
        binding.includePackegePosting.tvPackagePrice.text = "${formatPrice(packageModel.price)} VND"
        binding.includePackegePosting.tvPackageDescription.text = packageModel.description
        binding.includePackegePosting.rvFeatures.let {
            it.adapter = benefitAdapter
            it.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(it.context)
        }
    }

    fun formatPrice(price: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(price)
    }

    // Funtion to Bind Timeshare Info Data to UI
    private fun bindDataTimeshareInfo(
        myTimeshareModel: MyTimeshareModel,
        dateRange: Pair<Long?, Long?>
    ) {
        val startDate = dateRange.first ?: return
        val endDate = dateRange.second ?: return
        val totalDays = ((endDate - startDate) / (1000 * 60 * 60 * 24)).toInt() + 1
        binding.includeTimesharePosting.apply {
            // Hide Unnecessary UI
            llLocation.visibility = View.GONE

            // Image
            Glide.with(requireContext())
                .load(myTimeshareModel.image)
                .into(imImageTimeshare)

            // Title
            tvResortNameDtb.text = "${myTimeshareModel.name} | ${myTimeshareModel.roomName}"

            // Number of Night
            tvNumberNight.text = " ${totalDays} đêm"

            // Checkin Date
            tvCheckInDate.text = myTimeshareModel.checkInDate

            // Checkout Date
            tvCheckOutDate.text = myTimeshareModel.checkOutDate

            // Cancel Policy
            tvCancellationPolicy.text = "Không có"

            // Room Price Per Night
            tvRoomPricePerNight.text = "${myTimeshareModel.price} / 1 đêm"

            // Estimated Total Price
            tvEstimatedTotalPrice.text = "${myTimeshareModel.price} / 1 đêm"

            // User Image
            Glide.with(requireContext())
                .load(myTimeshareModel.image)
                .into(imUserImage)
            // User Name
            tvUserName.text = "Đăng tải bởi Trần Cuơng Quyết"


        }
    }


}