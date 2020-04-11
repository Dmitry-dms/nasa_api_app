package com.dms.nasaapi.ui.image_and_video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dms.nasaapi.R
import com.dms.nasaapi.model.image_library.Item
import kotlinx.android.synthetic.main.image_lst_item.view.*

class ImLViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private var item: Item? = null

    fun bind(item: Item) {
        showItemData(item)
    }

    private fun showItemData(item: Item) {
        if (item.data[0].media_type == "image") {//skip video
            this.item = item
            Glide
                .with(view)
                .load(item.links[0].href)
                .into(view.image_lib)
        }
    }

    companion object {
        fun create(parent: ViewGroup): ImLViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.image_lst_item, parent, false)
            return ImLViewHolder(view)
        }
    }
}