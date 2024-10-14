package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.FacilitieModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemFacilitieBinding

class FacilitieAdapter : BaseAdapter<FacilitieModel, FacilitieAdapter.FacilitieViewHolder>() {

    inner class FacilitieViewHolder(binding: ItemFacilitieBinding) : BaseItemViewHolderCF<FacilitieModel, ItemFacilitieBinding>(binding){
        override fun bind(item: FacilitieModel) {
            binding.apply {
                tvFacilitieName.text = item.name
                Glide.with(itemView).load(item.image).into(imFacilitie)

            }
        }

    }

    override fun differCallBack(): DiffUtil.ItemCallback<FacilitieModel> {
        return object : DiffUtil.ItemCallback<FacilitieModel>() {
            override fun areItemsTheSame(oldItem: FacilitieModel, newItem: FacilitieModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FacilitieModel, newItem: FacilitieModel): Boolean {
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