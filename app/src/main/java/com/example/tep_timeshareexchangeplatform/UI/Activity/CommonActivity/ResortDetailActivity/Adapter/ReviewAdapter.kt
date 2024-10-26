package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ReviewModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemReviewBinding

class ReviewAdapter: BaseAdapter<ReviewModel, ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(binding : ItemReviewBinding): BaseItemViewHolderCF<ReviewModel,ItemReviewBinding > (binding){
        override fun bind(item: ReviewModel) {
            binding.tvComment.text = item.reviewContent
            binding.tvDateReview.text = item.reviewDate
            binding.ratingBar.rating = item.reviewRating.toFloat()
            binding.tvUserName.text = "User Name: ${item.reviewUserId}"
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<ReviewModel> {
        return object : DiffUtil.ItemCallback<ReviewModel>(){
            override fun areItemsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean {
                return oldItem.reviewId == newItem.reviewId
            }

            override fun areContentsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean {
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