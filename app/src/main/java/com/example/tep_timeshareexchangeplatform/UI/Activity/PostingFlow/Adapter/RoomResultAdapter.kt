package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemRoomResultBinding

class RoomResultAdapter : BaseAdapter<RoomModel, RoomResultAdapter.RoomResultViewHolder>(){

    var onItemClick: ((RoomModel) -> Unit)? = null
    inner class RoomResultViewHolder(binding: ItemRoomResultBinding) : BaseItemViewHolderCF<RoomModel, ItemRoomResultBinding>(binding) {
        override fun bind(item: RoomModel) {
            binding.tvRoomCode.text ="Mã Phòng: " + item.roomInfoCode
            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<RoomModel> {
        return object : DiffUtil.ItemCallback<RoomModel>() {
            override fun areItemsTheSame(oldItem: RoomModel, newItem: RoomModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: RoomModel, newItem: RoomModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomResultViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRoomResultBinding.inflate(layoutInflater, parent, false)
        return RoomResultViewHolder(binding)
    }
}