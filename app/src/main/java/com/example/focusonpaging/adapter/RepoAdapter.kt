package com.example.focusonpaging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.focusonpaging.databinding.ItemReposBinding
import com.example.focusonpaging.network.model.Hit

class RepoAdapter : PagingDataAdapter<Hit, RepoAdapter.ViewHolder>(COMPARATOR) {

    companion object {
        private var isSelectAll = false
        private val COMPARATOR = object : DiffUtil.ItemCallback<Hit>() {
            override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean =
                oldItem.document.handle == newItem.document.handle

            override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean =
                oldItem == newItem
        }
    }

    fun updateCheckBoxes(isAllSelected: Boolean){
        isSelectAll = isAllSelected
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ItemReposBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: Hit) = with(binding) {
            tvItemRepos.text = repo.document.name
            checkbox.isChecked = isSelectAll
        }
    }

    override fun onBindViewHolder(holder: RepoAdapter.ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemReposBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}