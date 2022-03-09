package com.example.focusonpaging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.focusonpaging.R
import com.example.focusonpaging.databinding.ItemProductsBinding
import com.example.focusonpaging.network.model.Hit

class ProductsAdapter : PagingDataAdapter<Hit, ProductsAdapter.ViewHolder>(COMPARATOR) {

    companion object {
        private var isSelectAll = false
        private val COMPARATOR = object : DiffUtil.ItemCallback<Hit>() {
            override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean =
                oldItem.document.handle == newItem.document.handle

            override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean =
                oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun updateCheckBoxes(isAllSelected: Boolean){
        isSelectAll = isAllSelected
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ItemProductsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: Hit) = with(binding) {
            tvItemName.text = repo.document.name
            tvSKUs.text = "SKU: ${repo.document.sku}"
            tvItemPrice.text = "£${repo.document.price_including_tax}"
            tvItemInOutStock.text = "${repo.document.inventory_level} ${repo.document.stock_status}"
            tvItemReOrderPoint.text = repo.document.reorder_point

            checkbox.isChecked = isSelectAll
            if(checkbox.isChecked){
                rowFG.setBackgroundColor(itemView.resources.getColor(R.color.lightGrey))
            }else{
                rowFG.setBackgroundColor(itemView.resources.getColor(R.color.white))
            }
            checkbox.setOnClickListener {
                if(checkbox.isChecked){
                    rowFG.setBackgroundColor(itemView.resources.getColor(R.color.lightGrey))
                }else{
                    rowFG.setBackgroundColor(itemView.resources.getColor(R.color.white))
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ProductsAdapter.ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}