package com.example.tep_timeshareexchangeplatform.Common.Adapter.SpannedGridLayoutManager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.DestinationModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.GridItemLayoutBinding

class GridAdapter(
    private val destinationModels: List<DestinationModel>,
    private val onItemClick: (DestinationModel) -> Unit
) : RecyclerView.Adapter<GridAdapter.DataViewHolder>() {

    class DataViewHolder(private val binding: GridItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(destinationModel: DestinationModel, onItemClick: (DestinationModel) -> Unit) {
            binding.apply {
                Glide.with(imageViewAvatar.context)
                    .load(destinationModel.destinationImage)
                    .into(imageViewAvatar)

                tvDestinationName.text = destinationModel.destinationName

                // Thiết lập sự kiện click cho root view của item
                root.setOnClickListener {
                    onItemClick(destinationModel) // Gọi lambda khi item được click
                }

               // executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: GridItemLayoutBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.grid_item_layout, parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int = destinationModels.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(destinationModels[position], onItemClick)


}