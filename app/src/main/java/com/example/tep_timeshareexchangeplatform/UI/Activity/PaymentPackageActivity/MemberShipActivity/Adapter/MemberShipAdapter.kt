package com.example.tep_timeshareexchangeplatform.UI.Activity.PaymentPackageActivity.MemberShipActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemMemberShipBinding
import java.text.DecimalFormat

class MemberShipAdapter: BaseAdapter<PackageModel, MemberShipAdapter.MemberShipViewHolder>() {

    var onItemClick: ((PackageModel) -> Unit)? = null

    inner class MemberShipViewHolder(binding: ItemMemberShipBinding): BaseItemViewHolderCF<PackageModel, ItemMemberShipBinding>(binding){
        override fun bind(item: PackageModel) {
            var benefitAdapter = BenefitAdapter()
            benefitAdapter.submitList(item.listBenefit)
            binding.tvTypeMembership.text = item.name
            binding.priceText.text = "${formatPrice(item.price)} VND"
            binding.tvDuration.text = "${ "/ " +item.duration.toString() + " Tháng"}"
            binding.rvBenefits.let {
                it.adapter = benefitAdapter
                it.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(it.context)
            }
            if (item.type == "Gói Tháng") {
                binding.llTypeMembership.setBackgroundResource(R.drawable.lite_gradient)
            } else {
                binding.llTypeMembership.setBackgroundResource(R.drawable.pro_gradient)
            }

            binding.getStartedButton.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }

        fun formatPrice(price: Int): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(price)
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<PackageModel> {
        return object : DiffUtil.ItemCallback<PackageModel>(){
            override fun areItemsTheSame(oldItem: PackageModel, newItem: PackageModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PackageModel, newItem: PackageModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberShipViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMemberShipBinding.inflate(layoutInflater, parent, false)

        return MemberShipViewHolder(binding)
    }
}