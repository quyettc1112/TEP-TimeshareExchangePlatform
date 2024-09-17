package com.example.tep_timeshareexchangeplatform.Common.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.BlogModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ResortModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemBlogBinding
import com.example.tep_timeshareexchangeplatform.databinding.ItemResortBinding

class BlogAdapter : BaseAdapter<BlogModel, BlogAdapter.BlogViewHolder> (){

    inner class BlogViewHolder(binding: ItemBlogBinding) : BaseItemViewHolderCF<BlogModel, ItemBlogBinding> (binding) {
        override fun bind(item: BlogModel) {
            binding.tvBlogTitle.text = item.title
            Glide.with(binding.imBlog.context)
                .load(item.image)
                .into(binding.imBlog)

        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<BlogModel> {
        return object : DiffUtil.ItemCallback<BlogModel>() {
            override fun areItemsTheSame(oldItem: BlogModel, newItem: BlogModel): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: BlogModel, newItem: BlogModel): Boolean {
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