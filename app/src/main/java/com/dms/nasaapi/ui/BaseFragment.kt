package com.dms.nasaapi.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment : Fragment() {
    abstract val viewModel: ViewModel

    abstract fun showEmptyList(show: Boolean)
    abstract fun initAdapter()
    abstract fun updateListFromInput()
    abstract fun initSearch(query: String)
    abstract fun showLoading(show: Boolean)
}