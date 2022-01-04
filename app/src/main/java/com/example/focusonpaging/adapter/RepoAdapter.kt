package com.example.focusonpaging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.focusonpaging.databinding.ItemReposBinding
import com.example.focusonpaging.network.model.Repo

class RepoAdapter: PagingDataAdapter<Repo, RepoAdapter.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                oldItem.fullName == newItem.fullName

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                oldItem == newItem
        }
    }

    class ViewHolder(
        private val binding: ItemReposBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: Repo) = with(binding) {
            tvItemRepos.text = repo.fullName
        }
    }

    override fun onBindViewHolder(holder: RepoAdapter.ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)  =  ViewHolder(
        ItemReposBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}