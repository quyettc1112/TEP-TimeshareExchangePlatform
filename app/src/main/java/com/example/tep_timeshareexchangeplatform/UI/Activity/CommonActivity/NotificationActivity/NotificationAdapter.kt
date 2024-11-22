package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.NotificationActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.airbnb.lottie.LottieDrawable
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.NotificationModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.NotificationType
import com.example.tep_timeshareexchangeplatform.databinding.ItemNotificationBinding

class NotificationAdapter :
    BaseAdapter<NotificationModel, NotificationAdapter.NotificationViewHolder>() {
    var onItemClick: ((NotificationModel) -> Unit)? = null

    inner class NotificationViewHolder(binding: ItemNotificationBinding) :
        BaseItemViewHolderCF<NotificationModel, ItemNotificationBinding>(binding) {
        override fun bind(item: NotificationModel) {
            // Is Read
            if (item.isRead){
                binding.notificationItem.backgroundTintList =
                    ContextCompat.getColorStateList(binding.root.context, R.color.primary_background_F9)
                binding.optionNotification.setImageResource(R.drawable.baseline_more_horiz_24)
            }
            else {
                binding.notificationItem.backgroundTintList =
                    ContextCompat.getColorStateList(binding.root.context, R.color.white)
                binding.optionNotification.setImageResource(R.drawable.ic_seen)

            }

            // Time
            binding.timeNotification.text = item.timestamp

            // Description
            binding.descriptionNotification.text = item.description

            // Title
            binding.titleNotification.text = item.title

            // Type Notification
            bindDataTypeNotification(item)




            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }


        }

        private fun bindDataTypeNotification(item: NotificationModel) {
            when (item.typeNotification) {
                NotificationType.NOTIFICATION -> {
                    binding.typeNotification.text = NotificationType.NOTIFICATION.notificationType
                }

                NotificationType.DEPOSIT -> {
                    binding.typeNotification.text = NotificationType.DEPOSIT.notificationType
                }

                NotificationType.REJECT_POSTING -> {
                    binding.typeNotification.text = NotificationType.REJECT_POSTING.notificationType
                }

                NotificationType.ACCEPT_POSTING -> {
                    binding.typeNotification.text = NotificationType.ACCEPT_POSTING.notificationType
                }

                NotificationType.DONE_BOOKING -> {
                    binding.typeNotification.text = NotificationType.DONE_BOOKING.notificationType
                }

                NotificationType.MEMBERSHIP -> {
                    binding.typeNotification.text = NotificationType.MEMBERSHIP.notificationType
                }
            }
            binding.animType.apply {
                setAnimation(item.iconResId)
                if(item.isRead) pauseAnimation()
                else playAnimation()
                repeatCount = LottieDrawable.INFINITE // Lặp lại animation
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<NotificationModel> {
        return object : DiffUtil.ItemCallback<NotificationModel>() {
            override fun areItemsTheSame(
                oldItem: NotificationModel,
                newItem: NotificationModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: NotificationModel,
                newItem: NotificationModel
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