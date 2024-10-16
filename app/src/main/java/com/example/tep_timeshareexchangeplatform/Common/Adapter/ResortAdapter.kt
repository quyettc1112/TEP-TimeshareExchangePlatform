package com.example.tep_timeshareexchangeplatform.Common.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelOfficial.Resort.ResortModel
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.databinding.ItemResortBinding

class ResortAdapter: BaseAdapter<ResortModel.Content, ResortAdapter.ResortViewHolder>() {

    var onItemClick: ((ResortModel.Content) -> Unit)? = null
    var onFavoriteClick: ((ResortModel.Content) -> Unit)? = null

    inner class ResortViewHolder(binding: ItemResortBinding): BaseItemViewHolderCF<ResortModel.Content, ItemResortBinding>(binding) {
        override fun bind(item: ResortModel.Content) {
            binding.tvResortName.text = item.resortName
            binding.tvLocation.text = item.address
            binding.tvPrice.text = "${item.minPrice} - ${item.maxPrice} VND"

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

    }

    override fun differCallBack(): DiffUtil.ItemCallback<ResortModel.Content> {
        return object : DiffUtil.ItemCallback<ResortModel.Content>() {
            override fun areItemsTheSame(oldItem: ResortModel.Content, newItem: ResortModel.Content): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ResortModel.Content, newItem: ResortModel.Content): Boolean {
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