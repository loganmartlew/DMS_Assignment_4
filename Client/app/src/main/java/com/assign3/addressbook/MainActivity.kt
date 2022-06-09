package com.assign3.addressbook

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.ActionProvider
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.assign3.addressbook.activities.AppActivity
import com.assign3.addressbook.api.ApiInterface
import com.assign3.addressbook.api.UserDTO
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProvider: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)


        getCurrentLocation()

    }

    fun getCurrentLocation(){
        if(checkPermissions()){
            if(isLocationEnabled()){

            }else{
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_CODE)
    }

    companion object{
        private const val PERMISSION_REQUEST_CODE = 10
    }

    private fun checkPermissions(): Boolean{
        if(ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode.equals(PERMISSION_REQUEST_CODE)){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            }else{
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onClick(view: View) {
        val intent = Intent(this@MainActivity, AppActivity::class.java);
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val name = nameInput.text.toString()

        val apiInterface = ApiInterface.create().login(UserDTO(name))
        apiInterface.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    intent.putExtra("Username", name);
                    startActivity(intent)
                } else {
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }
}
