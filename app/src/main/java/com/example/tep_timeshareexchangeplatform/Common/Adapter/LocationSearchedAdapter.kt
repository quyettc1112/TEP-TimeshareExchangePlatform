package com.example.tep_timeshareexchangeplatform.Common.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.LocationModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemLocationSearchedBinding

class LocationSearchedAdapter : BaseAdapter<LocationModel, LocationSearchedAdapter.LocationSearchedViewHolder>(), Filterable {

    // Store the original unfiltered list
    private var originalList: List<LocationModel> = listOf()

    // Inner ViewHolder class
    inner class LocationSearchedViewHolder(binding: ItemLocationSearchedBinding) :
        BaseItemViewHolderCF<LocationModel, ItemLocationSearchedBinding>(binding) {
        override fun bind(item: LocationModel) {
            binding.cityName.text = item.name
            binding.cityLocation.text = item.location
        }
    }

    // DiffUtil callback for differ
    override fun differCallBack(): DiffUtil.ItemCallback<LocationModel> {
        return object : DiffUtil.ItemCallback<LocationModel>() {
            override fun areItemsTheSame(oldItem: LocationModel, newItem: LocationModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: LocationModel, newItem: LocationModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    // Create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationSearchedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationSearchedBinding.inflate(layoutInflater, parent, false)
        return LocationSearchedViewHolder(binding)
    }

    // Update original list and submit it to differ
    fun submitOriginalList(list: List<LocationModel>) {
        originalList = list // Save original unfiltered list
        differ.submitList(list) // Set initial list to differ
    }

    // Filtering logic for the adapter
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase()?.trim()

                // Filter the original unfiltered list, not differ.currentList
                val filteredList = if (query.isNullOrEmpty()) {
                    originalList
                } else {
                    originalList.filter {
                        it.name.lowercase().contains(query) || it.location.lowercase().contains(query)
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // Submit the filtered list to the AsyncListDiffer
                differ.submitList(results?.values as List<LocationModel>)
            }
        }
    }
}