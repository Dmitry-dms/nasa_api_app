package com.dms.nasaapi.ui.image_and_video


import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.dms.nasaapi.Injection
import com.dms.nasaapi.R
import com.dms.nasaapi.model.Status
import com.dms.nasaapi.ui.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.image_library_fragment.*


class ImageLibraryFragment : BaseFragment() {
    private val adapter = ImLAdapter()

    override val viewModel: ImageLibraryViewModel
        get() = ViewModelProviders.of(this, Injection.provideImLViewModelFactory())
            .get(ImageLibraryViewModel::class.java)

    private var sheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val coordinatorLayout =
            inflater.inflate(R.layout.image_library_fragment, container, false) as CoordinatorLayout
        val filterIcon = coordinatorLayout.findViewById<LottieAnimationView>(R.id.icon)
        val contentLayout = coordinatorLayout.findViewById<LinearLayout>(R.id.layout_main)

        sheetBehavior = BottomSheetBehavior.from(contentLayout)

        sheetBehavior?.apply {
            isFitToContents = false
            isHideable = false
            state = BottomSheetBehavior.STATE_EXPANDED
        }
        filterIcon.setOnClickListener {
            toggleFilters(it)
        }

        return coordinatorLayout

    }

    private fun toggleFilters(view: View) {
        if (sheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            (view as LottieAnimationView).apply {
                setMinAndMaxFrame(0, 35)
                playAnimation()
            }
            sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED

        } else {
            (view as LottieAnimationView).apply {
                setMinAndMaxFrame(35, 70)
                playAnimation()
            }
            sheetBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        val linearLayoutManager = LinearLayoutManager(context)
        image_lib_rv.layoutManager = linearLayoutManager
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        viewModel.searchImages(query)
        initSearch(query)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
    }

    override fun initAdapter() {
        image_lib_rv.adapter = adapter
        viewModel.items.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            showLoading(it.status == Status.RUNNING)
            showEmptyList(it.status == Status.FAILED || it.status == Status.EMPTY)
        })
    }

    override fun updateListFromInput() {
        search_repo.text?.trim().let {
            if (it!!.isNotEmpty()) {
                image_lib_rv.scrollToPosition(0)
                viewModel.searchImages(it.toString())
                adapter.submitList(null)
            }
        }
    }

    override fun initSearch(query: String) {
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

    override fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList_iml.visibility = View.VISIBLE
            image_lib_rv.visibility = View.GONE
            loading_iml.visibility = View.GONE
        } else {
            emptyList_iml.visibility = View.GONE
            image_lib_rv.visibility = View.VISIBLE
        }
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            loading_iml.resumeAnimation()
            loading_iml.visibility = View.VISIBLE
        } else {
            loading_iml.visibility = View.GONE
            loading_iml.cancelAnimation()
        }
    }


    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Mars"
    }
}
