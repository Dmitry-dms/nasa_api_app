package com.dms.nasaapi.ui.mrp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.dms.nasaapi.Injection
import com.dms.nasaapi.R
import com.dms.nasaapi.model.Status
import com.dms.nasaapi.ui.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_mrp.*


class MrpFragment : BaseFragment() {
    private val adapter = MarsPhotoAdapter()
    override val viewModel: MrpViewModel
        get() = ViewModelProvider(this, Injection.provideViewModelFactory(context!!)).get(
            MrpViewModel::class.java
        )
    private var sheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val coordinatorLayout =
            inflater.inflate(R.layout.fragment_mrp, container, false) as CoordinatorLayout
        val filterIcon = coordinatorLayout.findViewById<ImageView>(R.id.filterIcon)
        val contentLayout =
            coordinatorLayout.findViewById<LinearLayout>(R.id.contentLayout)
        sheetBehavior = BottomSheetBehavior.from(contentLayout)
        sheetBehavior?.apply {
            isFitToContents = false
            isHideable = false
            state = BottomSheetBehavior.STATE_EXPANDED
            peekHeight = 170
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
            sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        recycler_view_mrp.layoutManager = linearLayoutManager

        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        viewModel.searchMrp(query)
        initSearch(query)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
    }

    override fun initAdapter() {
        recycler_view_mrp.adapter = adapter
        viewModel.marsPhotoPagedList.observe(viewLifecycleOwner, Observer {
            showLoading(it.isEmpty())
            adapter.submitList(it)
        })
        viewModel.networkErrors.observe(viewLifecycleOwner, Observer {
            showEmptyList(it.status == Status.FAILED || it.status == Status.EMPTY)
        })
    }

    override fun initSearch(query: String) {
        updateListFromInput()
    }

    override fun updateListFromInput() {
        recycler_view_mrp.scrollToPosition(0)
        viewModel.searchMrp(DEFAULT_QUERY)
        adapter.submitList(null)

    }

    override fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            recycler_view_mrp.visibility = View.GONE
            loading_mrp.visibility = View.GONE
            loading_mrp.cancelAnimation()
        } else {
            emptyList.visibility = View.GONE
            recycler_view_mrp.visibility = View.VISIBLE
        }
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            loading_mrp.resumeAnimation()
            loading_mrp.visibility = View.VISIBLE
            recycler_view_mrp.visibility = View.GONE
        } else {
            loading_mrp.visibility = View.GONE
            loading_mrp.cancelAnimation()
            recycler_view_mrp.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "25"
    }
}





