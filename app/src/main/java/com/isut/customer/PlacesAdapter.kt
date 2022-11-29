package com.isut.customer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isut.customer.PlacesAdapter.MyViewHolder

/**
 * Created by Lenovo on 10/23/2017.
 */
class PlacesAdapter(private val myPlacesList: List<Places>) : RecyclerView.Adapter<MyViewHolder>() {
    private var itemClickListener: RecyclerViewClickListener? = null

    fun setClickListener(itemClickListener: RecyclerViewClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var primaryText: TextView
        var secText: TextView
        var distText: TextView

        init {
            primaryText = itemView.findViewById<View>(R.id.primaryText) as TextView
            secText = itemView.findViewById<View>(R.id.addressDescription) as TextView
            distText = itemView.findViewById<View>(R.id.distance) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.place_autocomplete_adapter, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val places = myPlacesList[position]
        holder.primaryText.text = places.primaryText
        holder.secText.text = places.addressDescription
        holder.distText.text = places.distance
        holder.itemView.setOnClickListener(View.OnClickListener {
            itemClickListener!!.itemclick(
                myPlacesList[position].primaryText, position, 0
            )
        })
    }

    override fun getItemCount(): Int {
        return myPlacesList.size
    }

    companion object {
        private val itemClickListener: RecyclerViewClickListener? = null
    }
}