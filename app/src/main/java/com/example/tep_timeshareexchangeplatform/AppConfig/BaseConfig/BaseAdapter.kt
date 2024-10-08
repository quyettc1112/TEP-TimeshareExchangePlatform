package com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : Any, VH : BaseItemViewHolderCF<T, *>> : RecyclerView.Adapter<VH>() {

    private var onItemClickListener: ((T) -> Unit)? = null

    private var setItemOrderBy: ((List<T>) -> Unit)? = null

    protected abstract fun differCallBack(): DiffUtil.ItemCallback<T>

    val differ = AsyncListDiffer(this, differCallBack())

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        onItemClickListener?.let { listener ->
            holder.itemView.setOnClickListener { _ ->
                listener(item)
            }
        }
    }
    open fun submitList(list: List<T>?) {
        setItemOrderBy?.let {
            list?.let { it1 -> it(it1.toList()) }
        }
        differ.submitList(list?.toList())
        notifyDataSetChanged()
    }

    fun setItemOrderBy(listener: (List<T>) -> Unit) {
        setItemOrderBy = listener
    }

    fun setItemOnclickListener(listener: (T) -> Unit) {
        onItemClickListener = listener
    }

}

