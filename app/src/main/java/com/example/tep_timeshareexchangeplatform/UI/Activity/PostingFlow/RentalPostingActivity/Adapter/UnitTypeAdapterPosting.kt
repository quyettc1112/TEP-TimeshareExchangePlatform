package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemResortRoomTypeBinding

class UnitTypeAdapterPosting(private val showFullInfo: Boolean): BaseAdapter<UnitTypeModel, UnitTypeAdapterPosting.RoomTypeViewHolder>() {

    var onItemClick: ((UnitTypeModel) -> Unit)? = null
    var onButtonBookClick: ((UnitTypeModel) -> Unit)? = null

    // Lưu trữ vị trí của item đã chọn
    private var selectedPosition: Int = -1

    inner class RoomTypeViewHolder(binding: ItemResortRoomTypeBinding): BaseItemViewHolderCF<UnitTypeModel, ItemResortRoomTypeBinding>(binding) {

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

        override fun bind(item: UnitTypeModel) {
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

                // Cập nhật viền của item dựa trên việc nó có được chọn hay không
                if (position == selectedPosition) {
                    // Nếu được chọn, làm sáng viền
                    root.setBackgroundResource(R.drawable.selected_item_border)  // Tạo một drawable cho viền sáng
                } else {
                    // Nếu không được chọn, để viền mặc định
                    root.setBackgroundResource(R.drawable.default_item_border)  // Drawable cho viền mặc định
                }

                // Sự kiện click item
                binding.root.setOnClickListener {
                    // Cập nhật vị trí của item đã chọn
                    val previousPosition = selectedPosition
                    selectedPosition = position

                    // Thông báo thay đổi item cũ và mới để cập nhật giao diện
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(position)

                    onItemClick?.invoke(item)
                }

                binding.btnViewRoom.setOnClickListener {
                    onButtonBookClick?.invoke(item)
                }

                binding.btnViewRoom.visibility = ViewGroup.VISIBLE

                // Hide Price
                binding.tvPrice.visibility = ViewGroup.GONE
                binding.llAmennities.visibility = ViewGroup.GONE
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<UnitTypeModel> {
        return object : DiffUtil.ItemCallback<UnitTypeModel>() {
            override fun areItemsTheSame(oldItem: UnitTypeModel, newItem: UnitTypeModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UnitTypeModel, newItem: UnitTypeModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomTypeViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding = ItemResortRoomTypeBinding.inflate(layoutInflater, parent, false)
        return RoomTypeViewHolder(binding)
    }

}