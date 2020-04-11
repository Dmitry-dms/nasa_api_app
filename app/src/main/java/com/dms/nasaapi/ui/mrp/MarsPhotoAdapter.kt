package com.dms.nasaapi.ui.mrp

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.dms.nasaapi.model.mrp.MarsPhoto

class MarsPhotoAdapter :
    PagedListAdapter<MarsPhoto, MarsViewHolder>(REPO_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsViewHolder {
        return MarsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MarsViewHolder, position: Int) {
        val marsPhoto = getItem(position)
        if (marsPhoto !=null){
            holder.bind(marsPhoto)
        }
    }


    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<MarsPhoto>() {
            override fun areItemsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean =
                oldItem.image == newItem.image


            override fun areContentsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean =
                oldItem == newItem

        }
    }
}