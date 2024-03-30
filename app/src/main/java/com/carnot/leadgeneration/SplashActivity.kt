package com.carnot.leadgeneration

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Check if permissions are granted
        if (!checkPermissions()) {
            // Request permissions if not granted
            requestPermissions()
        } else {
            // If permissions are already granted, move to the main activity
            moveToMainActivity()
        }
    }

    // Method to check if all permissions are granted
    private fun checkPermissions(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    // Method to request permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE)
    }

    // Method to handle permission request results
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            var allPermissionsGranted = true
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false
                    break
                }
            }
            if (allPermissionsGranted) {
                // If all permissions are granted, move to the main activity
                moveToMainActivity()
            } else {
                // If any permission is denied, request permissions again or handle accordingly
                Toast.makeText(
                    this,
                    "Permissions denied. Please grant all permissions to proceed.",
                    Toast.LENGTH_SHORT
                ).show()
                requestPermissions()
            }
        }
    }

    // Method to move to the main activity after a delay
    private fun moveToMainActivity() {
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000) // Delay in milliseconds
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}