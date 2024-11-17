package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.BlogListActivity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.MyExchangeRequestResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.BlogResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.BlogListActivity.BlogListActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.Adapter.MyExchangeRequestAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.MyExchangeRequestActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyExchangeRequestStatus
import com.example.tep_timeshareexchangeplatform.databinding.ItemBlogBinding
import com.example.tep_timeshareexchangeplatform.databinding.ItemExchangeRequestBinding

class BlogListAdapter(var context: BlogListActivity) :
    BaseAdapter<BlogResponse.Content, BlogListAdapter.BlogListViewHolder>() {

    var onItemClick: ((BlogResponse.Content) -> Unit)? = null


    inner class BlogListViewHolder(binding: ItemBlogBinding) :
        BaseItemViewHolderCF<BlogResponse.Content, ItemBlogBinding>(binding) {
        override fun bind(item: BlogResponse.Content) {

            // Blog Info
            binding.apply {
                tvBlogTitle.text = "${item.title}"
                tvBlogDate.text = "${item.createdAt}"

                // Photo
                Glide.with(binding.root.context)
                    .load(item.image)
                    .placeholder(R.drawable.ripple_effect_white)
                    .into(binding.imgBlogThumbnail)
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<BlogResponse.Content> {
        return object : DiffUtil.ItemCallback<BlogResponse.Content>() {
            override fun areItemsTheSame(
                oldItem: BlogResponse.Content,
                newItem: BlogResponse.Content
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: BlogResponse.Content,
                newItem: BlogResponse.Content
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemBlogBinding.inflate(layoutInflater, parent, false)
        return BlogListViewHolder(binding)
    }

}