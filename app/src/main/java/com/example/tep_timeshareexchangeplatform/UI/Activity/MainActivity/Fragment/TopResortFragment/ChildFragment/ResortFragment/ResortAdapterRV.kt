package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.ResortFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ResortModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemResortRvBinding

class ResortAdapterRV: BaseAdapter<ResortModel, ResortAdapterRV.ResortViewHolder>() {

    var onItemClick: ((ResortModel) -> Unit)? = null
    var onFavoriteClick: ((ResortModel) -> Unit)? = null

    inner class ResortViewHolder(binding: ItemResortRvBinding): BaseItemViewHolderCF<ResortModel, ItemResortRvBinding>(binding) {
        override fun bind(item: ResortModel) {
            binding.tvResortName.text = item.resortName
            binding.tvRating.text = item.rating.toString()
            binding.tvRatingCount.text = item.ratingCount
            binding.tvLocation.text = item.location
            binding.tvRoom.text = item.roomDetails
            binding.tvPrice.text = item.price
            binding.tvNumberOfNight.text = item.numberOfNights
            Glide.with(binding.imResortImage.context)
                .load(item.resortImage)
                .into(binding.imResortImage)

            binding.root.setOnClickListener {
                onItemClick?.let { it1 -> it1(item) }
            }

            binding.llFavorite.setOnClickListener {
                onFavoriteClick?.let { it1 -> it1(item) }
            }
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<ResortModel> {
        return object : DiffUtil.ItemCallback<ResortModel>() {
            override fun areItemsTheSame(oldItem: ResortModel, newItem: ResortModel): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: ResortModel, newItem: ResortModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResortViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemResortRvBinding.inflate(inflater, parent, false)
        return ResortViewHolder(binding)
    }
}