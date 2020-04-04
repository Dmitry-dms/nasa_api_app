package com.dms.nasaapi.ui.image_and_video


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dms.nasaapi.Injection
import com.dms.nasaapi.R
import com.dms.nasaapi.model.image_library.Status
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.image_library_fragment.*


class ImageLibraryFragment : Fragment() {
    private val adapter = ImLAdapter()


    private lateinit var viewModel: ImageLibraryViewModel
    private var sheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val coordinatorLayout = inflater.inflate(R.layout.image_library_fragment,container,false) as CoordinatorLayout
        val filterIcon = coordinatorLayout.findViewById<ImageView>(R.id.icon)
        val contentLayout = coordinatorLayout.findViewById<LinearLayout>(R.id.layout_main)

        sheetBehavior = BottomSheetBehavior.from(contentLayout)

        sheetBehavior?.apply {
            isFitToContents=false
            isHideable = false //prevents the boottom sheet from completely hiding off the screen
            state=BottomSheetBehavior.STATE_EXPANDED //initially state to fully expanded
        }

        filterIcon.setOnClickListener {
            toggleFilters()
        }
        viewModel = ViewModelProviders.of(this, Injection.provideImLViewModelFactory())
            .get(ImageLibraryViewModel::class.java)
        return coordinatorLayout

    }
    private fun toggleFilters() {
        if (sheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            sheetBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
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
            Log.d("TAG2", "size = ${it.size}")
        })
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            Log.d("TAG2", " network state ${it.status}")
            showEmptyList(it.status == Status.FAILED || it.status==Status.EMPTY)
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
        search_repo.setOnKeyListener { it, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
                val imm =
                    it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
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
