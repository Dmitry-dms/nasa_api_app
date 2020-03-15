package com.dms.nasaapi.ui.mrp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dms.nasaapi.Injection
import com.dms.nasaapi.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_mrp.*


class MrpFragment : Fragment() {


    private val adapter = MarsPhotoAdapter()
    private lateinit var viewModel: MrpViewModel
    private var sheetBehavior: BottomSheetBehavior<LinearLayout>? = null



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val coordinatorLayout= inflater.inflate(R.layout.fragment_mrp, container, false) as CoordinatorLayout
        val filterIcon = coordinatorLayout.findViewById<ImageView>(R.id.filterIcon)
        val contentLayout =
            coordinatorLayout.findViewById<LinearLayout>(R.id.contentLayout)
        sheetBehavior = BottomSheetBehavior.from(contentLayout)
        (sheetBehavior as BottomSheetBehavior<LinearLayout>?)?.setFitToContents(false)
        (sheetBehavior as BottomSheetBehavior<LinearLayout>?)?.setHideable(false) //prevents the boottom sheet from completely hiding off the screen

        (sheetBehavior as BottomSheetBehavior<LinearLayout>?)?.setState(BottomSheetBehavior.STATE_EXPANDED) //initially state to fully expanded


        filterIcon.setOnClickListener {
            toggleFilters()
        }
        // get the view model
         viewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory(context!!)
        ).get(MrpViewModel::class.java)

        return coordinatorLayout
    }
    private fun toggleFilters() {
        if (sheetBehavior?.state === BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            sheetBehavior?.peekHeight=resources.displayMetrics.heightPixels-300

        } else {
            sheetBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        recycler_view_mrp.layoutManager=linearLayoutManager
        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recycler_view_mrp.addItemDecoration(decoration)


        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        viewModel.searchMrp(query)
        initSearch(query)


    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
    }

    private fun initAdapter() {
        recycler_view_mrp.adapter = adapter
        viewModel.marsPhotoPagedList.observe(viewLifecycleOwner, Observer {
            Log.d("TAG3", "list: ${it?.size}")
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
        })
        viewModel.networkErrors.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "\uD83D\uDE28 Wooops $it", Toast.LENGTH_LONG).show()
        })

    }

    private fun initSearch(query: String) {
        updateMarsListFromInput(query)
//        search_repo.setText(query)
//
//        search_repo.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_GO) {
//                updateRepoListFromInput()
//                true
//            } else {
//                false
//            }
//        }
//        search_repo.setOnKeyListener { _, keyCode, event ->
//            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                updateRepoListFromInput()
//                true
//            } else {
//                false
//            }
//        }
    }

    private fun updateMarsListFromInput(query: String) {
        recycler_view_mrp.scrollToPosition(0)
        viewModel.searchMrp(query)
        adapter.submitList(null)

    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            recycler_view_mrp.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            recycler_view_mrp.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "25"
    }
}





