package com.apposum.githubrepos.reposearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apposum.githubrepos.R
import com.apposum.githubrepos.databinding.ListItemRepoBinding
import com.apposum.githubrepos.entity.Repo


private val diffUtil = object : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem == newItem
}

class RepoSearchResultAdapter : ListAdapter<Repo, RepoSearchResultAdapter.RepoSearchViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoSearchViewHolder {
        val binding = DataBindingUtil.inflate<ListItemRepoBinding>(LayoutInflater.from(parent.context), R.layout.list_item_repo, parent, false)
        return RepoSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoSearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RepoSearchViewHolder(private val binding: ListItemRepoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataItem: Repo) {
            binding.listItem = dataItem
        }
    }
}