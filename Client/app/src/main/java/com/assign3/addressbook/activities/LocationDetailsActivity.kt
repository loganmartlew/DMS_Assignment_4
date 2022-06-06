package com.assign3.addressbook.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.assign3.addressbook.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions

class LocationDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    val lat get() = intent.getDoubleExtra("lat", 0.0)
    val long get() = intent.getDoubleExtra("long", 0.0)
    val name get() = intent.getStringExtra("name")
    val address get() = intent.getStringExtra("address")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_details)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

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
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val addressMarker = LatLng(lat, long)
        googleMap.addMarker(
            MarkerOptions()
                .position(addressMarker)
                .title(name + "'s Location")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(addressMarker))
    }
}