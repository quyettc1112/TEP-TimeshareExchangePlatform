package com.example.tep_timeshareexchangeplatform.Common.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortDetailModelResponse
import com.example.tep_timeshareexchangeplatform.databinding.ItemReviewBinding

class ReviewAdapter : BaseAdapter<ResortDetailModelResponse.Feedback, ReviewAdapter.ReviewViewHolder>(){

    inner class ReviewViewHolder(binding: ItemReviewBinding) :
        BaseItemViewHolderCF<ResortDetailModelResponse.Feedback, ItemReviewBinding>(binding) {
        override fun bind(item: ResortDetailModelResponse.Feedback) {
            binding.apply {
                ratingBar.rating = item.ratingPoint.toFloat()
                tvComment.text = item.comment
                tvDateReview.text = item.createdDate
                tvUserName.text = item.user.fullName
            }
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<ResortDetailModelResponse.Feedback> {
        return object : DiffUtil.ItemCallback<ResortDetailModelResponse.Feedback>() {
            override fun areItemsTheSame(
                oldItem: ResortDetailModelResponse.Feedback,
                newItem: ResortDetailModelResponse.Feedback
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ResortDetailModelResponse.Feedback,
                newItem: ResortDetailModelResponse.Feedback
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemReviewBinding.inflate(layoutInflater, parent, false)
        return ReviewViewHolder(binding)
    }

}