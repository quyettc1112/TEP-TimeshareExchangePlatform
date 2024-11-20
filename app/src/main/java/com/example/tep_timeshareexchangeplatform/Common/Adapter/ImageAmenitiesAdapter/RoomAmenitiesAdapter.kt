package com.example.tep_timeshareexchangeplatform.Common.Adapter.ImageAmenitiesAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RoomAmenityDB
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.databinding.ItemFacilitieBinding

class RoomAmenitiesAdapter : BaseAdapter<AmenitiesModel, RoomAmenitiesAdapter.ImageAmenitiesAdapter>() {
    private var originalList = listOf<AmenitiesModel>() // Dữ liệu gốc
    private var filteredList = listOf<AmenitiesModel>() // Dữ liệu đã lọc

    inner class ImageAmenitiesAdapter(binding: ItemFacilitieBinding): BaseItemViewHolderCF<AmenitiesModel, ItemFacilitieBinding>(binding) {
        override fun bind(item: AmenitiesModel) {
            binding.apply {
                tvFacilitieName.text = item.name
                // Lấy hình ảnh từ RoomAmenityDB dựa trên tên
                val amenityIcon = RoomAmenityDB.values().find { it.model.name == item.name }?.imageResId
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAmenitiesAdapter {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFacilitieBinding.inflate(layoutInflater, parent, false)
        return ImageAmenitiesAdapter(binding)
    }
    fun submitOriginalList(list: List<AmenitiesModel>) {
        originalList = list
        filteredList = list // Ban đầu lọc toàn bộ
        submitList(filteredList)
    }

    fun filterByAmenityTypes(vararg types: AmenityType) {
        val validTypes = types.map { it.name } // Lấy tên của các loại từ enum
        filteredList = originalList.filter {
            validTypes.contains(it.type.uppercase()) // So sánh dựa trên giá trị `type`
        }
        submitList(filteredList)
    }
}