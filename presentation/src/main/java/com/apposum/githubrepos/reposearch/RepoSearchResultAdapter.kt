package com.apposum.githubrepos.reposearch

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apposum.githubrepos.entity.Repo


private val diffUtil = object : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem == newItem
}

class RepoSearchResultAdapter(var repoList: List<Repo>) : ListAdapter<Repo, RepoSearchResultAdapter.RepoSearchViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoSearchViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RepoSearchViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class RepoSearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}