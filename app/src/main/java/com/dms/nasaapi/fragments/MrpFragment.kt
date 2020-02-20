package com.dms.nasaapi.fragments

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dms.nasaapi.MainActivityViewModel
import com.dms.nasaapi.MrpViewModel
import com.dms.nasaapi.R
import com.dms.nasaapi.paging.MarsPhotoAdapter
import kotlinx.android.synthetic.main.fragment_mrp.*


class MrpFragment : Fragment() {
   // private lateinit var viewModel: MainActivityViewModel

    lateinit var adapter: MarsPhotoAdapter
    lateinit var viewModel: MrpViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        viewModel=ViewModelProvider.AndroidViewModelFactory(activity!!.application).create(MrpViewModel::class.java)

        return inflater.inflate(R.layout.fragment_mrp, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter= MarsPhotoAdapter()
        recycler_view_mrp.layoutManager=LinearLayoutManager(context)


        viewModel.marsPhotoPagedList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        recycler_view_mrp.adapter=adapter
    }

}





