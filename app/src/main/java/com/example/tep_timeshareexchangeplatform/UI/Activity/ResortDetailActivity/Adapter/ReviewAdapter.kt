package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ReviewModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortDetailModelResponse
import com.example.tep_timeshareexchangeplatform.databinding.ItemReviewBinding

class ReviewAdapter: BaseAdapter<ResortDetailModelResponse.Feedback, ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(binding : ItemReviewBinding): BaseItemViewHolderCF<ResortDetailModelResponse.Feedback,ItemReviewBinding > (binding){
        override fun bind(item: ResortDetailModelResponse.Feedback) {
            binding.tvComment.text = item.comment
            binding.tvDateReview.text = item.createdDate
            binding.ratingBar.rating = item.ratingPoint.toFloat()
            binding.tvUserName.text = "Khách hàng: ${maskUserName(item.user.fullName)}"
        }

        fun maskUserName(fullName: String): String {
            val words = fullName.split(" ")
            return words.joinToString(" ") { word ->
                if (word.length <= 2) {
                    word // Nếu từ có ít hơn hoặc bằng 2 ký tự, giữ nguyên
                } else {
                    word.first() + "*".repeat(word.length - 2) + word.last()
                }
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<ResortDetailModelResponse.Feedback> {
        return object : DiffUtil.ItemCallback<ResortDetailModelResponse.Feedback>(){
            override fun areItemsTheSame(oldItem: ResortDetailModelResponse.Feedback, newItem: ResortDetailModelResponse.Feedback): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ResortDetailModelResponse.Feedback, newItem: ResortDetailModelResponse.Feedback): Boolean {
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