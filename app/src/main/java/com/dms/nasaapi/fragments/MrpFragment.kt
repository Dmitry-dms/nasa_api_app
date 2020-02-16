package com.dms.nasaapi.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dms.nasaapi.MainActivityViewModel
import com.dms.nasaapi.R
import kotlinx.android.synthetic.main.fragment_apod.*
import kotlinx.android.synthetic.main.fragment_apod.image
import kotlinx.android.synthetic.main.fragment_mrp.*


class MrpFragment : Fragment() {
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_mrp, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= ViewModelProvider.AndroidViewModelFactory(activity!!.application).create(
            MainActivityViewModel::class.java)
        viewModel?.getMarsPhoto()?.observe(viewLifecycleOwner, Observer {
            if (it!=null) {
                Log.d("TAG","${it[18].image}")
                Glide.with(this)
                    .load(it[18].image)
                    .into(image_mrp)


            }
            else  Log.d("TAG", "error vm mars")
        })


    }


}
