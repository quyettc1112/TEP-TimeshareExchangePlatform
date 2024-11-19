package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortDetailModelResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemResortRoomTypeBinding

class UnitTypeAdapter(private val showFullInfo: Boolean): BaseAdapter<ResortDetailModelResponse.UnitTypeDto, com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.UnitTypeAdapter.RoomTypeViewHolder>(){

    var onItemClick: ((ResortDetailModelResponse.UnitTypeDto) -> Unit)? = null
    var onButtonBookClick: ((ResortDetailModelResponse.UnitTypeDto) -> Unit)? = null

    inner class RoomTypeViewHolder(binding: ItemResortRoomTypeBinding): BaseItemViewHolderCF<ResortDetailModelResponse.UnitTypeDto, ItemResortRoomTypeBinding> (binding) {

        fun showDetailInfoVisibility(isShow: Boolean) {
            binding.apply {
                if (isShow) {
                    llAmennities.visibility = ViewGroup.VISIBLE
                    crlResortPrice.visibility = ViewGroup.VISIBLE
                } else {
                    llAmennities.visibility = ViewGroup.GONE
                    crlResortPrice.visibility = ViewGroup.GONE
                }
            }
        }

        override fun bind(item: ResortDetailModelResponse.UnitTypeDto) {
            binding.apply {
                // Name of the room
                tvRoomName.text = item.title
                // Image
                Glide.with(itemView)
                    .load(R.drawable.im_matiral_timeshare)
                    .into(imRoomTypeImage)
                showDetailInfoVisibility(showFullInfo)

                // Bathroom
                tvNumBathroom.text = item.bathrooms.toString()

                // Kitchen
                tvNumKitchen.text = 1.toString()
                tvKitchen.text = item.kitchen

                // Bedroom
                tvNumBed.text = "${item.bedrooms}"
                tvBed.text = "${item.bedsQueen} Queen, ${item.bedsKing} King, ${item.bedsTwin} Twin"

                // Number of guests
                tvNumPerson.text = item.sleeps.toString()


                binding.root.setOnClickListener {
                    onItemClick?.invoke(item)
                }

                binding.btnViewRoom.setOnClickListener {
                    onButtonBookClick?.invoke(item)
                }

                // Hide Price
                binding.tvPrice.visibility = ViewGroup.GONE
            }
        }
    }



    override fun differCallBack(): DiffUtil.ItemCallback<ResortDetailModelResponse.UnitTypeDto> {
        return object : DiffUtil.ItemCallback<ResortDetailModelResponse.UnitTypeDto>() {
            override fun areItemsTheSame(oldItem: ResortDetailModelResponse.UnitTypeDto, newItem: ResortDetailModelResponse.UnitTypeDto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ResortDetailModelResponse.UnitTypeDto, newItem: ResortDetailModelResponse.UnitTypeDto): Boolean {
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