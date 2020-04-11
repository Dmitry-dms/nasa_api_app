package com.dms.nasaapi.ui.apod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.dms.nasaapi.Injection
import com.dms.nasaapi.R
import com.dms.nasaapi.model.Status
import com.dms.nasaapi.model.apod.PictureOfTheDay
import com.dms.nasaapi.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_apod.*


class ApodFragment : BaseFragment() {
    override val viewModel: ApodViewModel
        get() = ViewModelProviders.of(this, Injection.provideApodViewModelFactory(context!!))
            .get(ApodViewModel::class.java)

    override fun initAdapter() {
        viewModel.result.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                displayData(it)
                viewModel.clearCoroutineJob()
            }
        })
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            showLoading(it.status == Status.RUNNING)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_apod, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun displayData(list: PictureOfTheDay) {
        date.text = list.date
        title_api.text = list.title
        explanation.text = list.explanation
        Glide.with(this)
            .load(list.picture)
            .into(image)
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            loading_apod.resumeAnimation()
            loading_apod.visibility = View.VISIBLE
            content_apod.visibility = View.GONE
        } else {
            loading_apod.visibility = View.GONE
            loading_apod.cancelAnimation()
            content_apod.visibility = View.VISIBLE
        }
    }

    override fun initSearch(query: String) {}

    override fun updateListFromInput() {}

    override fun showEmptyList(show: Boolean) {}
}
