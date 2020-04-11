package com.dms.nasaapi.ui.mrp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dms.nasaapi.R
import com.dms.nasaapi.model.mrp.MarsPhoto
import kotlinx.android.synthetic.main.list_item.view.*

class MarsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private var marsPhoto: MarsPhoto? = null


    fun bind(marsPhoto: MarsPhoto) {
        showMarsData(marsPhoto)
    }

    private fun showMarsData(marsPhoto: MarsPhoto) {
        this.marsPhoto = marsPhoto
        Glide.with(view)
            .load(marsPhoto.image)
            .into(view.image_rv_mars)
    }

    companion object {
        fun create(parent: ViewGroup): MarsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
            return MarsViewHolder(view)
        }
    }
}