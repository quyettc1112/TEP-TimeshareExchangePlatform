package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MapViewActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map.OverpassResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MapsAmenityType
import com.example.tep_timeshareexchangeplatform.databinding.DialogNearbyLocationBinding
import com.example.tep_timeshareexchangeplatform.databinding.ItemNearbyLocationBinding

class NearByAdapter : BaseAdapter<OverpassResponse.Element, NearByAdapter.NearByViewHolder>(){

    var onItemClickListener: ((OverpassResponse.Element) -> Unit)? = null

    inner class NearByViewHolder(binding: ItemNearbyLocationBinding) :
        BaseItemViewHolderCF<OverpassResponse.Element, ItemNearbyLocationBinding>(binding) {
        override fun bind(item: OverpassResponse.Element) {
            // Lấy giá trị từ item (amenity, shop hoặc bus_stop)
            val amenityType = MapsAmenityType.fromValue((item.tags.amenity ?: item.tags.shop ?: item.tags.highway).toString())

            // Thiết lập icon nếu tìm thấy
            if (amenityType != null) {
                binding.ivIcon.setImageResource(amenityType.iconResId) // ivIcon là ImageView trong layout
            } else {
                binding.ivIcon.setImageResource(R.drawable.baseline_location_pin_24_blue) // Icon mặc định nếu không tìm thấy
            }

            // Hiển thị tên
            binding.tvName.text = item.tags.name ?: "Unknown Location"
            binding.tvType.text = amenityType?.getDisplayName(binding.root.context) ?: ""

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(item)
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<OverpassResponse.Element> {
        return object : DiffUtil.ItemCallback<OverpassResponse.Element>() {
            override fun areItemsTheSame(
                oldItem: OverpassResponse.Element,
                newItem: OverpassResponse.Element
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: OverpassResponse.Element,
                newItem: OverpassResponse.Element
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearByViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNearbyLocationBinding.inflate(layoutInflater, parent, false)
        return NearByViewHolder(binding)
    }
}