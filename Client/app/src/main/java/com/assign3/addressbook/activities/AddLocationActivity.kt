package com.assign3.addressbook.activities

import com.assign3.addressbook.HttpRequests.HttpRequestHandler
import android.content.Intent
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
import kotlin.reflect.KFunction3


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

            fun callback(lat: Double?, lng: Double?, address: String?) {
                val dto = LocationDTO(nameInput.text.toString(), addressInput.text.toString(), lat.toString(), lng.toString(), name!!)

                val apiInterface = ApiInterface.create().createLocation(dto)
                apiInterface.enqueue(object: Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        val intent = Intent(this@AddLocationActivity, AppActivity::class.java);
                        intent.putExtra("Username", name);
                        startActivity(intent);
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("AddLocationActivity", "Failed to add location")
                    }
                })
            }

            // Get the name and address from the input fields
            GetLatLngFromAdd(::callback).execute(addressInput.text.toString().replace(" ", "+"))
            getAddressFromLatLng(::callback).execute(lat.toString(), lng.toString());
        }
}
    // Get the latitude and longitude from the address
    companion object{
        class GetLatLngFromAdd(var callback: KFunction3<Double?, Double?, String?, Unit>) : AsyncTask<String, Void, String>() {

            var lat: Double = 0.0;
            var lng: Double = 0.0;

            override fun onPostExecute(result: String?) {
                try {
                    var jsonObject = JSONObject(result);

                    // Get the latitude and longitude from the JSON object
                    lat = jsonObject.getJSONArray("results")
                        .getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").getDouble("lat");

                    lng = jsonObject.getJSONArray("results")
                        .getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").getDouble("lng");

                    // Call the callback function
                    callback(lat, lng, null)

                }catch (e: Exception){
                    println(e);
                    Log.d("AddLocationActivity", "Failed to get lat lng post execute")
                }
            }

            override fun doInBackground(vararg params: String): String {
                var result = "";
                try {
                    var address: String = params[0];
                    var data = HttpRequestHandler();
                    var url: String = String.format(
                        "https://maps.googleapis.com/maps/api/geocode/json?address=$address&key=AIzaSyDcRu3LnakPAm8gHksuQI6jV3AfUME2PAk");
                    result = data.getURLData(url);
                    return result;
                }catch (e: Exception){
                    println(e);
                    Log.d("AddLocationActivity", "Failed to get lat lng do in background")
                }
                return result;
            }

        }
        // Get the address from the latitude and longitude
        class getAddressFromLatLng(var callback: KFunction3<Double?, Double?, String?, Unit>) : AsyncTask<String, Void, String>() {

            var address: String = "";

            override fun onPostExecute(result: String?) {
                try {

                    var jsonObject = JSONObject(result);
                    address = jsonObject.getJSONArray("results")
                        .getJSONObject(0).getString("formatted_address");

                    callback(null,null,address)

                }catch (e: Exception){
                    println(e);
                    Log.d("AddLocationActivity", "Failed to get address from lat lng post execute")
                }
            }

            override fun doInBackground(vararg params: String): String {
                var result = "";
                try {
                    var lat: Double = params[0].toDouble();
                    var lng: Double = params[1].toDouble();
                    var data = HttpRequestHandler();
                    var url: String = String.format(
                        "https://maps.googleapis.com/maps/api/geocode/json?latlng=$lat,$lng&key=AIzaSyDcRu3LnakPAm8gHksuQI6jV3AfUME2PAk");
                    result = data.getURLData(url);
                    return result;
                }catch (e: Exception){
                    println(e);
                    Log.d("AddLocationActivity", "Failed to get address from lat lng do in background")
                }
                return result;
            }
        }
    }
}