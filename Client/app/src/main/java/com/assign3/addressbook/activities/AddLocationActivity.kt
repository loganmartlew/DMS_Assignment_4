package com.assign3.addressbook.activities

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.assign3.addressbook.R
import java.util.*


class AddLocationActivity : AppCompatActivity() {

    var lat = 0.0
    var lng = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_location)

        val name: String? = intent.getStringExtra("Username");

        val address: EditText = findViewById(R.id.nameEditText);

        getLatLongFromAddress(address.text.toString());

        val intent = Intent(this@AddLocationActivity, LocationsListActivity::class.java);
        val button: Button = findViewById(R.id.addContactButton);

        button.setOnClickListener{
            intent.putExtra("Username", name);
            startActivity(intent);
        }
    }

    private fun getLatLongFromAddress(address: String) {
        val geoCoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address> = geoCoder.getFromLocationName(address, 1)
            if (addresses.isNotEmpty()) {
                lat = addresses[0].getLatitude() as Double;
                lng = addresses[0].getLongitude() as Double;
                Log.d("Latitude", "" + lat)
                Log.d("Longitude", "" + lng)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}