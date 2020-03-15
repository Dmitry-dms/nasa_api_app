package com.dms.nasaapi.ui.epic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dms.nasaapi.Injection
import com.dms.nasaapi.R
import kotlinx.android.synthetic.main.epic_fragment.*

class EpicFragment : Fragment() {


    private lateinit var viewModel: EpicViewModel
    private val adapter = EpicAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, Injection.provideEpicViewModelFactory(context!!)).get(EpicViewModel::class.java)
        return inflater.inflate(R.layout.epic_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val linearLayoutManager = LinearLayoutManager(context)
        epic_rv.layoutManager=linearLayoutManager
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        epic_rv.addItemDecoration(decoration)

        initAdapter()
        viewModel.search("2020-03-06")
    }

    private fun initAdapter(){
        epic_rv.adapter=adapter
        viewModel.pagedList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        viewModel.state.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context,"${it.msg}",Toast.LENGTH_SHORT).show()
        })
    }

}
