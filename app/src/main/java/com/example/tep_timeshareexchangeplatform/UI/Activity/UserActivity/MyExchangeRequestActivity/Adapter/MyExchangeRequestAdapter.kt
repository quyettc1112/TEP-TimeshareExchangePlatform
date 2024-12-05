package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.ExchangeRequestOnPostResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.MyExchangeRequestResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.MyExchangeRequestActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyExchangeRequestStatus
import com.example.tep_timeshareexchangeplatform.databinding.ItemExchangeRequestBinding

class MyExchangeRequestAdapter(var context: MyExchangeRequestActivity) :
    BaseAdapter<MyExchangeRequestResponse.Content, MyExchangeRequestAdapter.MyExchangeRequestViewHolder>() {

    var onItemClick: ((MyExchangeRequestResponse.Content) -> Unit)? = null


    inner class MyExchangeRequestViewHolder(binding: ItemExchangeRequestBinding) :
        BaseItemViewHolderCF<MyExchangeRequestResponse.Content, ItemExchangeRequestBinding>(binding) {
        override fun bind(item: MyExchangeRequestResponse.Content) {


            binding.status.text = MyExchangeRequestStatus.fromApiStatus(item.status)?.getDescription(context);

            // Posting Info
            binding.apply {
                resortName.text = "${item.exchangePosting.roomInfoResortResortName}"
                if(item.startDate != null && item.endDate != null) {
                    checkInDate.text =
                        Constant.formatDateByLocale(item.startDate, binding.root.context)
                    checkOutDate.text =
                        Constant.formatDateByLocale(item.endDate, binding.root.context)
                }
                // Photo
                Glide.with(binding.root.context)
                    .load(item.exchangePosting.roomInfoResortLogo)
                    .placeholder(R.drawable.ripple_effect_white)
                    .into(binding.resortLogo)
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }
            applyStatus(item)
        }

        private fun applyStatus(item: MyExchangeRequestResponse.Content) {
            // Show Status
            when (MyExchangeRequestStatus.fromApiStatus(item.status)) {
                MyExchangeRequestStatus.PENDING_OWNER -> {
                    applyStatusStyle(
                        context,
                        R.color.status_awaiting_confirmation_text,
                        R.color.status_awaiting_confirmation_bg
                    )
                }

                MyExchangeRequestStatus.PENDING_APPROVAL -> {
                    applyStatusStyle(
                        context,
                        R.color.white,
                        R.color.status_pending_approval_text
                    )
                }

                MyExchangeRequestStatus.PENDING_RENTER_PRICING -> {
                    applyStatusStyle(
                        context,
                        R.color.white,
                        R.color.status_pending_approval_text
                    )
                }

                MyExchangeRequestStatus.PENDING_RENTER_PAYMENT -> {
                    applyStatusStyle(
                        context,
                        R.color.white,
                        R.color.status_pending_approval_text
                    )
                }

                MyExchangeRequestStatus.PENDING_OWNER_PAYMENT -> {
                    applyStatusStyle(
                        context,
                        R.color.white,
                        R.color.status_pending_approval_text
                    )
                }

                MyExchangeRequestStatus.COMPLETED -> {
                    applyStatusStyle(
                        context,
                        R.color.white,
                        R.color.green_verify
                    )
                }


                MyExchangeRequestStatus.REJECT_APPROVAL -> {
                    applyStatusStyle(
                        context,
                        R.color.white,
                        R.color.status_rejected_text
                    )
                }

                MyExchangeRequestStatus.RENTER_REJECT -> {
                    applyStatusStyle(
                        context,
                        R.color.white,
                        R.color.status_rejected_text
                    )
                }

                MyExchangeRequestStatus.OWNER_REJECT -> {
                    applyStatusStyle(
                        context,
                        R.color.white,
                        R.color.status_rejected_text
                    )
                }

                else -> {
                    // Default or unknown status case
                    applyStatusStyle(
                        context,
                        R.color.status_unknown_bg,
                        R.color.status_unknown_text
                    )
                }
            }

            binding.status.text =
                MyExchangeRequestStatus.fromApiStatus(item.status)?.getDescription(context)
                    ?: ""
        }

        private fun applyStatusStyle(context: Context, backgroundColorRes: Int, textColorRes: Int) {
            binding.apply {
                // Set the background color resource
                status.setBackgroundResource(backgroundColorRes)

                // Set the text color
                status.setTextColor(context.getColor(textColorRes))
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<MyExchangeRequestResponse.Content> {
        return object : DiffUtil.ItemCallback<MyExchangeRequestResponse.Content>() {
            override fun areItemsTheSame(
                oldItem: MyExchangeRequestResponse.Content,
                newItem: MyExchangeRequestResponse.Content
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MyExchangeRequestResponse.Content,
                newItem: MyExchangeRequestResponse.Content
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyExchangeRequestViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemExchangeRequestBinding.inflate(layoutInflater, parent, false)
        return MyExchangeRequestViewHolder(binding)
    }

}