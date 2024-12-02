package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.Adapter


import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.DataSource
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemImageUploadBinding

class ImageUPAdapter : BaseAdapter<String, ImageUPAdapter.ImageUploadViewHolder>() {

    var onDeleteClick: (String) -> Unit = {}
    inner class ImageUploadViewHolder(binding: ItemImageUploadBinding) :
        BaseItemViewHolderCF<String, ItemImageUploadBinding>(binding) {
        override fun bind(item: String) {
            binding.apply {
                // Load image using Glide
                Glide.with(itemView.context)
                    .load(item)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.lottiePlaceholder.visibility = View.GONE
                            imageViewAvatar.setImageResource(R.drawable.ic_error_) // hiển thị ảnh lỗi
                            imageViewAvatar.visibility = View.VISIBLE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            dataSource: com.bumptech.glide.load.DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.lottiePlaceholder.visibility = View.GONE
                            imageViewAvatar.visibility = View.VISIBLE
                            return false
                        }

                    })
                    .error(R.drawable.ic_image_tmp_holder)
                    .into(imageViewAvatar)
            }
            binding.cardViewClose.visibility = ViewGroup.VISIBLE
            binding.cardViewClose.setOnClickListener {
                onDeleteClick.let { invoke -> invoke(item) }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageUploadViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemImageUploadBinding =
            ItemImageUploadBinding.inflate(layoutInflater, parent, false)
        return ImageUploadViewHolder(binding)
    }

    // Remove an item from the list
    fun removeItem(item: String) {
        val currentList = differ.currentList.toMutableList()
        currentList.remove(item)
        differ.submitList(currentList)
    }

    fun addImage(listImage: List<String>) {
        val currentList = differ.currentList.toMutableList()
        currentList.addAll(listImage)
        differ.submitList(currentList)
    }

    fun clearAll() {
        differ.submitList(listOf())
    }

    fun getImageListSize(): Int {
        return differ.currentList.size
    }

}