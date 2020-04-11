package com.dms.nasaapi.ui.epic

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.dms.nasaapi.model.epic.Epic

class EpicAdapter : PagedListAdapter<Epic, EpicViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Epic>() {
            override fun areItemsTheSame(oldItem: Epic, newItem: Epic): Boolean =
                oldItem.identifier == newItem.identifier

            override fun areContentsTheSame(oldItem: Epic, newItem: Epic): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpicViewHolder {
    return EpicViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EpicViewHolder, position: Int) {
        val item = getItem(position)
        if (item!=null){
            holder.bind(item)
        }
    }
}