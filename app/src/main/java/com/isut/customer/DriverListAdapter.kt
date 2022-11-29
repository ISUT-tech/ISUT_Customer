package com.isut.customer

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.isut.customer.model.cabd.DataItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverListAdapter(
    private val mContext: Context,
    var typeList: List<DataItem>, //private static RecyclerViewClickListener itemListener;
    var img: String
) : RecyclerView.Adapter<DriverListAdapter.CustomViewHolder>() {
    //int child_position=-1;
    var type = ""
    private var itemClickListener: RecyclerViewClickListener? = null

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
            LayoutInflater.from(viewGroup.context).inflate(R.layout.row_driver, null)
        return CustomViewHolder(view)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(customViewHolder: CustomViewHolder, i: Int) {
        customViewHolder.zname.text = typeList[i].cabs[0].carName
        if (typeList[i].cabs.size != 0) {
            if (typeList[i].cabs[0].carImages != null) {
                //  getimgpath(myObj.getData().get(i).getCarImages());
                Glide.with(mContext)
                    .load("http://143.198.75.109:9090/api/file/getFile/" + typeList[i].cabs[0].carImages)
                    .apply(
                        RequestOptions().override(100, 100)
                            .placeholder(R.drawable.isut)
                            .error(R.mipmap.ic_launcher_round).centerCrop()
                    )
                    .into(customViewHolder.img)
            }
        }

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
        var zname: TextView
        var txt_rate: TextView? = null
        var img: ImageView
        var fav_linear_content_view: LinearLayout? = null

        init {
            zname = view.findViewById(R.id.zname)
            img = view.findViewById(R.id.img)
        }
    }

    companion object {
        private val itemClickListener: RecyclerViewClickListener? = null
    }


}