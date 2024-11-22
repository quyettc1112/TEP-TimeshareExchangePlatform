package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.Adapter

import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.ExchangeRequestOnPostActivity.ExchangeRequestOnPostActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.ExchangeRequestOnPostResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemRequestOnPostBinding

class ExchangeRequestOnPostAdapter(var context: ExchangeRequestOnPostActivity) :
    BaseAdapter<ExchangeRequestOnPostResponse.Content, ExchangeRequestOnPostAdapter.ExchangeRequestOnPostViewHolder>() {

    var onItemClick: ((ExchangeRequestOnPostResponse.Content) -> Unit)? = null


    inner class ExchangeRequestOnPostViewHolder(binding: ItemRequestOnPostBinding) :
        BaseItemViewHolderCF<ExchangeRequestOnPostResponse.Content, ItemRequestOnPostBinding>(
            binding
        ) {
        override fun bind(item: ExchangeRequestOnPostResponse.Content) {

            // Posting Info
            binding.apply {
                tvOwnerFullName.text = "${item.ownerFullName}"
                if(item.startDate != null && item.endDate != null){
                    tvCheckInDate.text =
                        Constant.formatDateByLocale(item.startDate, binding.root.context)
                    tvCheckOutDate.text =
                        Constant.formatDateByLocale(item.endDate, binding.root.context)
                }
                // Photo
                Glide.with(binding.root.context)
                    .load(item.ownerAvatar)
                    .error(R.drawable.ic_image_placeholder)
                    .placeholder(R.drawable.ripple_effect_white)
                    .into(binding.ivOwnerAvatar)
                //Resort photo
                item.roomInfo.unitType?.photos?.let {
                    Glide.with(binding.root.context)
                        .load(item.roomInfo.unitType.photos ?: "")
                        .error(R.drawable.placeholder_image)
                        .placeholder(R.drawable.ripple_effect_white)
                        .into(binding.ivRoomImage)
                }
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<ExchangeRequestOnPostResponse.Content> {
        return object : DiffUtil.ItemCallback<ExchangeRequestOnPostResponse.Content>() {
            override fun areItemsTheSame(
                oldItem: ExchangeRequestOnPostResponse.Content,
                newItem: ExchangeRequestOnPostResponse.Content
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ExchangeRequestOnPostResponse.Content,
                newItem: ExchangeRequestOnPostResponse.Content
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExchangeRequestOnPostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRequestOnPostBinding.inflate(layoutInflater, parent, false)
        return ExchangeRequestOnPostViewHolder(binding)
    }

}