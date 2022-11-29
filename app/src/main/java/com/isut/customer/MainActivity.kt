package com.isut.customer

import android.Manifest
import android.app.LoaderManager
import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.Loader
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.isut.customer.FixValue.CheckBooking
import com.isut.customer.R.id
import com.isut.customer.apiclient.APIClient
import com.isut.customer.apiclient.APIInterface
import com.isut.customer.model.Booking.BookingModel
import com.isut.customer.model.DroverModel
import com.isut.customer.model.UpdateResponse
import com.isut.customer.model.bookingNoti.NotiBookingResponse
import com.isut.customer.model.cabd.DataItem
import com.isut.customer.model.cabd.TaxyModel
import com.isut.customer.model.vlaid.ValidResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnMapReadyCallback, LoaderManager.LoaderCallbacks<Any>, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, PlaceSelectionListener, LocationListener,
    RecyclerViewClickListener {
    var Header = "Bearer"
    var tv_distqnce: TextView? = null
    var tv_fair: TextView? = null
    var textView2: TextView? = null
    var change_location_text: TextView? = null
    var textViewemil: TextView? = null
    var radioSudan: RadioButton? = null
    var radioSUv: RadioButton? = null
    var jsonObjectForTip = JSONObject()
    var jsonObjectForRating = JSONObject()


    //  Marker srchMarker = new Marker();
    private var mOrigin: LatLng? = null
    private var mDestination: LatLng? = null
    private var mPolyline: Polyline? = null
    var rec_driver: RecyclerView? = null
    var lintype: LinearLayout? = null
    var fair = 0
    var firebaseDatabase: FirebaseDatabase? = null
    var progress: ProgressDialog? = null
    var droverModelList: MutableList<DroverModel?> = ArrayList()
    var driverlist: List<DataItem> = ArrayList()
    var driverListAdapter: DriverListAdapter? = null

    // creating a variable for our
    // Database Reference for Firebase.
    var databaseReference: DatabaseReference? = null
    var lng = 0.0
    var lat = 0.0
    var products_select_option: RelativeLayout? = null
    var select_btn: ImageButton? = null
    var product1: ImageButton? = null
    var img_share: ImageButton? = null
    var myCurrentloc: Button? = null
    var book_button: Button? = null
    var dropoffLocation: Button? = null
    var myCLocation2: Button? = null
    var relchange: RelativeLayout? = null
    var strEditText: String? = null
    var origiadrss: String? = null
    var originLatLong: LatLng? = null
    var destinqtoionLatLong: LatLng? = null
    var destinAdrss: String? = null
    var driverId: String? = null
    var bookingId: String? = "0"
    var amount: String? = null
    var gtotal: String? = null
    var discount: String? = null
    var fieldSelector: PlacesFieldSelector? = null
    private var mapFragment: MapFragment? = null
    private var mMap: GoogleMap? = null
    private var googleApiClient: GoogleApiClient? = null
    private var mLastLocation: Location? = null
    private var request: LocationRequest? = null
    var mapView: View? = null
    private var mRequestingLocationUpdates = false
    var cLocation: CameraUpdate? = null
    var latitude = 0.0
    var longitude = 0.0
    var now: Marker? = null
    var srchMarker: Marker? = null
    var destin: Marker? = null
    var geocoder: Geocoder? = null
    var addresses: List<Address>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        setupLocationManager()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        mapFragment = fragmentManager
            .findFragmentById(id.map) as MapFragment
        mapView = mapFragment!!.view
        mapFragment!!.getMapAsync(this)
        CheckMapPermission()
        rec_driver = findViewById(id.rec_driver)
        change_location_text = findViewById(id.change_location_text)
        radioSUv = findViewById(id.radioSUv)
        img_share = findViewById(id.img_share)
        relchange = findViewById(id.relchange)
        radioSudan = findViewById(id.radioSudan)
        lintype = findViewById(id.lintype)
        tv_distqnce = findViewById(id.tv_distqnce)
        tv_fair = findViewById(id.tv_fair)
        Places.initialize(applicationContext, getString(R.string.google_maps_key))
        val placesClient = Places.createClient(this)
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(id.place_autocomplete_fragment) as AutocompleteSupportFragment?



        val drawer = findViewById<View>(id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.setDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<View>(id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val header = navigationView.getHeaderView(0)
        textViewemil = header.findViewById(id.textViewemil)
        textView2 = header.findViewById(id.textView2)
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        val id = sharedpreferences.getInt(FixValue.User_ID, 0)
        val name = sharedpreferences.getString(FixValue.FIRSTNAME, "")
        val mobile = sharedpreferences.getString(FixValue.MOBILE, "")
        textViewemil!!.setText(name)
        textView2!!.setText(mobile)
        fieldSelector = PlacesFieldSelector()
        //Buttons Select Product option
        select_btn = findViewById<View>(R.id.img_selected) as ImageButton
      //  product1 = findViewById<View>(R.id.product_type_1_button) as ImageButton
      //  product2 = findViewById<View>(R.id.product_type_2_button) as ImageButton
      //  products_select_option = findViewById<View>(R.id.products_select_option) as RelativeLayout
        myCurrentloc = findViewById<View>(R.id.myCLocation) as Button
        myCLocation2 = findViewById<View>(R.id.myCLocation2) as Button

        val intent = intent
        if (intent != null) {
            if (intent.hasExtra("key")) {
                if (intent.getStringExtra("key").equals("fromonti", ignoreCase = true)) {
                    bookingId= intent.getStringExtra("bookingId")
                  checkStatusSubmit()

                }
            }
        }
        radioSudan!!.setOnClickListener {
            if (radioSudan!!.isChecked){
                radioSudan?.isChecked = true
                radioSUv?.isChecked = false
                getCabBytype("0")
            }
        }
        radioSUv!!.setOnClickListener {
            if (radioSUv!!.isChecked){
                radioSudan?.isChecked = false
                radioSUv?.isChecked = true
                getCabBytype("1")
            }
        }
       /* radioSudan?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                radioSudan?.isChecked = true
                radioSUv?.isChecked = false
                getCabBytype("0")
            }
        }
        radioSUv?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                radioSudan?.isChecked = false
                radioSUv?.isChecked = true
                getCabBytype("1")
            }
        }*/
        img_share?.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)

          intent.type = "text/plain"
          intent.putExtra(
            Intent.EXTRA_SUBJECT,
            "hello Dear..  I am going from $origiadrss  to $destinAdrss "
        )
            val ori =  getLocationAddress(myCurrentloc?.text.toString())
            val des =  getLocationAddress(myCLocation2?.text.toString())
           val str = "https://www.google.com/maps/dir/?api=1&origin=${ori?.latitude},${ori?.longitude}&destination=${des?.latitude},${des?.longitude}"
            intent.putExtra(Intent.EXTRA_TEXT, "hello Dear..  I am going from ${myCurrentloc?.text.toString()}  to ${myCLocation2?.text.toString()} ${str}")
            /*Fire!*/
            /*Fire!*/startActivity(Intent.createChooser(intent, "Share Location"))
        }
    }

    override fun onStart() {
        super.onStart()
        googleApiClient!!.connect()
        //mGoogleApiClient.connect();
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        progress = ProgressDialog(this)
        progress!!.setTitle("Loading")
        progress!!.setMessage("Wait while loading...")
        progress!!.setCancelable(false) // disable dismiss by tapping outside of the dialog-
        progress!!.show()
        submit()
        //        getdata();
        if (googleApiClient!!.isConnected) {
            setInitialLocation()
        }
        val service = getSystemService(LOCATION_SERVICE) as LocationManager
        val enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER)

        // Check if enabled and if not send user to the GPS settings
        if (!enabled) {
            buildAlertMessageNoGps()
        }
        if (CheckBooking.equals("1")) {
          relchange?.visibility = View.VISIBLE
          change_location_text?.visibility = View.VISIBLE
            CheckBooking = "0"
        }
        checkvlaid()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (googleApiClient!!.isConnected) {
            googleApiClient!!.disconnect()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.nav_profile) {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_rides) {
            val intent = Intent(this@MainActivity, RideListActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_contact_us) {
            val intent = Intent(this@MainActivity, ContactActivity::class.java)
            startActivity(intent)

        } else if (id == R.id.nav_manage) {
            //   FirebaseAuth.getInstance().signOut();
            logdialog()
            // finish();
        } else if (id == R.id.nav_rate_us) {
            val intent = Intent(this@MainActivity, FeedBackActivity::class.java)
            startActivity(intent)
        }
        else if (id == R.id.nav_slideshow) {
            val intent = Intent(this@MainActivity, AboutActivity::class.java)
            startActivity(intent)
        }
        else if (id == R.id.nav_refer) {
            val intent = Intent(this@MainActivity, ReferFriendActivity::class.java)
            startActivity(intent)
        }
        else if (id == R.id.nav_send) {
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        /*    try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this ) );

            if (!success) {
                Log.e( TAG, "Style parsing failed." );
            }
        } catch (Resources.NotFoundException e) {
            Log.e( TAG, "Can't find style. Error: ", e );
        }*/if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then over   riding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        //This line will show your current location on Map with GPS dot
        mMap!!.isMyLocationEnabled = true
        locationButton()
        /*
        Toast.makeText( MainActivity.this, "OnStart:"+latitude+","+longitude, Toast.LENGTH_SHORT ).show();
*/
    }

    private fun setupLocationManager() {
        //buildGoogleApiClient();
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
                .addApi(com.google.android.gms.location.places.Places.PLACE_DETECTION_API)
                .build()
            //mGoogleApiClient = new GoogleApiClient.Builder(this);
        }
        googleApiClient!!.connect()
        createLocationRequest()
    }

    protected fun createLocationRequest() {
        request = LocationRequest()
        request!!.smallestDisplacement = 10f
        request!!.fastestInterval = 50000
        request!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        request!!.numUpdates = 3
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(request)
        builder.setAlwaysShow(true)
        val result = LocationServices.SettingsApi.checkLocationSettings(
            googleApiClient,
            builder.build()
        )
        result.setResultCallback { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> setInitialLocation()
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                         // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(
                            this@MainActivity,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (e: SendIntentException) {
                        // Ignore the error.
                    }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("onActivityResult()", Integer.toString(resultCode))
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                RESULT_OK -> {
                    setInitialLocation()
                    Toast.makeText(this@MainActivity, "Location enabled", Toast.LENGTH_LONG).show()
                }
                RESULT_CANCELED -> {
                    // The user was asked to change settings, but chose not to
                    Toast.makeText(this@MainActivity, "Location not enabled", Toast.LENGTH_LONG)
                        .show()
                    mRequestingLocationUpdates = false
                }
                else -> {}
            }
        }
        if (requestCode == 1) {
            if (resultCode == -1) {
                //  strEditText = data.getStringExtra("place");
                val place = Autocomplete.getPlaceFromIntent(
                    data!!
                )
                originLatLong = place.latLng

                origiadrss = String.format(place.name!!)
               // Toast.makeText(this, origiadrss, Toast.LENGTH_LONG).show()
              //  Toast.makeText(this, "dfgn$strEditText", Toast.LENGTH_SHORT).show()
                if (origiadrss != null) {
                    myCurrentloc!!.text = origiadrss
                }
                //   onResume();
                ///  dropoffLocation.setText(strEditText);
                getLocationFromAddress(origiadrss, 1,bookingId!!)
            }
        }
        if (requestCode == 2) {
            if (resultCode == -1) {
                //  strEditText = data.getStringExtra("place");
                val place = Autocomplete.getPlaceFromIntent(
                    data!!
                )

                // Place place = PlacePicker.getPlace(data, this);
                val toastMsg = String.format(place.name!!)
                destinAdrss = String.format(place.name!!)
                destinqtoionLatLong = place.latLng

               // Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()
                if (toastMsg != null) {
                    myCLocation2!!.text = toastMsg
                    rec_driver!!.visibility = View.VISIBLE
                    lintype!!.visibility = View.VISIBLE
                }
                // onResume();
                ///  dropoffLocation.setText(strEditText);
                getLocationFromAddress(toastMsg, 2,bookingId!!)
            }
        }

        if (requestCode == 4) {
            if (resultCode == -1) {
                //  strEditText = data.getStringExtra("place");
                val place = Autocomplete.getPlaceFromIntent(
                    data!!
                )

                // Place place = PlacePicker.getPlace(data, this);
                val toastMsg = String.format(place.name!!)
                if (toastMsg != null) {
                    myCLocation2!!.text = toastMsg
                    rec_driver!!.visibility = View.VISIBLE
                    lintype!!.visibility = View.VISIBLE
                }
                // onResume();
                ///  dropoffLocation.setText(strEditText);
                getLocationFromAddress(toastMsg, 4,bookingId!!)
            }
        }
        if (requestCode == 3) {

               bookingId = data?.getStringExtra("bookingId")

                        val result = data?.getStringExtra("result")
                        change_location_text?.visibility = View.VISIBLE
                        relchange?.visibility = View.VISIBLE



            }


    }

    fun getLocationFromAddress(strAddress: String?, type: Int,bookingId: String) {
        //Create coder with Activity context - this
        val coder = Geocoder(this)
        val address: List<Address>?
        try {
            //Get latLng from String
            address = coder.getFromLocationName(strAddress, 5)

            //check for null
            if (address == null) {
                return
            }
            if (type == 2) {
                //Lets take first possibility from the all possibilities.
                val location = address[0]
                val latLng = LatLng(location.latitude, location.longitude)

                //Put marker on map on that LatLng
                // Log.d("sdfg","sdfg"+latLng.toString());
                if (destin != null) {
                    destin!!.remove()
                }
                destin = mMap!!.addMarker(
                    MarkerOptions().position(latLng).title("Your Destination")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.trank))
                )
                mDestination = LatLng(latLng.latitude, latLng.longitude)

                //Animate and Zoon on that map location
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
                /*    Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(lat, lng), latLng)
                        .width(5)
                        .color(Color.RED));*/
            }
            if (type == 4) {
                //Lets take first possibility from the all possibilities.
                val location = address[0]
                val latLng = LatLng(location.latitude, location.longitude)

                //Put marker on map on that LatLng
                // Log.d("sdfg","sdfg"+latLng.toString());
                if (destin != null) {
                    destin!!.remove()
                }
                destin = mMap!!.addMarker(
                    MarkerOptions().position(latLng).title("Your Destination")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.trank))
                )
                mDestination = LatLng(latLng.latitude, latLng.longitude)

                //Animate and Zoon on that map location
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
               // changeAddress(myCLocation2?.text.toString(),bookingId!!)
                /*    Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(lat, lng), latLng)
                        .width(5)
                        .color(Color.RED));*/
            }
            if (type == 1) {
                val location = address[0]
                val latLng = LatLng(location.latitude, location.longitude)

                //Put marker on map on that LatLng
                // Log.d("sdfg","sdfg"+latLng.toString());
                if (srchMarker != null) {
                    srchMarker!!.remove()
                }
                srchMarker = mMap!!.addMarker(
                    MarkerOptions().position(latLng).title("Your Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.trank))
                )
                mOrigin = LatLng(latLng.latitude, latLng.longitude)

                //Animate and Zoon on that map location
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))

                /* Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(lat, lng), latLng)
                        .width(5)
                        .color(Color.RED));*/
            }
            /*  if(mOrigin != null && mDestination != null)
            {
                drawRoute();
            }*/
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    fun getLocationAddress(strAddress: String?): LatLng? {
        //Create coder with Activity context - this
        val coder = Geocoder(this)
        val address: List<Address>?
        try {
            //Get latLng from String
            address = coder.getFromLocationName(strAddress, 5)

            //check for null


                val location = address[0]
                val latLng = LatLng(location.latitude, location.longitude)


              return  LatLng(latLng.latitude, latLng.longitude)


        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    private fun setInitialLocation() {
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            googleApiClient,
            request
        ) { location ->
            mLastLocation = location
            lat = location.latitude
            lng = location.longitude
            try {
                if (now != null) {
                    now!!.remove()
                }
                if (mOrigin == null) {
                    mOrigin = LatLng(lat, lng)
                    val positionUpdate = LatLng(latitude, longitude)
                    val update = CameraUpdateFactory.newLatLngZoom(positionUpdate, 15f)
                    now = mMap!!.addMarker(
                        MarkerOptions().position(positionUpdate)
                            .title("Current Position")
                    )
                    latitude = lat
                    longitude = lng
                    mMap!!.animateCamera(update)
                } else {
                    latitude = lat
                    longitude = lng
                    val positionUpdate = LatLng(mOrigin!!.latitude, mOrigin!!.longitude)
                    val update = CameraUpdateFactory.newLatLngZoom(positionUpdate, 15f)
                    now = mMap!!.addMarker(
                        MarkerOptions().position(positionUpdate)
                            .title("Your Position")
                    )
                    mMap!!.animateCamera(update)
                }
                if (mOrigin != null && mDestination != null) {
                    CalculationByDistance(mOrigin!!, mDestination!!)
                }

                /* if(mOrigin == null) {
                         mOrigin = new LatLng(lat, lng);
    
                         MainActivity.this.latitude = lat;
                         MainActivity.this.longitude = lng;
                     }*/


                /*  LatLng positionUpdate = new LatLng(MainActivity.this.latitude, MainActivity.this.longitude);
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(positionUpdate, 15);
                        now = mMap.addMarker(new MarkerOptions().position(positionUpdate)
                                .title("Current Position"));
    
                        mMap.animateCamera( update );*/if (mOrigin != null && mDestination != null) {
                    drawRoute()
                }
                //myCurrentloc.setText( ""+latitude );
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("MapException", ex.message!!)
            }

            //Geocode current location details
            try {
                geocoder = Geocoder(this@MainActivity, Locale.ENGLISH)
                addresses = geocoder!!.getFromLocation(latitude, longitude, 1)
                val str = StringBuilder()
                if (Geocoder.isPresent()) {
                    /*Toast.makeText(getApplicationContext(),
                                    "geocoder present", Toast.LENGTH_SHORT).show();*/
                    val returnAddress = addresses!!.get(0)
                    val localityString = returnAddress.getAddressLine(0)
                    //String city = returnAddress.getAddressLine(1);
                    //String region_code = returnAddress.getAddressLine(2);
                    //String zipcode = returnAddress.getAddressLine(3);
                    str.append(localityString).append("")
                    // str.append( city ).append( "" ).append( region_code ).append( "" );
                    // str.append( zipcode ).append( "" );
                    if (origiadrss == null) {
                        myCurrentloc!!.text = str

                    }
                } else {
                    /*    Toast.makeText(getApplicationContext(),
                                    "geocoder not present", Toast.LENGTH_SHORT).show();*/
                }

// } else {
// Toast.makeText(getApplicationContext(),
// "address not available", Toast.LENGTH_SHORT).show();
// }
            } catch (e: IOException) {
// TODO Auto-generated catch block
                Log.e("tag", e.message!!)
            }
        }
    }

    fun CalculationByDistance(StartP: LatLng, EndP: LatLng): Double {
        val Radius = 6371 // radius of earth in Km
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + (Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2)))
        val c = 2 * Math.asin(Math.sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec = Integer.valueOf(newFormat.format(meter))
        Log.i(
            "Radius Value", "" + valueResult + "   KM  " + kmInDec
                    + " Meter   " + meterInDec
        )
        val miles = (0.6213711922 * km).toFloat()
        val milesInDec = Integer.valueOf(newFormat.format(miles))

        tv_distqnce!!.visibility = View.VISIBLE
        tv_fair!!.visibility = View.VISIBLE
        fair = milesInDec * 8
        tv_fair!!.text = "$ $fair"
        tv_distqnce!!.text = "Distance $milesInDec Miles"
        return Radius * c
    }

    private fun updateCamera() {}
    private fun CheckMapPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1002
                )
            } else {
                setupLocationManager()
            }
        } else {
            setupLocationManager()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1002 -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        setupLocationManager()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                    //finish();
                }
            }
        }
    }

    fun getLatLang(placeId: String?) {
        com.google.android.gms.location.places.Places.GeoDataApi.getPlaceById(
            googleApiClient!!,
            placeId
        )
            .setResultCallback { places ->
                if (places.status.isSuccess && places.count > 0) {
                    val place = places[0]
                    val latLng = place.latLng
                    try {
                        val update = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                        mMap!!.animateCamera(update)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        Log.e("MapException", ex.message!!)
                    }
                    Log.i("place", "Place found: " + place.name)
                } else {
                    Log.e("place", "Place not found")
                }
                places.release()
            }
    }

    override fun onConnected(bundle: Bundle?) {
        //AlertMessageNoGps();
    }

    override fun onConnectionSuspended(i: Int) {
        //checkLocaionStatus();
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    override fun onLocationChanged(location: Location) {}
    override fun onPointerCaptureChanged(hasCapture: Boolean) {}
    override fun onCreateLoader(i: Int, bundle: Bundle): Loader<Any>? {
        return null
    }

    override fun onLoadFinished(loader: Loader<Any>, o: Any) {}
    override fun onLoaderReset(loader: Loader<Any>) {}

    //GET CURRENT LOCATION BUTTON POSITION....
    private fun locationButton() {
        val mapFragment = fragmentManager
            .findFragmentById(id.map) as MapFragment
        val locationButton =
            (mapFragment.view!!.findViewById<View>("1".toInt()).parent as View).findViewById<View>("2".toInt())
        if (locationButton != null && locationButton.layoutParams is RelativeLayout.LayoutParams) {
            // location button is inside of RelativeLayout
            val params = locationButton.layoutParams as RelativeLayout.LayoutParams

            // Align it to - parent BOTTOM|LEFT
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)

            // Update margins, set to 10dp
            val margin = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 150f,
                resources.displayMetrics
            ).toInt()
            params.setMargins(margin, margin, margin, margin)
            locationButton.layoutParams = params
        }
    }

    //Button Location Search
    fun myLocation(view: View?) {
        val fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME, Place.Field.LAT_LNG
        )
        val autocompleteIntent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        )
            .build(this@MainActivity)
        //requestCode in INT
        startActivityForResult(autocompleteIntent, 1)
    }

    fun destination(view: View?) {
        /*

        Intent intent = new Intent( MainActivity.this, LocationAutoActivity.class );
        intent.putExtra("key", "mydesti");

        startActivityForResult(intent, 2);
*/
        val fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME, Place.Field.LAT_LNG
        )
        val autocompleteIntent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        )
            .build(this@MainActivity)
        //requestCode in INT
        startActivityForResult(autocompleteIntent, 2)
    }
    fun changeDestination(view: View?) {
        /*

        Intent intent = new Intent( MainActivity.this, LocationAutoActivity.class );
        intent.putExtra("key", "mydesti");

        startActivityForResult(intent, 2);
*/
        val fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME, Place.Field.LAT_LNG
        )
        val autocompleteIntent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        )
            .build(this@MainActivity)
        //requestCode in INT
        startActivityForResult(autocompleteIntent, 4)
    }

    //Select product option button click
    fun img_selected(view: View?) {
        products_select_option!!.visibility = View.VISIBLE
    }

    //Select product option button click
    fun product_type_1_button(view: View?) {
        products_select_option!!.visibility = View.GONE
    }

    //Select product option button click
    fun product_type_2_button(view: View?) {
        products_select_option!!.visibility = View.GONE
    }

    //Enable Location Button
    private fun checkLocaionStatus() {}
    protected fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Please Turn ON your GPS Connection")
            .setTitle("GPS Not Enabled")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    private fun drawRoute() {

        // Getting URL to the Google Directions API
        val url = getDirectionsUrl(mOrigin, mDestination)
        val downloadTask: DownloadTask = DownloadTask()

        // Start downloading json data from Google Directions API
        downloadTask.execute(url)
    }

    private fun getDirectionsUrl(origin: LatLng?, dest: LatLng?): String {
        // Origin of route
        val str_origin = "origin=" + origin!!.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest!!.latitude + "," + dest.longitude


        // Sensor enabled

        // Building the parameters to the web service
        val sensor = "sensor=true"
        val mode = "mode=driving"
        val key = "key=" + resources.getString(R.string.google_maps_key)
        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor&$mode&$key"

        // Output format
        val output = "json"

        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }

    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)

            // Creating an http connection to communicate with url
            urlConnection = url.openConnection() as HttpURLConnection

            // Connecting to url
            urlConnection!!.connect()

            // Reading data from url
            iStream = urlConnection.inputStream
            val br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line: String? = ""
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            data = sb.toString()
            br.close()
        } catch (e: Exception) {
            Log.d("Exception on download", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    override fun onPlaceSelected(place: Place) {
        Log.d("dfghj", "aeretyguhjdf")
    }

    override fun onError(status: Status) {}
    private inner class DownloadTask : AsyncTask<String?, Void?, String>() {
        // Downloading data in non-ui thread

        // Executes in UI thread, after the execution of
        // doInBackground()
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            val parserTask = ParserTask()

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result)
        }

        override fun doInBackground(vararg url: String?): String {
            // For storing data from web service
            var data = ""
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]!!)
                Log.d("DownloadTask", "DownloadTask : $data")
            } catch (e: Exception) {
                Log.d("Background Task", e.toString())
            }
            return data
        }
    }

    private inner class ParserTask :
        AsyncTask<String?, Int?, List<List<HashMap<String, String>>>?>() {
        // Parsing the data in non-ui thread

        // Executes in UI thread, after the parsing process
        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
            var points: ArrayList<LatLng?>? = null
            var lineOptions: PolylineOptions? = null

            // Traversing through all the routes
            for (i in result!!.indices) {
                points = ArrayList()
                lineOptions = PolylineOptions()

                // Fetching i-th route
                val path = result[i]

                // Fetching all the points in i-th route
                for (j in path.indices) {
                    val point = path[j]
                    val lat = point["lat"]!!.toDouble()
                    val lng = point["lng"]!!.toDouble()
                    val position = LatLng(lat, lng)
                    points.add(position)
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points)
                lineOptions.width(8f)
                lineOptions.color(Color.RED)
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                if (mPolyline != null) {
                    mPolyline!!.remove()
                }
                mPolyline = mMap!!.addPolyline(lineOptions)
            } else Toast.makeText(applicationContext, "No route is found", Toast.LENGTH_LONG).show()
        }

        override fun doInBackground(vararg jsonData: String?): List<List<HashMap<String, String>>>? {
            val jObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? = null
            try {
                jObject = JSONObject(jsonData[0])
                val parser = DirectionsJSONParser()

                // Starts parsing data
                routes = parser.parse(jObject)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return routes
        }
    }

    override fun itemclick(type: String, position: Int, child_position: Int) {
        val i = Intent(this, BookActivity::class.java)
        val id = driverlist[position]!!.user.id
        i.putExtra("startpoint", myCurrentloc!!.text.toString().trim { it <= ' ' })
        i.putExtra("endpoint", myCLocation2!!.text.toString().trim { it <= ' ' })
        i.putExtra("drivername", driverlist[position]!!.user.fullName)
        i.putExtra("fair", fair)
        i.putExtra("linsece", driverlist[position].licenseNumber)

        if (driverlist[position]!!.cabs.size != 0) {
            i.putExtra("cabnumum", driverlist[position]!!.cabs[0].carNumber)
        }
        i.putExtra("dids", id)
        startActivityForResult(i,3)
    }

    override fun itemlikeclick(type: String, position: Int, child_position: Int) {}
    private fun getdata() {
        firebaseDatabase = FirebaseDatabase.getInstance()

        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase!!.getReference("Driver")
        droverModelList.clear()
        // calling add value event listener method
        // for getting the values from database.
        databaseReference!!.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (dsp in dataSnapshot.children) {
                        droverModelList.add(dsp.getValue(DroverModel::class.java))
                        val droverModel = dataSnapshot.getValue(
                            DroverModel::class.java
                        )
                        // after getting the value we are setting
                        // our value to our text view in below line.
                        //   droverModelList.add(droverModel);
                        //driverListAdapter = new DriverListAdapter(MainActivity.this, droverModelList);
                        rec_driver!!.adapter = driverListAdapter
                        rec_driver!!.setHasFixedSize(true)
                        val linearLayoutManager1 = LinearLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        rec_driver!!.layoutManager = linearLayoutManager1
                        click()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //handle databaseError
                }
            })
    }

    fun click() {
        driverListAdapter!!.setClickListener(this)
    }

    fun submit() {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        Header = "Bearer " + sharedpreferences.getString(FixValue.TOKEN, "")

        //    RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        // SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //  final SharedPreferences.Editor editor = sharedPreferences.edit();
        val apiService = APIClient.client?.create(APIInterface::class.java)
      val url = FixValue.baseurl.plus("customers/3")
        val call = apiService?.getdriver(Header, url)
        call?.enqueue(object : Callback<TaxyModel> {
            override fun onResponse(call: Call<TaxyModel>, response: Response<TaxyModel>) {

                if(response.body() != null) {
                    if (response.body()!!.status.equals("ok", ignoreCase = true)) {

                        /* Intent i = new Intent(SignupActivity.this,HomeActivity.class);
                    startActivity(i);*/
                        //getotp();
                        //userdatwe(response.body().getUserId(),phone);
                        // int ids = response.body().get();
                        driverlist = response.body()!!.data
                        /* if (driverlist.get(0).getCabs().size() != 0)
                    {
                        getimgpath(driverlist.get(0).getCabs().get(0).getCarImages());
                    }
                   else
                   {*/
                        val img = " "
                        driverListAdapter = DriverListAdapter(this@MainActivity, driverlist!!, img)
                        rec_driver!!.adapter = driverListAdapter
                        rec_driver!!.setHasFixedSize(true)

                        //  }
                        val linearLayoutManager1 = LinearLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        rec_driver!!.layoutManager = linearLayoutManager1
                        click()
                        Toast.makeText(
                            this@MainActivity,
                            "" + response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        // progress.dismiss();
                        progress!!.dismiss()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "" + response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        progress!!.dismiss()
                    }
                }else{
                    val preferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    againSetData()
                    val intent = Intent(this@MainActivity, LogInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()

                }
            }

            override fun onFailure(call: Call<TaxyModel>, t: Throwable) {
                Toast.makeText(this@MainActivity, "" + t.toString(), Toast.LENGTH_SHORT).show()
                progress!!.dismiss()
            }
        })
    }

    fun logdialog() {
        val alert = AlertDialog.Builder(this@MainActivity)
        alert.setMessage("Are you sure you want to LogOut?")
        alert.setPositiveButton("YES") { dialog, which ->
            val preferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
            val editor = preferences.edit()
            editor.clear()
            editor.apply()
            againSetData()
            val intent = Intent(this@MainActivity, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
        alert.setNegativeButton("NO") { dialog, which -> // close dialog
            dialog.cancel()
        }
        alert.show()
    }

    private fun againSetData() {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        edit.putString(FixValue.IN_lOGIN, "")
        //edit.putString(IsAppOpenFirst,OUT_LOGIN);
        edit.commit()
    }


    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val PERMISSION_MY_LOCATION = 3
        private const val REQUEST_CHECK_SETTINGS = 1000
    }

    fun  RatingDialog()
    {
        val view: View = layoutInflater.inflate(R.layout.dialog_rating, null)
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Tip and Rating")
        alertDialog.setCancelable(false)
        val rating = view.findViewById(R.id.rating) as RatingBar
        val etcode = view.findViewById(R.id.etcode) as EditText
        val et_tip = view.findViewById(R.id.et_tip) as EditText
        val tvtotalfair = view.findViewById(R.id.tvtotalfair) as TextView
        val tvdiscount = view.findViewById(R.id.tvtotaldiscount) as TextView
        val tvgtotal = view.findViewById(R.id.tvgrand) as TextView
        val btncancel = view.findViewById(R.id.btncancel) as AppCompatButton
        val btnsave = view.findViewById(R.id.btnsave) as AppCompatButton
        val btntip = view.findViewById(R.id.btntip) as AppCompatButton

       val gtot = amount!!.toInt()+discount!!.toInt()
        tvtotalfair.setText("$"+amount)
        tvdiscount.setText("$"+discount)
        tvgtotal.setText("$"+gtot)
        btnsave.setOnClickListener {
            if (rating.rating == 0f)
            {
              Toast.makeText(this,"Please give any rating",Toast.LENGTH_LONG).show()
          return@setOnClickListener
            }


            progress = ProgressDialog(this)
            progress!!.setTitle("Loading")
            progress!!.setMessage("Wait while loading...")
            progress!!.setCancelable(false) // disable dismiss by tapping outside of the dialog
            progress!!.show()
            submitRating(rating.rating,etcode.text.toString(), bookingId = bookingId!!,alertDialog)
        }
        btntip.setOnClickListener {
            if (et_tip.text.toString().isEmpty())
            {
                Toast.makeText(this,"Please give any rating",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            progress = ProgressDialog(this)
            progress!!.setTitle("Loading")
            progress!!.setMessage("Wait while loading...")
            progress!!.setCancelable(false) // disable dismiss by tapping outside of the dialog
            progress!!.show()

            submitTip(driverId!!,et_tip.text.toString(),bookingId!!,alertDialog)
        }
        btncancel.setOnClickListener {
            alertDialog.cancel();
        }



        alertDialog.setView(view);
        alertDialog.show();
    }
    fun submitRating(rating: Float, feedback: String,bookingId:String, alertDialog: AlertDialog) {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        val  id =sharedpreferences.getInt(FixValue.User_ID, 0)

        try {
            jsonObjectForRating.put("rating", rating)
            jsonObjectForRating.put("driverId", driverId)
            jsonObjectForRating.put("userId", id.toString())
            jsonObjectForRating.put("bookingId", bookingId)

            //  jsonObject.put("appId", token)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val  Header = "Bearer " + sharedpreferences.getString(FixValue.TOKEN, "")

        val bodyRequest =
            RequestBody.create(MediaType.parse("application/json"), jsonObjectForRating.toString())
        // SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //  final SharedPreferences.Editor editor = sharedPreferences.edit();
        val apiService = APIClient.client?.create(APIInterface::class.java)
        val call = apiService?.ratingAdd(Header,bodyRequest)

        call?.enqueue(object : Callback<UpdateResponse> {
            override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                    Toast.makeText(this@MainActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                    progress!!.dismiss()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {

            }
        })
    }

    fun submitTip(driverid: String, tip: String, bookingId: String, alertDialog: AlertDialog) {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        val  id =sharedpreferences.getInt(FixValue.User_ID, 0)

        try {
            jsonObjectForTip.put("driverId", driverid)
            jsonObjectForTip.put("userId", id.toString())
            jsonObjectForTip.put("tip", tip)
            jsonObjectForTip.put("bookingId", bookingId)

            //  jsonObject.put("appId", token)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val  Header = "Bearer " + sharedpreferences.getString(FixValue.TOKEN, "")

        val bodyRequest =
            RequestBody.create(MediaType.parse("application/json"), jsonObjectForTip.toString())
        // SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //  final SharedPreferences.Editor editor = sharedPreferences.edit();
        val apiService = APIClient.client?.create(APIInterface::class.java)
        val call = apiService?.tipAdd(Header,bodyRequest)

        call?.enqueue(object : Callback<UpdateResponse> {
            override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                    Toast.makeText(this@MainActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    progress!!.dismiss()
                } else {

                    progress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {

            }
        })
    }

    fun changeAddress(DesAddress: String,bookingid :String) {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        val  id =sharedpreferences.getInt(FixValue.User_ID, 0)

        try {
            jsonObjectForTip.put("location", DesAddress)
            jsonObjectForTip.put("bookingId", bookingid)


            //  jsonObject.put("appId", token)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val  Header = "Bearer " + sharedpreferences.getString(FixValue.TOKEN, "")

        val bodyRequest =
            RequestBody.create(MediaType.parse("application/json"), jsonObjectForTip.toString())
        // SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //  final SharedPreferences.Editor editor = sharedPreferences.edit();
        val apiService = APIClient.client?.create(APIInterface::class.java)
        val call = apiService?.updatebooking(Header,bodyRequest)

        call?.enqueue(object : Callback<BookingModel> {
            override fun onResponse(call: Call<BookingModel>, response: Response<BookingModel>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                    Toast.makeText(this@MainActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    progress!!.dismiss()
                } else {

                    progress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<BookingModel>, t: Throwable) {

            }
        })
    }
    fun getCabBytype(cabType: String) {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        Header = "Bearer " + sharedpreferences.getString(FixValue.TOKEN, "")


        val apiService = APIClient.client?.create(APIInterface::class.java)
        val url = FixValue.baseurl.plus("cab/$cabType")
        val call = apiService?.getCabByType(Header, url)
        call?.enqueue(object : Callback<TaxyModel> {
            override fun onResponse(call: Call<TaxyModel>, response: Response<TaxyModel>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {

                    /* Intent i = new Intent(SignupActivity.this,HomeActivity.class);
                    startActivity(i);*/
                    //getotp();
                    //userdatwe(response.body().getUserId(),phone);
                    // int ids = response.body().get();
                    driverlist = response.body()!!.data
                    /* if (driverlist.get(0).getCabs().size() != 0)
                    {
                        getimgpath(driverlist.get(0).getCabs().get(0).getCarImages());
                    }
                   else
                   {*/
                    val img = " "
                    driverListAdapter = DriverListAdapter(this@MainActivity, driverlist!!, img)
                    rec_driver!!.adapter = driverListAdapter
                    rec_driver!!.setHasFixedSize(true)

                    //  }
                    val linearLayoutManager1 = LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    rec_driver!!.layoutManager = linearLayoutManager1
                    click()
                    Toast.makeText(
                        this@MainActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    // progress.dismiss();
                    rec_driver?.visibility = View.VISIBLE

                    progress!!.dismiss()
                } else {
                    rec_driver?.visibility = View.GONE
                    Toast.makeText(
                        this@MainActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<TaxyModel>, t: Throwable) {
                Toast.makeText(this@MainActivity, "" + t.toString(), Toast.LENGTH_SHORT).show()
                progress!!.dismiss()
            }
        })
    }
    fun checkvlaid() {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        Header = "Bearer " + sharedpreferences.getString(FixValue.TOKEN, "")
        val id = sharedpreferences.getInt(FixValue.User_ID, 0)
        val name = sharedpreferences.getString(FixValue.FIRSTNAME, "")
        val mobile = sharedpreferences.getString(FixValue.MOBILE, "")
        // SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //  final SharedPreferences.Editor editor = sharedPreferences.edit();
        val url = FixValue.baseurl + "customer/" + id + "/cabs"
        val apiService = APIClient.client?.create(APIInterface::class.java)
        val call = apiService?.validator(Header)
        call?.enqueue(object : Callback<ValidResponse> {
            override fun onResponse(
                call: Call<ValidResponse>,
                response: Response<ValidResponse>
            ) {
                if(response.body() != null) {
                    Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_SHORT)
                        .show()
                    if (response.body()!!.status.equals("EXPECTATION_FAILED", ignoreCase = true)) {
                        val intent1 = Intent(this@MainActivity, LogInActivity::class.java)
                        startActivity(intent1)
                        finish()
                    }

                }
                else{
                    val preferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    againSetData()
                    val intent = Intent(this@MainActivity, LogInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<ValidResponse>, t: Throwable) {
                progress!!.dismiss()
            }
        })
    }
    fun checkStatusSubmit() {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
        val edit = sharedpreferences.edit()
        Header = "Bearer " + sharedpreferences.getString(FixValue.TOKEN, "")
        val ids = java.lang.Long.valueOf(bookingId!!.toLong())
        // SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //  final SharedPreferences.Editor editor = sharedPreferences.edit();
        val url = FixValue.baseurl + "booking/" + ids
        val apiService = APIClient.client?.create(APIInterface::class.java)
        val call = apiService?.cabstatus(Header, url)
        call?.enqueue(object : Callback<NotiBookingResponse> {
            override fun onResponse(call: Call<NotiBookingResponse>, response: Response<NotiBookingResponse>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                    //  bottonln!!.visibility = View.GONE
                    if (response.body()!!.data.booking.status == 3) {
                        driverId= response.body()!!.data.booking.driverId.toString()
                        amount= response.body()!!.data.booking.fair.toString()
                       if (response.body()!!.data.discount == null) {
                           discount = "0"

                           RatingDialog()
                       }
                        else
                       {
                           discount = response.body()!!.data.discount.toString()

                           RatingDialog()
                       }
                    }

                    progress!!.dismiss()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<NotiBookingResponse>, t: Throwable) {}
        })
    }

}