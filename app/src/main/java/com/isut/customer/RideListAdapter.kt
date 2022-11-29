package com.isut.customer

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isut.customer.model.cabs.DataItem

class RideListAdapter     //private static RecyclerViewClickListener itemListener;
    (private val mContext: Context, var typeList: List<DataItem>) :

    RecyclerView.Adapter<RideListAdapter.CustomViewHolder>() {
    //int child_position=-1;
    private var itemClickListener: RecyclerViewClickListener? = null

    var type = ""
    fun setClickListener(itemClickListener: RecyclerViewClickListener) {
        this.itemClickListener = itemClickListener
    }

    /*public void setClickListener(RecyclerViewClickListener itemClickListener) {
        this.itemListener=itemClickListener;
    }*/
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): CustomViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.row_ride_item, null)
        return CustomViewHolder(view)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(customViewHolder: CustomViewHolder, i: Int) {
        customViewHolder.tv_reppee.text = "$" + typeList[i].fair.toString()
        customViewHolder.myCLocation2.text = typeList[i].destinationLocation
        customViewHolder.myCLocation.text = typeList[i].sourceLocation

        /*  customViewHolder.txt_name.setText(typeList.get(i).getTripName());
            customViewHolder.txt_rate.setText(typeList.get(i).getTripCost());

            Glide.with(mContext).load(typeList.get(i).getTripImg())
                    .apply(new RequestOptions().override(100, 100)
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round).centerCrop())
                    .into(customViewHolder.img_trip);*/customViewHolder.itemView.setOnClickListener {
            itemClickListener!!.itemclick(
                "",
                i,
                0
            )
        }
    }

    override fun getItemCount(): Int {
        return typeList.size
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        protected var img_trip: ImageView? = null

        //  CardView cardView;
        var myCLocation: Button
        var myCLocation2: Button
        var tv_reppee: TextView
        var zname: TextView? = null
        var txt_rate: TextView? = null
        var fav_linear_content_view: LinearLayout? = null

        init {
            myCLocation = view.findViewById(R.id.myCLocation)
            myCLocation2 = view.findViewById(R.id.myCLocation2)
            tv_reppee = view.findViewById(R.id.tv_reppee)
        }
    }

    companion object {
        private val itemClickListener: RecyclerViewClickListener? = null
    }
}