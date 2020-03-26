package com.apposum.githubrepos.common

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.apposum.githubrepos.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<Binding : ViewDataBinding> : Fragment() {

    protected lateinit var binding: Binding

    protected var toolbar: Toolbar? = null

    @LayoutRes
    abstract fun getLayoutId(): Int

    open fun setupToolbar(): Toolbar? = null

    open fun setTitle(@StringRes title: Int) {
        toolbar?.setTitle(title)
    }

    protected inline fun showSnackbar(message: () -> String) {
        Snackbar.make(binding.root, message(), Snackbar.LENGTH_LONG).show()
    }

    protected fun showSnackbar(message: () -> String, completion: () -> Unit) {
        Snackbar.make(binding.root, message(), Snackbar.LENGTH_LONG).addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                completion()
            }
        }).show()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        toolbar = setupToolbar()

        return binding.root
    }

    protected fun hideSoftKeyBoard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (imm.isAcceptingText) {
            imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        }

        activity?.currentFocus?.clearFocus()
    }

    protected fun showGenericError(t: Throwable) {
        Log.e("BaseFragment", t.toString())
        showSnackbar { getString(R.string.genericError) }
    }

    protected fun setStatusBarColor(color: Int) {
        activity?.let {
            it.window?.statusBarColor = ContextCompat.getColor(it, color)
        }
    }
}