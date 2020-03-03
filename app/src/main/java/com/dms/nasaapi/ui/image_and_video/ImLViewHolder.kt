package com.dms.nasaapi.ui.image_and_video

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dms.nasaapi.R
import com.dms.nasaapi.model.image_library.Item
import com.dms.nasaapi.model.mrp.MarsPhoto
import com.dms.nasaapi.ui.mrp.MarsViewHolder
import kotlinx.android.synthetic.main.image_lst_item.view.*

class ImLViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    private val tv: TextView = view.findViewById(R.id.img_lib_tv)
    private var item: Item? = null

    fun bind(item: Item) {
        if (item == null) {
            val resources = itemView.resources
            tv.text = resources.getString(R.string.loading)
        } else {
            showItemData(item)
        }
    }
    private fun showItemData(item: Item){
        if (item.data[0].media_type == "image") {//skip video
            this.item = item
            tv.text = item.data[0].title
            Glide.with(view).load(item.links[0].href).into(view.image_lib)
        }
//        else{
//            tv.text="video"
//        }
    }
    companion object {
        fun create(parent: ViewGroup): ImLViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.image_lst_item,parent,false)
            return ImLViewHolder(view)
        }
    }
}