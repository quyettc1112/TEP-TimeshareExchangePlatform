package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.databinding.ItemAmenitiesBinding

class AmenitiesAdapter: BaseAdapter<AmenitiesModel, AmenitiesAdapter.AmenitiesViewHolder>() {
    var onItemChecked: (AmenitiesModel) -> Unit = {}

    inner class AmenitiesViewHolder(binding: ItemAmenitiesBinding) : BaseItemViewHolderCF<AmenitiesModel, ItemAmenitiesBinding>(binding) {
        override fun bind(item: AmenitiesModel) {
            binding.checkBoxItem.text = item.name

            // Reset listener trước khi cập nhật trạng thái để tránh lỗi không mong muốn
            binding.checkBoxItem.setOnCheckedChangeListener(null)

            // Thiết lập trạng thái checkbox từ thuộc tính isChecked
            binding.checkBoxItem.isChecked = item.isChecked

            // Gán lại listener sau khi trạng thái được ImageAmenitiesAdapter lập
            binding.checkBoxItem.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                onItemChecked(item)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmenitiesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAmenitiesBinding.inflate(layoutInflater, parent, false)
        return AmenitiesViewHolder(binding)
    }
    fun getCheckedItems(): List<AmenitiesModel> {
        return differ.currentList.filter { it.isChecked }
    }
    fun updateCheckedItemsFromList(inputList: List<AmenitiesModel>) {
        // Lấy danh sách hiện tại trong Adapter
        val currentList = differ.currentList

        // Cập nhật trạng thái isChecked cho các mục trong Adapter
        val updatedList = currentList.map { currentItem ->
            if (inputList.any { it.name == currentItem.name }) {
                currentItem.copy(isChecked = true) // Đặt isChecked = true nếu có trong inputList
            } else {
                currentItem
            }
        }

        // Cập nhật danh sách mới cho Adapter
        differ.submitList(updatedList)
    }


}