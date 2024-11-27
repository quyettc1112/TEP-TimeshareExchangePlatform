package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemImageDetailBinding

class ImageDetailAdapter(private val recyclerView: RecyclerView): BaseAdapter<String, ImageDetailAdapter.ImageDetailViewHolder>() {



    var onItemClick: ((Int) -> Unit)? = null
    var selectedPositionImage: Int = RecyclerView.NO_POSITION

    inner class ImageDetailViewHolder (binding: ItemImageDetailBinding) : BaseItemViewHolderCF<String, ItemImageDetailBinding>(binding) {
        override fun bind(item: String) {
           if (item.isNotEmpty()) {
                binding.apply {
                     Glide.with(imageView.context)
                         .load(item)
                         .placeholder(R.drawable.ic_unwind_logo_25)
                         .error(R.drawable.ic_image_tmp_holder)
                         .into(imageView)
                }
               // Update the visibility based on whether this item is selected
               binding.imageView.alpha = if (adapterPosition == selectedPositionImage) 1.0f else 0.5f
               // Click event
               binding.imageView.setOnClickListener {
                   onItemClick?.invoke(adapterPosition)
                   setSelectedPosition(adapterPosition)
                   smoothScrollToSelectedPosition(adapterPosition)// Smooth scroll to the clicked item
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemImageDetailBinding.inflate(layoutInflater, parent, false)
        return ImageDetailViewHolder(binding)
    }



    fun setSelectedPosition(position: Int) {
        val previousPosition = selectedPositionImage
        selectedPositionImage = position

        // Notify the adapter about item changes
        notifyItemChanged(previousPosition) // Unhighlight the previous item
        notifyItemChanged(selectedPositionImage) // Highlight the new item
    }

    // Scroll to the selected position
    fun smoothScrollToSelectedPosition(position: Int) {
        recyclerView.smoothScrollToPosition(position)
    }


}