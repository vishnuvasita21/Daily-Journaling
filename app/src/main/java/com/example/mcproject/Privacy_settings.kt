package com.example.mcproject

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar


class PrivacySettingsActivity : AppCompatActivity() {

    private lateinit var switchCamera: Switch
    private lateinit var switchGPS: Switch
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        preferences = getSharedPreferences("PrivacySettings", Context.MODE_PRIVATE)

        switchCamera = findViewById(R.id.cameraSwitch)
        switchGPS = findViewById(R.id.gpsSwitch)

        val backButton:Button = findViewById(R.id.privacyBackButton)
        backButton.setOnClickListener {
            finish()
        }

        loadSavedPreferences()

        switchCamera.setOnCheckedChangeListener { _, isChecked ->
            savePreference("Camera", isChecked)
        }

        switchGPS.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, "GPS")
        }
    }

    private fun loadSavedPreferences() {
        switchCamera.isChecked = preferences.getBoolean("Camera", false)
        switchGPS.isChecked = preferences.getBoolean("GPS", false)
    }

    private fun requestPermission(permission: String, name: String) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
            updateSwitchState(name, false)
        } else {
            savePreference(name, true)
        }
    }

    private fun updateSwitchState(name: String, state: Boolean) {
        when (name) {
            "Camera" -> switchCamera.isChecked = state
            "GPS" -> switchGPS.isChecked = state
        }
    }

    private fun showPermissionDeniedSnackbar(message: String) {
        Snackbar.make(
            findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        permissions.forEachIndexed { index, permission ->
            if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                when (permission) {
                    Manifest.permission.CAMERA -> savePreference("Camera", true)
                    Manifest.permission.ACCESS_FINE_LOCATION -> savePreference("GPS", true)
                }
            } else {
                when (permission) {
                    Manifest.permission.CAMERA -> {
                        updateSwitchState("Camera", false)
                        showPermissionDeniedSnackbar("Camera permission denied.")
                    }
                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        updateSwitchState("GPS", false)
                        showPermissionDeniedSnackbar("GPS permission denied.")
                    }
                }
            }
        }
    }

    private fun savePreference(key: String, value: Boolean) {
        with(preferences.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

}