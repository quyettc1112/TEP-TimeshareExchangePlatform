package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ImageUploadModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemImageUploadBinding

class ImageUploadAdapter: BaseAdapter<ImageUploadModel, ImageUploadAdapter.ImageUploadViewHolder>(){
    var onDeleteClick: (ImageUploadModel) -> Unit = {}
    inner class ImageUploadViewHolder(binding: ItemImageUploadBinding): BaseItemViewHolderCF<ImageUploadModel, ItemImageUploadBinding>(binding) {
        override fun bind(item: ImageUploadModel) {
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

    override fun differCallBack(): DiffUtil.ItemCallback<ImageUploadModel> {
        return object : DiffUtil.ItemCallback<ImageUploadModel>() {
            override fun areItemsTheSame(oldItem: ImageUploadModel, newItem: ImageUploadModel): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ImageUploadModel, newItem: ImageUploadModel): Boolean {
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
    fun removeItem(item: ImageUploadModel) {
        val currentList = differ.currentList.toMutableList()
        currentList.remove(item)
        differ.submitList(currentList)
    }

    fun addImage(listImage: List<ImageUploadModel>) {
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