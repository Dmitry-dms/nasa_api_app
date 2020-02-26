package com.dms.nasaapi.ui.mrp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dms.nasaapi.R
import com.dms.nasaapi.model.MarsPhoto
import kotlinx.android.synthetic.main.list_item.view.*

class MarsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val date: TextView = view.findViewById(R.id.earth_date)

    private var marsPhoto: MarsPhoto? = null

    init {
//
    }

    fun bind(marsPhoto: MarsPhoto) {
        if (marsPhoto == null) {
            val resources = itemView.resources
            date.text = resources.getString(R.string.loading)
        } else {
            showMarsData(marsPhoto)
        }
    }

    private fun showMarsData(marsPhoto: MarsPhoto) {
        this.marsPhoto = marsPhoto
        date.text = marsPhoto.earthDate

        Glide.with(view)
            .load(marsPhoto.image)
            .into(view.image_rv_mars)

        // if the description is missing, hide the TextView
        //// if the description is missing, hide the TextView
        //        var descriptionVisibility = View.GONE
        //        if (repo.description != null) {
        //            description.text = repo.description
        //            descriptionVisibility = View.VISIBLE
        //        }
        //        description.visibility = descriptionVisibility
        //
        //        stars.text = repo.stars.toString()
        //        forks.text = repo.forks.toString()
        //
        //        // if the language is missing, hide the label and the value
        //        var languageVisibility = View.GONE
        //        if (!repo.language.isNullOrEmpty()) {
        //            val resources = this.itemView.context.resources
        //            language.text = resources.getString(R.string.language, repo.language)
        //            languageVisibility = View.VISIBLE
        //        }
        //        language.visibility = languageVisibility
    }

    companion object {
        fun create(parent: ViewGroup): MarsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
            return MarsViewHolder(view)
        }
    }
}