package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.PostingFragment.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.IntroSliderModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemSliderIntroductionBinding

class IntroSliderAdapter: BaseAdapter<IntroSliderModel, IntroSliderAdapter.IntroSliderViewHolder>() {
    inner class IntroSliderViewHolder(binding: ItemSliderIntroductionBinding)
        : BaseItemViewHolderCF<IntroSliderModel, ItemSliderIntroductionBinding>(binding) {
        override fun bind(item: IntroSliderModel) {
            binding.apply {
                introTitle.text = item.title
                introDescription.text = item.description
                introImage.setImageResource(item.image)
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<IntroSliderModel> {
        return object : DiffUtil.ItemCallback<IntroSliderModel>() {
            override fun areItemsTheSame(oldItem: IntroSliderModel, newItem: IntroSliderModel): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: IntroSliderModel, newItem: IntroSliderModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSliderViewHolder {
        val layoutInflate = LayoutInflater.from(parent.context)
        val binding = ItemSliderIntroductionBinding.inflate(layoutInflate, parent, false)
        return IntroSliderViewHolder(binding)
    }
}