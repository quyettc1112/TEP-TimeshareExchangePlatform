package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.TimeshareFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.TimeshareModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingsResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemTimeshareVer1Binding
import java.text.DecimalFormat

class TimeshareAdapterRV : BaseAdapter<PostingsResponse.Content, TimeshareAdapterRV.TimeshareAdapterRVViewHolder>() {


    var onItemClick: ((PostingsResponse.Content) -> Unit)? = null
    var onFavoriteClick: ((PostingsResponse.Content) -> Unit)? = null

    inner class TimeshareAdapterRVViewHolder(binding: ItemTimeshareVer1Binding): BaseItemViewHolderCF<PostingsResponse.Content, ItemTimeshareVer1Binding>(binding) {
        override fun bind(item: PostingsResponse.Content) {
            Glide.with(binding.imImageTimeshare.context)
                .load(R.drawable.im_matiral_timeshare)
                .into(binding.imImageTimeshare)
            binding.tvTimeShreName.text = item.roomName
            binding.tvLocation.text = item.address
            binding.tvDate.text = item.checkinDate + " - " + item.checkoutDate
          /*  binding.tvRoom.text = item.*/
            binding.tvPrice.text = "${formatPrice(item.totalPrice)} VND"
            binding.tvNumberOfNight.text = item.nights.toString()

            binding.root.setOnClickListener {
                onItemClick?.let { it1 -> it1(item) }
            }
          /*  binding.imFavorite.setOnClickListener {
                onFavoriteClick?.let { it1 -> it1(item) }
            }*/
        }

        fun formatPrice(price: Int): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(price)
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<PostingsResponse.Content> {
        return object : DiffUtil.ItemCallback<PostingsResponse.Content>() {
            override fun areItemsTheSame(
                oldItem: PostingsResponse.Content,
                newItem: PostingsResponse.Content
            ): Boolean {
                return oldItem.rentalPostingId == newItem.rentalPostingId
            }

            override fun areContentsTheSame(
                oldItem: PostingsResponse.Content,
                newItem: PostingsResponse.Content
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeshareAdapterRVViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTimeshareVer1Binding.inflate(inflater, parent, false)
        return TimeshareAdapterRVViewHolder(binding)
    }

}