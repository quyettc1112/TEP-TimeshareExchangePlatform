package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.RoomTypeModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemResortRoomTypeBinding

class RoomTypeAdapter: BaseAdapter<RoomTypeModel, RoomTypeAdapter.RoomTypeViewHolder>(){

    var onItemClick: ((RoomTypeModel) -> Unit)? = null
    var onButonBookClick: ((RoomTypeModel) -> Unit)? = null

    inner class RoomTypeViewHolder(binding: ItemResortRoomTypeBinding): BaseItemViewHolderCF<RoomTypeModel, ItemResortRoomTypeBinding> (binding) {
        override fun bind(item: RoomTypeModel) {
            binding.apply {
               tvRoomName.text = item.roomName
                Glide.with(itemView)
                    .load(item.image)
                    .into(imRoomTypeImage)
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }

            binding.btnViewRoom.setOnClickListener {
                onButonBookClick?.invoke(item)
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<RoomTypeModel> {
        return object : DiffUtil.ItemCallback<RoomTypeModel>() {
            override fun areItemsTheSame(oldItem: RoomTypeModel, newItem: RoomTypeModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RoomTypeModel, newItem: RoomTypeModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomTypeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemResortRoomTypeBinding.inflate(layoutInflater, parent, false)
        return RoomTypeViewHolder(binding)
    }

}