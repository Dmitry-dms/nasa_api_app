package com.dms.nasaapi.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dms.nasaapi.R
import com.dms.nasaapi.model.MarsPhoto
import kotlinx.android.synthetic.main.list_item.view.*

class MarsPhotoAdapter:PagedListAdapter<MarsPhoto, MarsPhotoAdapter.MarsViewHolder>(REPO_COMPARATOR) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarsPhotoAdapter.MarsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MarsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarsPhotoAdapter.MarsViewHolder, position: Int) {
        val repo = getItem(position)
        repo?.let {
            holder.setData(repo)
        }
    }

    class MarsViewHolder(val view: View) :RecyclerView.ViewHolder(view){
        fun setData(marsPhoto: MarsPhoto){
            view.earth_date.text=marsPhoto.earthDate
            Glide.with(view)
                .load(marsPhoto.image)
                .into(view.image_rv_mars)
        }

    }
    companion object{
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<MarsPhoto>() {
            override fun areItemsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean =
                oldItem.image==newItem.image



            override fun areContentsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean = oldItem == newItem

        }
    }
}