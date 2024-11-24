package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortDetailModelResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemResortRoomTypeBinding
import com.example.tep_timeshareexchangeplatform.databinding.ItemUnitTypeResortBinding

class UnitTypeResortAdapter : BaseAdapter<ResortDetailModelResponse.UnitTypeDto, UnitTypeResortAdapter.UnitTypeResortViewHolder>() {

    var onItemClick: ((ResortDetailModelResponse.UnitTypeDto) -> Unit)? = null
    var onViewDetailClick: ((ResortDetailModelResponse.UnitTypeDto) -> Unit)? = null
    inner class UnitTypeResortViewHolder(binding: ItemUnitTypeResortBinding) :
        BaseItemViewHolderCF<ResortDetailModelResponse.UnitTypeDto, ItemUnitTypeResortBinding>(
            binding
        ) {
        override fun bind(item: ResortDetailModelResponse.UnitTypeDto) {
            binding.apply {
                // Name of the room
                tvTitleUnit.text = item.title
                // Image
                Glide.with(itemView)
                    .load(item.photos)
                    .placeholder(R.drawable.ripple_effect_white)
                    .error(R.drawable.ic_image_tmp_holder)
                    .into(ivUnitTypeResort)

                // Bathroom
                tvNumBath.text = item.bathrooms.toString()

                tvKitchen.text = item.kitchen

                binding.tvPrice.text = "${item.price?.let { Constant.formatPriceLong(it) }} VNĐ"

                // Number of guests
                tvNumPerson.text = item.sleeps.toString()


                binding.root.setOnClickListener {
                    onItemClick?.invoke(item)
                }


                binding.llUnitTypeDetail.setOnClickListener {
                    onViewDetailClick?.invoke(item)
                }


            }
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<ResortDetailModelResponse.UnitTypeDto> {
        return object : DiffUtil.ItemCallback<ResortDetailModelResponse.UnitTypeDto>() {
            override fun areItemsTheSame(
                oldItem: ResortDetailModelResponse.UnitTypeDto,
                newItem: ResortDetailModelResponse.UnitTypeDto
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: ResortDetailModelResponse.UnitTypeDto,
                newItem: ResortDetailModelResponse.UnitTypeDto
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitTypeResortViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemUnitTypeResortBinding.inflate(layoutInflater, parent, false)
        return UnitTypeResortViewHolder(binding)
    }
}