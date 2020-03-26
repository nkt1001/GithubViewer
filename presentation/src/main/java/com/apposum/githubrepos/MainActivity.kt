package com.apposum.githubrepos

import android.os.Bundle
import com.apposum.githubrepos.common.BaseActivity
import com.apposum.githubrepos.databinding.ActivityMainBinding
import com.apposum.githubrepos.reposearch.RepoSearchFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, RepoSearchFragment())
            .addToBackStack(null)
            .commit()
    }
}
