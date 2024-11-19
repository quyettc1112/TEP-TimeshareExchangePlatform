package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemFacilitieBinding

class AmenitiesAdapter :
    BaseAdapter<PublicPostingDetailResponse.ResortAmenity, AmenitiesAdapter.FacilitieViewHolder>() {

    inner class FacilitieViewHolder(binding: ItemFacilitieBinding) :
        BaseItemViewHolderCF<PublicPostingDetailResponse.ResortAmenity, ItemFacilitieBinding>(binding) {
        override fun bind(item: PublicPostingDetailResponse.ResortAmenity) {
            binding.apply {
                tvFacilitieName.text = item.name
                Glide.with(itemView).load(R.drawable.ic_air_conditioner).into(imFacilitie)
            }
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<PublicPostingDetailResponse.ResortAmenity> {
        return object : DiffUtil.ItemCallback<PublicPostingDetailResponse.ResortAmenity>() {
            override fun areItemsTheSame(
                oldItem: PublicPostingDetailResponse.ResortAmenity,
                newItem: PublicPostingDetailResponse.ResortAmenity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PublicPostingDetailResponse.ResortAmenity,
                newItem: PublicPostingDetailResponse.ResortAmenity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacilitieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFacilitieBinding.inflate(layoutInflater, parent, false)
        return FacilitieViewHolder(binding)
    }
}