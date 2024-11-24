package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.databinding.ItemViewPagerAdapterBinding

class ImageViewPagerAdapter: BaseAdapter<String, ImageViewPagerAdapter.ImageViewPagerViewHoler>() {

    inner class ImageViewPagerViewHoler(binding : ItemViewPagerAdapterBinding) : BaseItemViewHolderCF<String, ItemViewPagerAdapterBinding>(binding) {
        override fun bind(item: String) {
            if (item.isNotEmpty()) {
                binding.apply {
                    imResortImage.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    imResortImage.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    imResortImage.requestLayout()

                    Glide.with(imResortImage.context)
                        .load(item)
                        .into(imResortImage)
                }
            }
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<String> {
        return object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewPagerViewHoler {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemViewPagerAdapterBinding.inflate(layoutInflater, parent, false)
        return ImageViewPagerViewHoler(binding)
    }
}