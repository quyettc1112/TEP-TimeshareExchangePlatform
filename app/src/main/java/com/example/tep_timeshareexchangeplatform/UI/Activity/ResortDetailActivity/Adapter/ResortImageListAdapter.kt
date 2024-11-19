package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemResortImageBinding

class ResortImageListAdapter (
    private val listResortImage: List<String>,
    private val onItemClick: (Int) -> Unit
): RecyclerView.Adapter<ResortImageListAdapter.DataViewHolder>() {

    class DataViewHolder(private val binding: ItemResortImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, onItemClick: (Int) -> Unit) {
            binding.apply {
                Glide.with(imageViewAvatar.context)
                    .load(item)
                    .into(imageViewAvatar)
                // Thiết lập sự kiện click cho root view của item
                root.setOnClickListener {
                    onItemClick.invoke(adapterPosition)
                }
            }
            binding.cardViewClose.visibility = ViewGroup.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemResortImageBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_resort_image, parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listResortImage.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =  holder.bind(listResortImage[position], onItemClick)

}