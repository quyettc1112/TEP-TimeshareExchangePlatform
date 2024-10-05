package com.example.tep_timeshareexchangeplatform.Common.Adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.LocationModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemLocationSearchedBinding
import java.text.Normalizer
import java.util.regex.Pattern

class LocationSearchedAdapter : BaseAdapter<LocationModel, LocationSearchedAdapter.LocationSearchedViewHolder>(), Filterable {

    // Store the original unfiltered list
    private var originalList: List<LocationModel> = listOf()
    private var searchQuery: String = "" // Store the search query
    var onItemClickListener: ((LocationModel) -> Unit)? = null
    inner class LocationSearchedViewHolder(binding: ItemLocationSearchedBinding) :
        BaseItemViewHolderCF<LocationModel, ItemLocationSearchedBinding>(binding) {
        override fun bind(item: LocationModel) {
            // Bind and highlight the name and location based on the search query

            binding.cityName.text = item.name
            binding.cityLocation.text = item.location
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(item)
            }

            if (item.type == 1) {
                binding.icon.apply {
                    layoutParams = layoutParams.apply {
                        width = context.resources.getDimensionPixelSize(R.dimen.dp_24) // Đổi 24dp sang pixel
                        height = context.resources.getDimensionPixelSize(R.dimen.dp_24)
                    }
                    setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_location))
                }
            } else {
                binding.icon.apply {
                    layoutParams = layoutParams.apply {
                        width = ViewGroup.LayoutParams.MATCH_PARENT
                        height = ViewGroup.LayoutParams.MATCH_PARENT
                    }
                }
                Glide.with(binding.root.context)
                    .load(item.image)
                    .into(binding.icon)
            }

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
    // Helper function to remove diacritics
    fun String.normalize(): String {
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(temp).replaceAll("").lowercase()
    }

    // Filtering logic for the adapter
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.normalize().orEmpty()

                // Filter the original unfiltered list, but use normalized versions for comparison
                val filteredList = if (query.isEmpty()) {
                    originalList
                } else {
                    originalList.filter {
                        it.name.normalize().contains(query) || it.location.normalize().contains(query)
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                differ.submitList(results?.values as List<LocationModel>)
            }
        }
    }
}