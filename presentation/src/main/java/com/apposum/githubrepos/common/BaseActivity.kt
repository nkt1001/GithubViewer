package com.apposum.githubrepos.common

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.apposum.githubrepos.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity<Binding : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var binding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, getLayoutId())
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val arguments = intent.extras
        if (arguments != null) {
            onExtractParams(arguments)
        }

    }

    open fun onExtractParams(arguments: Bundle) {

    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    protected inline fun showSnackbar(message: () -> String) {
        Snackbar.make(binding.root, message(), Snackbar.LENGTH_LONG).show()
    }

    fun configureBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun configureCloseButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun showGenericError(t: Throwable? = null) {
        Log.e("BaseActivity", t.toString())
        showSnackbar { getString(R.string.genericError) }
    }

    protected fun hideSoftKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (imm.isAcceptingText) {
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        currentFocus?.clearFocus()
    }
}