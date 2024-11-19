package com.example.tep_timeshareexchangeplatform.Common.Adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemTimeshareImageBinding

class ImagePostingAdapter : BaseAdapter<String, ImagePostingAdapter.ImagePostingViewHolder>() {


    inner class ImagePostingViewHolder(binding: ItemTimeshareImageBinding) : BaseItemViewHolderCF<String, ItemTimeshareImageBinding>(binding) {
        override fun bind(item: String) {
            binding.apply {
                // Load image using Glide
                val position = bindingAdapterPosition
                Glide.with(itemView.context)
                    .load(item)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            // Ẩn Lottie khi load thất bại
                            binding.lottiePlaceholder.visibility = View.GONE
                            imageView.setImageResource(R.drawable.ic_error_) // hiển thị ảnh lỗi
                            imageView.visibility = View.VISIBLE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.lottiePlaceholder.visibility = View.GONE
                            imageView.visibility = View.VISIBLE
                            return false
                        }

                    })
                    .into(imageView)

                // Kiểm tra nếu đây là item thứ 6 và còn item sau đó
                if (position == 5 && itemCount > 6) {
                    binding.llNumImageContainer.visibility = View.VISIBLE
                    binding.tvNumImageLeft.text = "+${itemCount - 5}"
                } else {
                    binding.llNumImageContainer.visibility = View.GONE
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePostingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTimeshareImageBinding.inflate(layoutInflater, parent, false)
        return ImagePostingViewHolder(binding)
    }
}