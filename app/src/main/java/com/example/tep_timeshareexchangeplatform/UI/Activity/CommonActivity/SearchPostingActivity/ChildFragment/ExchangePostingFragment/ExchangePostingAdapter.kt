package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.ExchangePostingFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.ExchangesResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ItemPostingBinding
import com.example.tep_timeshareexchangeplatform.databinding.ItemTimeshareVer1Binding
import java.text.DecimalFormat

class ExchangePostingAdapter(tokenManager: TokenManager) :
    BaseAdapter<ExchangesResponse.Content, ExchangePostingAdapter.ExchangePostingViewHolder>() {

    private val tokenManager = tokenManager
    var onItemClick: ((ExchangesResponse.Content) -> Unit)? = null
    var onFavoriteClick: ((ExchangesResponse.Content) -> Unit)? = null
    var onExchangeButtonClick: ((ExchangesResponse.Content) -> Unit)? = null

    inner class ExchangePostingViewHolder(binding: ItemPostingBinding) :
        BaseItemViewHolderCF<ExchangesResponse.Content, ItemPostingBinding>(binding) {
        override fun bind(item: ExchangesResponse.Content) {
            Glide.with(binding.imImageTimeshare.context)
                .load(item.unitTypeDTO.photos)
                .error(R.drawable.ic_error_)
                .into(binding.imImageTimeshare)
            binding.tvTimeshareName.text = item.resortName
            binding.tvLocation.text = item.resortLocationDisplayName
            binding.tvCheckInDate.text =
                Constant.formatDateByLocale(item.checkinDate, binding.root.context)
            binding.tvCheckOutDate.text =
                Constant.formatDateByLocale(item.checkoutDate, binding.root.context)
            binding.tvNights.text = "${item.nights} đêm"
            binding.tvPrice.visibility = View.GONE
            binding.tvRoom.text =
                "${item.unitTypeDTO.title}, ${item.unitTypeDTO.bedrooms} phòng ngủ, ${item.unitTypeDTO.sleeps} người"
            binding.root.setOnClickListener {
                onItemClick?.let { it1 -> it1(item) }
            }
            binding.llRatingContainer.visibility = View.GONE
            binding.tvPrice.visibility = View.GONE
            binding.btnExchange.visibility = View.VISIBLE
            binding.llVerify.visibility = if (item.isVerify) View.VISIBLE else View.GONE
            val customerInfo = tokenManager.getProfileInfo()
            if (item.ownerId == customerInfo?.id) {
                binding.btnExchange.apply {
                    backgroundTintList = resources.getColorStateList(R.color.green_verify)
                    text = "Bài Đăng Của Bạn"
                    textSize = 10f
                }
            } else {
                binding.btnExchange.setOnClickListener {
                    onExchangeButtonClick?.let { it1 -> it1(item) }
                }
            }

        }

        fun formatPrice(price: Int): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(price)
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<ExchangesResponse.Content> {
        return object : DiffUtil.ItemCallback<ExchangesResponse.Content>() {
            override fun areItemsTheSame(
                oldItem: ExchangesResponse.Content,
                newItem: ExchangesResponse.Content
            ): Boolean {
                return oldItem.exchangePostingId == newItem.exchangePostingId
            }

            override fun areContentsTheSame(
                oldItem: ExchangesResponse.Content,
                newItem: ExchangesResponse.Content
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangePostingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostingBinding.inflate(inflater, parent, false)
        return ExchangePostingViewHolder(binding)
    }

    fun clearData() {
        submitList(listOf())
    }
}