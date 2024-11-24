package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.PublicPostingFragment

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
import com.example.tep_timeshareexchangeplatform.databinding.ItemPostingBinding
import com.example.tep_timeshareexchangeplatform.databinding.ItemTimeshareVer1Binding
import java.text.DecimalFormat

class PublicPostingAdapterRV : BaseAdapter<PublicPostingResponse.Content, PublicPostingAdapterRV.TimeshareAdapterRVViewHolder>() {
    var onItemClick: ((PublicPostingResponse.Content) -> Unit)? = null
    var onFavoriteClick: ((PublicPostingResponse.Content) -> Unit)? = null

    inner class TimeshareAdapterRVViewHolder(binding: ItemPostingBinding): BaseItemViewHolderCF<PublicPostingResponse.Content, ItemPostingBinding>(binding) {
        override fun bind(item: PublicPostingResponse.Content) {
            Glide.with(binding.imImageTimeshare.context)
                .load(item.unitTypeDTO.photos)
                .error(R.drawable.ic_image_tmp_holder)
                .into(binding.imImageTimeshare)
            binding.tvTimeshareName.text = item.resortName
            binding.tvLocation.text = item.address
            binding.tvCheckInDate.text = Constant.formatDateByLocale(item.checkinDate, binding.root.context)
            binding.tvCheckOutDate.text = Constant.formatDateByLocale(item.checkoutDate, binding.root.context)

            if (item.isVerify) {
                binding.llVerify.visibility = View.VISIBLE
            } else {
                binding.llVerify.visibility = View.GONE
            }




            binding.tvPrice.text = "${formatPrice(item.pricePerNights)} VNĐ"

            binding.tvRoom.text = "${item.unitTypeDTO.title}, ${item.unitTypeDTO.bedrooms} phòng ngủ, ${item.unitTypeDTO.sleeps} người"
            binding.root.setOnClickListener {
                onItemClick?.let { it1 -> it1(item) }
            }

        }

        fun formatPrice(price: Long): String {
            val formatter = DecimalFormat("#,###")
            val formattedPrice = formatter.format(price)

            // Nếu giá trị lớn hơn 100 triệu, thay đổi textSize
            if (price > 100_000_000) {
                binding.tvPrice.textSize = 12f // Thay đổi kích thước text thành 10dp
            } else {
                binding.tvPrice.textSize = 15f // Hoặc kích thước mặc định
            }

            return formattedPrice
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeshareAdapterRVViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostingBinding.inflate(inflater, parent, false)
        return TimeshareAdapterRVViewHolder(binding)
    }

    fun clearData() {
        submitList(listOf())
    }

}