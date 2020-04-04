package com.dms.nasaapi.ui.epic

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dms.nasaapi.Injection
import com.dms.nasaapi.R
import com.dms.nasaapi.model.image_library.NetworkState
import com.dms.nasaapi.model.image_library.Status
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val linearLayoutManager = LinearLayoutManager(context)
        epic_rv.layoutManager=linearLayoutManager
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        epic_rv.addItemDecoration(decoration)

        initAdapter()
        viewModel.search("2020-03-03")
        initSearch("2020-03-03")
    }

    private fun initAdapter(){
        epic_rv.adapter=adapter
        viewModel.pagedList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        viewModel.state.observe(viewLifecycleOwner, Observer {
            showEmptyList(it.status == Status.FAILED || it.status==Status.EMPTY)
        })
    }
    private fun showEmptyList(show: Boolean) {
        if (show) {
            tv_error_epic.visibility = View.VISIBLE
            epic_rv.visibility = View.GONE
        } else {
            tv_error_epic.visibility = View.GONE
            epic_rv.visibility = View.VISIBLE
        }
    }
    private fun updateListFromInput() {
        editDate.text.trim().let {
            if (it.isNotEmpty()) {
                epic_rv.scrollToPosition(0)
                viewModel.search(it.toString())
                adapter.submitList(null)
                showEmptyList(false)
            }
        }
    }
    private fun initSearch(date:String){
        editDate.setOnKeyListener{ it, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
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

}
