package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.ResortAmenityDB
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RoomAmenityDB
import com.example.tep_timeshareexchangeplatform.databinding.ItemFacilitieBinding

class ResortAmenityAdapter : BaseAdapter<AmenitiesModel, ResortAmenityAdapter.ResortAmenityViewHolder>() {
    private var originalList = listOf<AmenitiesModel>() // Dữ liệu gốc
    private var filteredList = listOf<AmenitiesModel>()

    inner class ResortAmenityViewHolder(binding: ItemFacilitieBinding) : BaseItemViewHolderCF<AmenitiesModel, ItemFacilitieBinding>(binding) {
        override fun bind(item: AmenitiesModel) {
            binding.apply {
                if(item.isChecked) {
                    tvFacilitieName.text = item.name + "( Có trả phí )"
                } else {
                    tvFacilitieName.text = item.name
                }

                // Lấy hình ảnh từ RoomAmenityDB dựa trên tên
                val amenityIcon = ResortAmenityDB.values().find { it.model.name == item.name }?.imageResId
                    ?: R.drawable.border // Dùng icon mặc định nếu không tìm thấy
                Glide.with(itemView).load(amenityIcon).into(imFacilitie)
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<AmenitiesModel> {
        return object : DiffUtil.ItemCallback<AmenitiesModel>() {
            override fun areItemsTheSame(oldItem: AmenitiesModel, newItem: AmenitiesModel): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: AmenitiesModel, newItem: AmenitiesModel): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResortAmenityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFacilitieBinding.inflate(layoutInflater, parent, false)
        return ResortAmenityViewHolder(binding)
    }
    fun submitOriginalList(list: List<AmenitiesModel>) {
        originalList = list
        filteredList = list // Ban đầu lọc toàn bộ
        submitList(filteredList)
    }

    fun filterByAmenityTypes(vararg types: AmenityType) {
        val validTypes = types.map { it.displayName } // Lấy displayName từ các enum loại đã chỉ định
        filteredList = originalList.filter {
            validTypes.any { validType ->
                it.type.contains(validType, ignoreCase = true)
            }
        }
        submitList(filteredList)
    }
}