package com.assign3.addressbook.activities

import android.graphics.Bitmap
import android.media.Image
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.assign3.addressbook.HttpRequests.HttpRequestHandler
import com.assign3.addressbook.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions

class LocationDetailsActivity : AppCompatActivity() {

    var lat: Double = 0.0
    var long: Double = 0.0
    var name: String = ""
    var address: String = ""

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_details)

        lat = intent.getDoubleExtra("lat", 0.0)
        long = intent.getDoubleExtra("long", 0.0)
        name = intent.getStringExtra("Username")!!
        address = intent.getStringExtra("address")!!

        var latLng: String = "$lat+$long"

        val mapOwnerTextView = findViewById<TextView>(R.id.PersonNametv).apply {
            text = name
        }
        val addressTextView = findViewById<TextView>(R.id.addressstv).apply {
            text = address
        }
        val latTextView = findViewById<TextView>(R.id.latitudetv).apply {
            text = lat.toString()
        }
        val longtTextView = findViewById<TextView>(R.id.longitudetv).apply {
            text = long.toString()
        }

        val mapImg = getStaticMap().execute(latLng);

        val mapView = findViewById<ImageView>(R.id.imageView).apply{
            setImageBitmap(mapImg.get())
        }
    }
    companion object{
        class getStaticMap(): AsyncTask<String, Void, Bitmap>(){

            override fun doInBackground(vararg params: String?): Bitmap {

                var latLng: String = params[0]!!
                var data = HttpRequestHandler();
                var url: String = "https://maps.googleapis.com/maps/api/staticmap?center=$latLng&zoom=16&size=400x400&markers=color:blue%7Clabel:S%7C$latLng&key=AIzaSyDcRu3LnakPAm8gHksuQI6jV3AfUME2PAk";
                val mapImage: Bitmap = data.getURLImage(url)!!;
                return mapImage;
                }
            }
        }
    }