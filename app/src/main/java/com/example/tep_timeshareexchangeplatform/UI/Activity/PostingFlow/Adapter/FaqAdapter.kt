package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.FAQModel
import com.example.tep_timeshareexchangeplatform.databinding.ItemFaqBinding

class FaqAdapter: BaseAdapter<FAQModel, FaqAdapter.FaqViewHolder>() {

    inner class FaqViewHolder(binding: ItemFaqBinding) :BaseItemViewHolderCF<FAQModel,ItemFaqBinding >(binding) {
        override fun bind(item: FAQModel) {

            binding.titleTv.text = item.title
            binding.langDesc.text = item.desc
            binding.langDesc.visibility = if (item.isExpandable) {
                View.VISIBLE
            } else {
                View.GONE
            }


            binding.root.setOnClickListener {
               // isAnyItemExpanded(adapterPosition)
                item.isExpandable = !item.isExpandable
                notifyItemChanged(adapterPosition , Unit)
            }
        }

        private fun isAnyItemExpanded(position: Int){
            val temp = differ.currentList.indexOfFirst {
                it.isExpandable
            }
            if (temp >= 0 && temp != position){
                differ.currentList[temp].isExpandable = false
                notifyItemChanged(temp , 0)
            }
        }


    }

    override fun differCallBack(): DiffUtil.ItemCallback<FAQModel> {
        return object : DiffUtil.ItemCallback<FAQModel>() {
            override fun areItemsTheSame(oldItem: FAQModel, newItem: FAQModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FAQModel, newItem: FAQModel): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFaqBinding.inflate(layoutInflater, parent, false)
        return FaqViewHolder(binding)
    }
}