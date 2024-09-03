package com.example.tep_timeshareexchangeplatform.Common.Adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.DestinationModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemTouristDestinationBinding

class DestianationAdapter: BaseAdapter<DestinationModel, DestianationAdapter.DestinationViewHolder>()  {

    inner class DestinationViewHolder(binding: ItemTouristDestinationBinding): BaseItemViewHolderCF<DestinationModel, ItemTouristDestinationBinding>(binding) {
        override fun bind(item: DestinationModel) {

            binding.textView.text = position.toString()
            Glide.with(binding.imageDestination.context)
                .load(item.destinationImage)
                .into(binding.imageDestination)


            // Tùy chỉnh chiều cao của mỗi mục dựa trên vị trí
            val layoutParams = binding.imageDestination.layoutParams
            layoutParams.height = when (position) {
                0 -> 100.dpToPx()
                1 -> 200.dpToPx()
                2 -> 100.dpToPx()
                3 -> 100.dpToPx()
                else -> 200.dpToPx() // Chiều cao mặc định cho các mục khác
            }

            layoutParams.width = when (position) {
                0 -> 200.dpToPx()
                1 -> 100.dpToPx()
                2 -> 100.dpToPx()
                3 -> 100.dpToPx()
                else -> 200.dpToPx() // Chiều rộng mặc định cho các mục khác
            }

            binding.imageDestination.layoutParams = layoutParams
        }

        fun Int.dpToPx(): Int {
            return (this * Resources.getSystem().displayMetrics.density).toInt()
        }


    }

    override fun differCallBack(): DiffUtil.ItemCallback<DestinationModel> {
        return object : DiffUtil.ItemCallback<DestinationModel>() {
            override fun areItemsTheSame(oldItem: DestinationModel, newItem: DestinationModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DestinationModel, newItem: DestinationModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTouristDestinationBinding.inflate(inflater, parent, false)
        return DestinationViewHolder(binding)
    }
}