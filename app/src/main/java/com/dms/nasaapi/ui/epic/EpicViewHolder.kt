package com.dms.nasaapi.ui.epic

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dms.nasaapi.R
import com.dms.nasaapi.model.epic.Epic

import kotlinx.android.synthetic.main.epic_list_item.view.*

class EpicViewHolder(private val view:View): RecyclerView.ViewHolder(view) {

    private var epic:Epic? = null
    fun bind(epic: Epic) {
        if (epic == null) {
            Log.d("EPIC","empty date")
        } else {
            showEpicData(epic)
        }
    }
    private fun showEpicData(epic: Epic){
        this.epic=epic
        val hdUrl = "https://epic.gsfc.nasa.gov/archive/natural/${epic.year}/${epic.month}/${epic.day}/png/${epic.image}.png"
        val halfResolutionUrl ="https://epic.gsfc.nasa.gov/archive/natural/${epic.year}/${epic.month}/${epic.day}/jpg/${epic.image}.jpg"
        val thumbsUrl ="https://epic.gsfc.nasa.gov/archive/natural/${epic.year}/${epic.month}/${epic.day}/thumbs/${epic.image}.jpg"

        Glide.with(view)
            .load(halfResolutionUrl)
            .into(view.epic_image)
    }
    companion object {
        fun create(parent: ViewGroup): EpicViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.epic_list_item, parent, false)
            return EpicViewHolder(view)
        }
    }
}