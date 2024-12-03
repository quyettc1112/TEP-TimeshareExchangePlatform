package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ImageUploadModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemImageUploadBinding

class ImageUploadAdapter: BaseAdapter<ImageUploadModel, ImageUploadAdapter.ImageUploadViewHolder>(){
    var onDeleteClick: (ImageUploadModel) -> Unit = {}
    inner class ImageUploadViewHolder(binding: ItemImageUploadBinding): BaseItemViewHolderCF<ImageUploadModel, ItemImageUploadBinding>(binding) {
        override fun bind(item: ImageUploadModel) {
            binding.apply {
                Glide.with(imageViewAvatar.context)
                    .load(item.uri)
                    .into(imageViewAvatar)
                // Thiết lập sự kiện click cho root view của item
            }
            binding.lottiePlaceholder.visibility = ViewGroup.GONE
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