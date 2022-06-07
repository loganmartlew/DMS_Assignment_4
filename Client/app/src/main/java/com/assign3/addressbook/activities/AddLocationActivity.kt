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
import com.assign3.addressbook.api.ApiInterface
import com.assign3.addressbook.api.LocationDTO
import com.assign3.addressbook.models.Location
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AddLocationActivity : AppCompatActivity() {

    var lat = 0.0
    var lng = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_location)

        val name: String? = intent.getStringExtra("Username");

        val nameInput: EditText = findViewById(R.id.nameEditText);
        val addressInput: EditText = findViewById(R.id.addressEditText);
        val button: Button = findViewById(R.id.submitContactButton);

//        getLatLongFromAddress(addressInput.text.toString());



        button.setOnClickListener{
            val dto = LocationDTO(nameInput.text.toString(), addressInput.text.toString(), "0.0", "0.0", name!!)
            val apiInterface = ApiInterface.create().createLocation(dto)
            apiInterface.enqueue(object: Callback<Location> {
                override fun onResponse(call: Call<Location>, response: Response<Location>) {
                    val intent = Intent(this@AddLocationActivity, LocationsListActivity::class.java);
                    intent.putExtra("Username", name);
                    startActivity(intent);
                }

                override fun onFailure(call: Call<Location>, t: Throwable) {
                    Log.d("AddLocationActivity", "Failed to add location")
                }
            })
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