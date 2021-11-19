package com.gabrifermar.proyectodam

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.gabrifermar.proyectodam.databinding.ActivityChartsBinding

class Charts : AppCompatActivity() {

    private lateinit var binding: ActivityChartsBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //refresh webview
        binding.chartsRlRefresh.setOnRefreshListener {
            binding.chartsWvWebview.reload()
        }

        binding.chartsWvWebview.webChromeClient = object : WebChromeClient() {

        }

        binding.chartsWvWebview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            //control refresh icon
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                binding.chartsRlRefresh.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                binding.chartsRlRefresh.isRefreshing = false

            }

        }

        val settings = binding.chartsWvWebview.settings
        settings.javaScriptEnabled = true


        binding.chartsWvWebview.loadUrl("https://aip.enaire.es/AIP")
    }

    override fun onBackPressed() {
        if (binding.chartsWvWebview.canGoBack()) {
            binding.chartsWvWebview.goBack()
        } else {
            super.onBackPressed()
            finish()
        }
    }
}