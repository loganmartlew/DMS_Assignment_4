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

    val lat get() = intent.getDoubleExtra("lat", 0.0)
    val long get() = intent.getDoubleExtra("long", 0.0)
    val name get() = intent.getStringExtra("name")
    val address get() = intent.getStringExtra("address")

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_details)

        var latLng: String = "$lat, $long"

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
                val mapImage: Bitmap
                var latLng: String = params[0]!!
                var data = HttpRequestHandler();
                var url: String = String.format(
                    "https://maps.googleapis.com/maps/api/staticmap?center=$latLng&zoom=6&size=400x400\n" +
                            "&markers=color:blue%7Clabel:S%7C$latLng" +
                            "&key=AIzaSyDcRu3LnakPAm8gHksuQI6jV3AfUME2PAk&signature=YOUR_SIGNATURE");
                mapImage = data.getURLImage(url)!!;
                return mapImage;
                }
            }
        }
    }