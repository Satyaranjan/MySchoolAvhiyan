package com.satyasoft.myschoolavhiyan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satyasoft.myschoolavhiyan.R


 class ImageAdapter(private val imageList: ArrayList<String>, context: Context) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    private val context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.gallery_card_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // loading the images from the position
        Glide.with(holder.itemView.context).load(imageList[position]).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.item)
        }
    }

    init {
        this.context = context
    }
}