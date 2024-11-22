package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
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

            // Show Status
            when (MyExchangeRequestStatus.fromApiStatus(item.status)) {
                MyExchangeRequestStatus.PENDING_APPROVAL -> {
                    applyStatusStyle(
                        context,
                        R.color.yellow200,  // Background color (e.g., yellow background)
                        R.color.yellow600    // Text color (e.g., dark yellow for text)
                    )
                }

                MyExchangeRequestStatus.PENDING_CUSTOMER -> {
                    applyStatusStyle(
                        context,
                        R.color.green200,      // Background color (e.g., white background)
                        R.color.green_verify  // Text color (green for text)
                    )
                }

                MyExchangeRequestStatus.COMPLETED -> {
                    applyStatusStyle(
                        context,
                        R.color.blue200,  // Background color (blue background)
                        R.color.blue_full             // Text color (lighter blue for text)
                    )
                }

                MyExchangeRequestStatus.REJECTED -> {
                    applyStatusStyle(
                        context,
                        R.color.red200,      // Background color (white background)
                        R.color.status_rejected_text  // Text color (red text for rejection)
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