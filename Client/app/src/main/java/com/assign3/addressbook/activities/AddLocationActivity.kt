package com.assign3.addressbook.activities

import HttpRequests.HttpRequestHandler
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.assign3.addressbook.R
import com.assign3.addressbook.api.ApiInterface
import com.assign3.addressbook.api.LocationDTO
import com.assign3.addressbook.models.Location
import org.json.JSONObject
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

        button.setOnClickListener{

            // Get the name and address from the input fields
            getLatLngFromAdd().execute(addressInput.text.toString().replace(" ", "+"));

            val dto = LocationDTO(nameInput.text.toString(), addressInput.text.toString(), "0.0", "0.0", name!!)
            val apiInterface = ApiInterface.create().createLocation(dto)
            apiInterface.enqueue(object: Callback<Location> {
                override fun onResponse(call: Call<Location>, response: Response<Location>) {
                    val intent = Intent(this@AddLocationActivity, AppActivity::class.java);
                    intent.putExtra("Username", name);
                    startActivity(intent);
                }

                override fun onFailure(call: Call<Location>, t: Throwable) {
                    Log.d("AddLocationActivity", "Failed to add location")
                }
            })
        }
}
    // Get the latitude and longitude from the address
    companion object{
        class getLatLngFromAdd() : AsyncTask<String, Void, String>() {

            override fun onPostExecute(result: String?) {
                try {
                    var jsonObject: JSONObject = JSONObject(result);

                    var lat: Double = jsonObject.getJSONArray("results")
                        .getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").getDouble("lat");

                    var lng: Double = jsonObject.getJSONArray("results")
                        .getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").getDouble("lng");

                }catch (e: Exception){
                    Log.d("AddLocationActivity", "Failed to get lat lng")
                }
            }

            override fun doInBackground(vararg params: String): String {
                var result: String = "";
                try {
                    var address: String = params[0];
                    var data: HttpRequestHandler = HttpRequestHandler();
                    var url: String = String.format(
                        "https://maps.googleapis.com/maps/api/geocode/json?address=%s", address);
                    result = data.getURLData(url);
                    return result;
                }catch (e: Exception){
                    Log.d("AddLocationActivity", "Failed to get lat lng");
                }
                return result;
            }

        }
    }
}