package com.carnot.leadgeneration

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private var webView: WebView? = null
    private var logout: ImageView? = null

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 123
        private const val REQUEST_CAMERA_PERMISSION = 124
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        webView = findViewById(R.id.webview)
        logout = findViewById(R.id.logout)

        logout?.setOnClickListener {
            SharedPreferencesUtil.setUsername(this@MainActivity,"")
            val intent = Intent(this@MainActivity, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }

        val webSettings = webView!!.settings
        webSettings.javaScriptEnabled = true // Enable JavaScript if needed
        webSettings.domStorageEnabled = true
        webSettings.mediaPlaybackRequiresUserGesture = false
        webView!!.webViewClient = client
        webView!!.webChromeClient =
            MyWebChromeClient() // Set ChromeClient to handle JavaScript dialogs, etc.

        val userName = SharedPreferencesUtil.getUsername(this@MainActivity)
        // Load a webpage
        webView!!.loadUrl("https://main.d1wqrfxgnwts0g.amplifyapp.com/?user=$userName")
        webView!!.visibility = View.GONE
    }

    override fun onBackPressed() {
        // Check if the WebView can go back
        if (webView!!.canGoBack()) {
            webView!!.goBack() // Navigate back in WebView history
        } else {
            super.onBackPressed() // If no history, proceed with the default back button behavior
        }
    }

    inner class MyWebChromeClient : WebChromeClient() {
        override fun onGeolocationPermissionsShowPrompt(
            origin: String?,
            callback: GeolocationPermissions.Callback?
        ) {
            val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(permission),
                    REQUEST_LOCATION_PERMISSION
                )
            } else {
                callback?.invoke(origin, true, false)
            }
        }

        override fun onPermissionRequest(request: PermissionRequest?) {
           // if (request?.resources?.contains(PermissionRequest.RESOURCE_VIDEO_CAPTURE) == true) {
                val permission = android.Manifest.permission.CAMERA
                if (ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(permission),
                        REQUEST_CAMERA_PERMISSION
                    )
                } else {
                    request?.grant(request.resources)
                }
            //}
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION || requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, reload the webpage
                webView?.reload()
            } else {
                // Permission denied, handle accordingly
                // For example, display a message to the user
            }
        }
    }


    private val client: WebViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {

        }

        override fun onPageFinished(view: WebView, url: String) {
            webView!!.visibility = View.VISIBLE
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {

        }

        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {

            val url = request.url.toString()
            view.loadUrl(url)
            return false
        }
    }
}