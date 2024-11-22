package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.FeedbackListActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Feedback.FeedbacksResponse
import com.example.tep_timeshareexchangeplatform.databinding.ItemReviewBinding

class FeedbackListAdapter : BaseAdapter<FeedbacksResponse.Content, FeedbackListAdapter.FeedbackListViewHolder>() {


    inner class FeedbackListViewHolder(binding: ItemReviewBinding) :
        BaseItemViewHolderCF<FeedbacksResponse.Content, ItemReviewBinding>(binding) {
        override fun bind(item: FeedbacksResponse.Content) {
            binding.apply {
                ratingBar.rating = item.ratingPoint.toFloat()
                tvComment.text = item.comment
                tvDateReview.text = item.createdDate
                tvUserName.text = "Khách hàng: ${maskUserName(item.customer.fullName)}"
            }
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

    override fun differCallBack(): DiffUtil.ItemCallback<FeedbacksResponse.Content> {
        return object : DiffUtil.ItemCallback<FeedbacksResponse.Content>() {
            override fun areItemsTheSame(
                oldItem: FeedbacksResponse.Content,
                newItem: FeedbacksResponse.Content
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FeedbacksResponse.Content,
                newItem: FeedbacksResponse.Content
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackListViewHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemReviewBinding.inflate(layoutInflater, parent, false)
        return FeedbackListViewHolder(binding)
    }
}