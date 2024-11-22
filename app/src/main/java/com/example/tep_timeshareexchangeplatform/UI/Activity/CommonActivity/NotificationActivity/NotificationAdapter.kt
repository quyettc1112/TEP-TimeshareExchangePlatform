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
           /* if (item.isRead){
                binding.notificationItem.backgroundTintList =
                    ContextCompat.getColorStateList(binding.root.context, R.color.gray_400)
                binding.optionNotification.setImageResource(R.drawable.baseline_more_horiz_24)
            }
            else {
                binding.notificationItem.backgroundTintList =
                    ContextCompat.getColorStateList(binding.root.context, R.color.white)
                binding.optionNotification.setImageResource(R.drawable.ic_seen)

            }*/

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
                    binding.titleNotification.text = NotificationType.NOTIFICATION.notificationType
                    binding.btnViewDetail.visibility = View.GONE
                }

                NotificationType.DEPOSIT -> {
                    binding.titleNotification.text = NotificationType.DEPOSIT.notificationType
                    binding.btnViewDetail.visibility = View.VISIBLE

                }

                NotificationType.REJECT_POSTING -> {
                    binding.titleNotification.text = NotificationType.REJECT_POSTING.notificationType
                    binding.btnViewDetail.visibility = View.GONE
                }

                NotificationType.ACCEPT_POSTING -> {
                    binding.titleNotification.text = NotificationType.ACCEPT_POSTING.notificationType
                    binding.btnViewDetail.visibility = View.VISIBLE
                }

                NotificationType.DONE_BOOKING -> {
                    binding.titleNotification.text = NotificationType.DONE_BOOKING.notificationType
                    binding.btnViewDetail.visibility = View.VISIBLE
                }

                NotificationType.MEMBERSHIP -> {
                    binding.titleNotification.text = NotificationType.MEMBERSHIP.notificationType
                    binding.btnViewDetail.visibility = View.GONE
                }
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