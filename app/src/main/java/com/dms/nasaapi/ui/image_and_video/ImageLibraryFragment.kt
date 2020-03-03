package com.dms.nasaapi.ui.image_and_video


import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dms.nasaapi.Injection

import com.dms.nasaapi.R
import kotlinx.android.synthetic.main.fragment_mrp.*
import kotlinx.android.synthetic.main.image_library_fragment.*

class ImageLibraryFragment : Fragment() {
    private val adapter = ImLAdapter()


    private lateinit var viewModel: ImageLibraryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this,Injection.provideImLViewModelFactory()).get(ImageLibraryViewModel::class.java)
        return inflater.inflate(R.layout.image_library_fragment, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(context)
        image_lib_rv.layoutManager = linearLayoutManager
        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        viewModel.searchImages(query)
        initSearch(query)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
    }

    private fun initAdapter() {
        image_lib_rv.adapter = adapter
        viewModel.items.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
           // Log.d("TAG2","${viewModel.isEmpty.value}")
        })
        viewModel.networkErrors.observe(viewLifecycleOwner, Observer<String> {
            Log.d("TAG2", it)
            Toast.makeText(context, "\uD83D\uDE28 Wooops $it", Toast.LENGTH_LONG).show()
        })
        viewModel.isEmpty.observe(viewLifecycleOwner, Observer {

        })

    }

    private fun updateListFromInput() {
        search_repo.text.trim().let {
            if (it.isNotEmpty()) {
                image_lib_rv.scrollToPosition(0)
                viewModel.searchImages(it.toString())
                adapter.submitList(null)

            }
        }
    }
    private fun initSearch(query: String) {
        search_repo.setText(query)

        search_repo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateListFromInput()
                true
            } else {
                false
            }
        }
        search_repo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateListFromInput()
                true
            } else {
                false
            }
        }
    }
    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList_iml.visibility = View.VISIBLE
            image_lib_rv.visibility = View.GONE
        } else {
            emptyList_iml.visibility = View.GONE
            image_lib_rv.visibility = View.VISIBLE
        }
    }
    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Mars"
    }
}
