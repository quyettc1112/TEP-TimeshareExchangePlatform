package com.example.tep_timeshareexchangeplatform.Common.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.BlogResponse
import com.example.tep_timeshareexchangeplatform.databinding.ItemBlogBinding

class BlogAdapter : BaseAdapter<BlogResponse.Content, BlogAdapter.BlogViewHolder> (){

    inner class BlogViewHolder(binding: ItemBlogBinding) : BaseItemViewHolderCF<BlogResponse.Content, ItemBlogBinding> (binding) {
        override fun bind(item: BlogResponse.Content) {
            binding.tvBlogTitle.text = item.title
            Glide.with(binding.root.context)
                .load(item.image)
                .into(binding.imgBlogThumbnail)

        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<BlogResponse.Content> {
        return object : DiffUtil.ItemCallback<BlogResponse.Content>() {
            override fun areItemsTheSame(oldItem: BlogResponse.Content, newItem: BlogResponse.Content): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: BlogResponse.Content, newItem: BlogResponse.Content): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBlogBinding.inflate(inflater, parent, false)
        return BlogViewHolder(binding)
    }
}