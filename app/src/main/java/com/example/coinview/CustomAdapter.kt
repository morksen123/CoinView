package com.example.coinview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val mList: List<ItemsViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]
        // sets the image to the imageview from our itemHolder class
//        holder.imageView.setImageURI(itemsViewModel.imageUrl)
//        holder.imageView.setImageBitmap(itemsViewModel.image)//setImageResource(itemsViewModel.image)
        holder.imageView.setImageResource(itemsViewModel.image)
        // sets the text to the textview from our itemHolder class
        holder.nameView.text = itemsViewModel.name
        holder.priceView.text = itemsViewModel.price

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val nameView: TextView = itemView.findViewById(R.id.name)
        val priceView: TextView = itemView.findViewById(R.id.price)
    }
}