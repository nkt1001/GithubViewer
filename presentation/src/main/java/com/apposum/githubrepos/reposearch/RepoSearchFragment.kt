package com.apposum.githubrepos.reposearch

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.apposum.githubrepos.R
import com.apposum.githubrepos.common.BaseFragment
import com.apposum.githubrepos.common.PaginationListener
import com.apposum.githubrepos.databinding.FragmentRepoSearchBinding
import com.apposum.githubrepos.entity.Data
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RepoSearchFragment : BaseFragment<FragmentRepoSearchBinding>() {

    private val viewModel: RepoSearchViewModel by sharedViewModel()
    private val listAdapter = RepoSearchResultAdapter()

    override fun getLayoutId(): Int = R.layout.fragment_repo_search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.searchToolbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.findGithubRepository(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        binding.searchResultList.adapter = listAdapter
        binding.searchResultList.addOnScrollListener(object : PaginationListener(binding.searchResultList.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                viewModel.loadNextPage()
            }
            override fun isLastPage(): Boolean = viewModel.lastSearchResult?.items.isNullOrEmpty()
            override fun isLoading(): Boolean = binding.swipeRefreshLayout.isRefreshing
        })

        viewModel.loadLastSearchResult()
    }

    override fun onStart() {
        super.onStart()

        viewModel.reposLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Data.Error -> {
                    it.error.message?.let { showSnackbar { it } }
                }
                is Data.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = it.isLoading
                }
                is Data.Success -> {
                    binding.searchToolbar.setQuery(viewModel.lastSearchResult?.searchQuery, false)

                    val repos = it.data?.items ?: emptyList()

                    val currentPage = (it.data?.nextPage ?: 0) - 1
                    if (currentPage > 1) {
                        listAdapter.currentList.addAll(repos)
                    } else {
                        it.data?.items?.let { list ->
                            listAdapter.submitList(list)
                        }
                    }
                }
            }
        })
    }
}