package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.databinding.ItemResortRvBinding
import java.text.DecimalFormat

class ResortAdapterRV: BaseAdapter<ResortModelResponse.Content, ResortAdapterRV.ResortViewHolder>() {

    var onItemClick: ((ResortModelResponse.Content) -> Unit)? = null
    var onFavoriteClick: ((ResortModelResponse.Content) -> Unit)? = null

    inner class ResortViewHolder(binding: ItemResortRvBinding): BaseItemViewHolderCF<ResortModelResponse.Content, ItemResortRvBinding>(binding) {
        override fun bind(item: ResortModelResponse.Content) {
            binding.tvResortName.text = item.resortName
            binding.tvLocation.text = item.address


            binding.btnPrice.text = "Chỉ Từ ${formatPrice(item.minPrice)} VND"

            binding.root.setOnClickListener {
                onItemClick?.let { it1 -> it1(item) }
            }

            binding.llFavorite.setOnClickListener {
                onFavoriteClick?.let { it1 -> it1(item) }
            }
            // Not yet implemented
            Glide.with(binding.imResortImage.context)
                .load(item.logo)
                .into(binding.imResortImage)
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
        val binding = ItemResortRvBinding.inflate(inflater, parent, false)
        return ResortViewHolder(binding)
    }
    fun clearData() {
        submitList(listOf())
    }
}