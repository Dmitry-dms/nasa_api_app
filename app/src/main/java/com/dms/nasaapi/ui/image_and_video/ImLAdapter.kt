package com.dms.nasaapi.ui.image_and_video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.dms.nasaapi.R
import com.dms.nasaapi.model.image_library.Item


class ImLAdapter: PagedListAdapter<Item,ImLViewHolder>(REPO_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImLViewHolder {

        return ImLViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ImLViewHolder, position: Int) {
        val items = getItem(position)
        items?.let {
            (holder as ImLViewHolder).bind(it)
        }

    }
    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem.links[0].href == newItem.links[0].href


            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem == newItem

        }
    }
}