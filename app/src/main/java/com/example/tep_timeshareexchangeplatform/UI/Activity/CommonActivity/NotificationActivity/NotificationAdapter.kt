package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.NotificationActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.airbnb.lottie.LottieDrawable
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.NotificationModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Notification.NotificationResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.NotificationType
import com.example.tep_timeshareexchangeplatform.databinding.ItemNotificationBinding

class NotificationAdapter :
    BaseAdapter<NotificationResponse.Content, NotificationAdapter.NotificationViewHolder>() {
    var onItemClick: ((NotificationResponse.Content) -> Unit)? = null

    inner class NotificationViewHolder(binding: ItemNotificationBinding) :
        BaseItemViewHolderCF<NotificationResponse.Content, ItemNotificationBinding>(binding) {
        override fun bind(item: NotificationResponse.Content) {

            if (item.isRead) {
                binding.main.backgroundTintList = ContextCompat.getColorStateList(
                    binding.root.context,
                    R.color.white
                )
            } else {
                binding.main.backgroundTintList = ContextCompat.getColorStateList(
                    binding.root.context,
                    R.color.i_blue_light
                )
            }

            // Time
            binding.timeNotification.text = item.createdAt

            // Description
            binding.descriptionNotification.text = item.content

            // Title
            binding.titleNotification.text = item.title

            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<NotificationResponse.Content> {
        return object : DiffUtil.ItemCallback<NotificationResponse.Content>() {
            override fun areItemsTheSame(
                oldItem: NotificationResponse.Content,
                newItem: NotificationResponse.Content
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: NotificationResponse.Content,
                newItem: NotificationResponse.Content
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNotificationBinding.inflate(layoutInflater, parent, false)
        return NotificationViewHolder(binding)
    }
}