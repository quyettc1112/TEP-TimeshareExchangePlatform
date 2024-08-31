package com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseItemViewHolderCF<T : Any, VB : ViewBinding>(val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {

    val itemContext: Context = binding.root.context
    abstract fun bind(item: T)

}