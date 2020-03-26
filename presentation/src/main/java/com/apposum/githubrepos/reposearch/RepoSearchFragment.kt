package com.apposum.githubrepos.reposearch

import android.os.Bundle
import android.view.View
import com.apposum.githubrepos.R
import com.apposum.githubrepos.common.BaseFragment
import com.apposum.githubrepos.databinding.FragmentRepoSearchBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RepoSearchFragment : BaseFragment<FragmentRepoSearchBinding>() {

    private val viewModel: RepoSearchViewModel by sharedViewModel()

    override fun getLayoutId(): Int = R.layout.fragment_repo_search

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}