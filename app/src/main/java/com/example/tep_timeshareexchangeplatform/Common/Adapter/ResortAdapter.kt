package com.example.tep_timeshareexchangeplatform.Common.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.databinding.ItemResortBinding
import java.text.DecimalFormat

class ResortAdapter: BaseAdapter<ResortModelResponse.Content, ResortAdapter.ResortViewHolder>() {

    var onItemClick: ((ResortModelResponse.Content) -> Unit)? = null
    var onFavoriteClick: ((ResortModelResponse.Content) -> Unit)? = null

    inner class ResortViewHolder(binding: ItemResortBinding): BaseItemViewHolderCF<ResortModelResponse.Content, ItemResortBinding>(binding) {
        override fun bind(item: ResortModelResponse.Content) {
            binding.tvResortName.text = item.resortName
            binding.tvLocation.text = item.address


            binding.tvPrice.text = "${formatPrice(item.minPrice)} - ${formatPrice(item.maxPrice)} VND"

            binding.root.setOnClickListener {
                onItemClick?.let { it1 -> it1(item) }
            }

            binding.llFavorite.setOnClickListener {
                onFavoriteClick?.let { it1 -> it1(item) }
            }
            // Not yet implemented
           /* binding.tvRating.text = item.rating.toString()
            binding.tvRatingCount.text = item.ratingCount*/
            /*Glide.with(binding.imResortImage.context)
                .load(item.resortImage)
                .into(binding.imResortImage)*/

            // Hide Unnecessary Views
            binding.tvNumberOfNight.visibility = View.GONE
        }

        fun formatPrice(price: Int): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(price)
        }


    }

    override fun differCallBack(): DiffUtil.ItemCallback<ResortModelResponse.Content> {
        return object : DiffUtil.ItemCallback<ResortModelResponse.Content>() {
            override fun areItemsTheSame(oldItem: ResortModelResponse.Content, newItem: ResortModelResponse.Content): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ResortModelResponse.Content, newItem: ResortModelResponse.Content): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResortViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemResortBinding.inflate(inflater, parent, false)
        return ResortViewHolder(binding)
    }
}