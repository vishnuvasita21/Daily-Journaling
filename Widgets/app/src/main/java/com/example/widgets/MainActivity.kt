package com.example.widgets

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.Calendar

class MainActivity : AppCompatActivity(),DatePickerDialog.OnDateSetListener {
    private lateinit var locationManager: LocationManager
    private val locationListener = MyLocationListener()
    var day = 0
    var month = 0
    var year = 0
    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    private lateinit var progressBar : ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar);
        pickDate()
        getLocation()
        //val calendarView: CalendarView = findViewById(R.id.calendarView)
//        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
//            val selectedDate = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year)
//            Toast.makeText(
//                this@MainActivity,
//                "Selected date: $selectedDate",
//                Toast.LENGTH_SHORT
//            ).show()
//        }

    }
    private  fun getDatePickerCalendar(){
        val calendar : Calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        val dateButton : Button = findViewById(R.id.DatePickerButton)
        dateButton.text = String.format("%02d-%02d-%d", savedDay, savedMonth , savedYear)

    }

    private fun pickDate() {
        val dateButton : Button = findViewById(R.id.DatePickerButton)
        dateButton.setOnClickListener {
           DatePickerDialog(this,this,year,month,day).show()
        }

    }

    private fun getLocation() {
        val locationPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                when {
                    permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                        requestLocationUpdates()
                    }
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                        requestLocationUpdates()
                    }
                    else -> {

                    }
                }
            }

        // Check if the app has location permission
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                // Permission already granted, proceed to get location
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            else -> {
                // Permission not granted, request it
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }


    private fun requestLocationUpdates() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Request location updates with a specified minTime (in milliseconds) and minDistance (in meters)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_TIME_BETWEEN_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES,
            locationListener
        )
    }


    private inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            showProgressBar()
            val latitude = location.latitude
            val longitude = location.longitude
            Toast.makeText(
                this@MainActivity,
                "Latitude: $latitude, Longitude: $longitude",
                Toast.LENGTH_SHORT
            ).show()

            val text : TextView = findViewById(R.id.TempView);
            val client  =  OkHttpClient()
            val url = "https://api.weatherapi.com/v1/current.json?key=542d8232b6964783abf194227232411&q=44.651070,-63.582687"
            GlobalScope.launch(Dispatchers.IO) {
                val request = Request.Builder()
                    .url(url)
                    .build();
                try {
                    val response: Response = client.newCall(request).execute()
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody)
                    val currentObject = jsonResponse.getJSONObject("current")
                    val tempCelsius = currentObject.getDouble("temp_c")
                    runOnUiThread {
                        hideProgressBar()
                        text.text = "Temperature: $tempCelsius °C"
                    }

                }
                catch (e : IOException){
                    hideProgressBar()
                    text.text = "Error fetching weather"
                    e.printStackTrace()
                }
            }
            locationManager.removeUpdates(this)
        }


        // Implement other methods if needed
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }
    private fun showProgressBar() {
        runOnUiThread {
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar() {
        runOnUiThread {
            progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val MIN_TIME_BETWEEN_UPDATES: Long = 1000 // 1 second
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 1.0F // 1 meter
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year
        getDatePickerCalendar()
    }
}

