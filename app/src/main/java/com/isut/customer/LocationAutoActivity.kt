package com.isut.customer

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import java.util.ArrayList

class LocationAutoActivity : AppCompatActivity(), PlaceSelectionListener,
    RecyclerViewClickListener {
    private val myPlacesList: MutableList<Places> = ArrayList()
    private var placesRecyclerView: RecyclerView? = null
    private var mPlacesAdapter: PlacesAdapter? = null
    var resultok = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_auto)
        val toolbar = findViewById<View>(R.id.toolbar_autoLocation) as Toolbar
        setSupportActionBar(toolbar)

        //Class Place Adapter
        mPlacesAdapter = PlacesAdapter(myPlacesList)

        //Recyclar view for Places
        placesRecyclerView = findViewById<View>(R.id.placesRecyclerView) as RecyclerView

        //RecyclerView Animation..
        placesRecyclerView!!.setHasFixedSize(true)
        val mlayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        placesRecyclerView!!.layoutManager = mlayoutManager
        placesRecyclerView!!.itemAnimator = DefaultItemAnimator()
        placesRecyclerView!!.adapter = mPlacesAdapter
        mPlacesAdapter!!.setClickListener(this)
        val i = intent
        resultok = if (i.getStringExtra("key").equals("myloc", ignoreCase = true)) {
            1
        } else {
            2
        }
        placesData()
    }

    private fun placesData() {
        var place = Places("ajmeri gate jaipur", "Cyberjaya", "10.2KM")
        myPlacesList.add(place)
        place = Places("ajmeri gate jaipur", "Cyberjaya,Selangor", "5.2KM")
        myPlacesList.add(place)
        place = Places("ajmeri gate jaipur", "Cyberjaya,Selangor", "8.2KM")
        myPlacesList.add(place)
        place = Places("ajmeri gate jaipur", "Cyberjaya", "4.2KM")
        myPlacesList.add(place)
        place = Places("ajmeri gate jaipur", "Cyberjaya", "13.2KM")
        myPlacesList.add(place)
        place = Places("ajmeri gate jaipur", "Cyberjaya", "1.2KM")
        myPlacesList.add(place)
        place = Places("ajmeri gate jaipur", "Cyberjaya,Selangor", "5.2KM")
        myPlacesList.add(place)
        place = Places("ajmeri gate jaipur", "Cyberjaya,Selangor", "8.2KM")
        myPlacesList.add(place)
        place = Places("ajmeri gate jaipur", "Cyberjaya", "4.2KM")
        myPlacesList.add(place)
        place = Places("ajmeri gate jaipur", "Cyberjaya", "13.2KM")
        myPlacesList.add(place)
        place = Places("Cyberia Smart Homes,Block B2", "Cyberjaya", "1.2KM")
        myPlacesList.add(place)
        place = Places("Cyberia Smart Homes,Block A1", "Cyberjaya,Selangor", "5.2KM")
        myPlacesList.add(place)
        place = Places("Cyberia Smart Homes,Block A2", "Cyberjaya,Selangor", "8.2KM")
        myPlacesList.add(place)
        place = Places("Cyberia Smart Homes,Block A3", "Cyberjaya", "4.2KM")
        myPlacesList.add(place)
        place = Places("Cyberia Smart Homes,Block B1", "Cyberjaya", "13.2KM")
        myPlacesList.add(place)
        place = Places("Cyberia Smart Homes,Block B2", "Cyberjaya", "1.2KM")
        myPlacesList.add(place)
    }

    override fun onPlaceSelected(place: Place) {
        Toast.makeText(this, "sdfgh", Toast.LENGTH_SHORT).show()
    }

    override fun onError(status: Status) {
        Toast.makeText(this, "sdfgh", Toast.LENGTH_SHORT).show()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Toast.makeText(this, "sdfgh", Toast.LENGTH_SHORT).show()
    }

    override fun itemclick(type: String, position: Int, child_position: Int) {
        if (resultok == 1) {
            val intent = Intent()
            intent.putExtra("place", type)
            setResult(1, intent)
        } else {
            val intent = Intent()
            intent.putExtra("place", type)
            setResult(2, intent)
        }
        finish()
        onBackPressed()
    }

    override fun itemlikeclick(type: String, position: Int, child_position: Int) {}
}