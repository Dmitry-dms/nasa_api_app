package com.dms.nasaapi.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dms.nasaapi.R
import com.dms.nasaapi.model.PictureOfTheDay
import kotlinx.android.synthetic.main.fragment_apod.*


class ApodFragment : Fragment() {
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_apod, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= ViewModelProvider.AndroidViewModelFactory(activity!!.application).create(
            MainActivityViewModel::class.java)
        viewModel.getApod()?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                displayData(it)
                Log.d("TAG","success vm")
            }
            else  {
                Log.d("TAG", "error vm")

            }

        })
    }

    private fun displayData(list: PictureOfTheDay) {
        date.text = list.date
        title_api.text = list.title
        explanation.text = list.explanation
        Glide.with(this)
            .load(list.picture)
            .into(image)
    }
}
