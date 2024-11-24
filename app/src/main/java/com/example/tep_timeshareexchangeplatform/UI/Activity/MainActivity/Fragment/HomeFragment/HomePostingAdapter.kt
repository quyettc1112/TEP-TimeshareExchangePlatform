package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.HomeFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.PublicPostingFragment.PublicPostingAdapterRV
import com.example.tep_timeshareexchangeplatform.databinding.ItemPostingBinding
import com.example.tep_timeshareexchangeplatform.databinding.ItemTimeshareVer1Binding
import java.text.DecimalFormat

class HomePostingAdapter : BaseAdapter<PublicPostingResponse.Content, HomePostingAdapter.HomePostingViewHolder>() {

    var onItemClick: ((PublicPostingResponse.Content) -> Unit)? = null
    var onFavoriteClick: ((PublicPostingResponse.Content) -> Unit)? = null

    inner class HomePostingViewHolder(binding: ItemTimeshareVer1Binding): BaseItemViewHolderCF<PublicPostingResponse.Content, ItemTimeshareVer1Binding>(binding) {
        override fun bind(item: PublicPostingResponse.Content) {
            Glide.with(binding.imImageTimeshare.context)
                .load(item.unitTypeDTO.photos)
                .error(R.drawable.ic_error_)
                .into(binding.imImageTimeshare)
            binding.tvResortName.text = item.resortName
            binding.tvLocation.text = item.address
            binding.tvCheckInDate.text = Constant.formatDateByLocale(item.checkinDate, binding.root.context)
            binding.tvCheckOutDate.text = Constant.formatDateByLocale(item.checkoutDate, binding.root.context)
            binding.llRatingContainer.visibility = View.GONE

            if (item.isVerify) {
                binding.llVerify.visibility = View.VISIBLE
            } else {
                binding.llVerify.visibility = View.GONE
            }


            binding.tvPrice.text = "${formatPriceLong(item.pricePerNights)} VND"

            binding.tvRoom.text = "${item.unitTypeDTO.title}, ${item.unitTypeDTO.bedrooms} phòng ngủ, ${item.unitTypeDTO.sleeps} người"
            binding.root.setOnClickListener {
                onItemClick?.let { it1 -> it1(item) }
            }

        }

        fun formatPrice(price: Int): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(price)
        }
        fun formatPriceLong(price: Long): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(price)
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<PublicPostingResponse.Content> {
        return object : DiffUtil.ItemCallback<PublicPostingResponse.Content>() {
            override fun areItemsTheSame(oldItem: PublicPostingResponse.Content, newItem: PublicPostingResponse.Content): Boolean {
                return oldItem.rentalPostingId == newItem.rentalPostingId
            }

            override fun areContentsTheSame(oldItem: PublicPostingResponse.Content, newItem: PublicPostingResponse.Content): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePostingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTimeshareVer1Binding.inflate(inflater, parent, false)
        return HomePostingViewHolder(binding)
    }

    fun clearData() {
        submitList(listOf())
    }
}