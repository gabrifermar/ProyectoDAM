package com.gabrifermar.proyectodam

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.gabrifermar.proyectodam.databinding.ActivityChartsBinding
import java.util.jar.Manifest

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


        //binding.chartsWvWebview.loadUrl("https://aip.enaire.es/AIP/contenido_AIP/AD/AD2/LEMD/LE_AD_2_LEMD_PDC_2_en.pdf")
        binding.chartsWvWebview.loadUrl("https://aip.enaire.es/AIP/")
    }

    //TODO https://vlemon.com/blog/android/android-webview-how-to-download-a-file-using-kotlin download files
    private fun checkWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    //Permission denied, dialog before requesting permission
                    val builder=AlertDialog.Builder(this)
                    builder.setMessage(getString(R.string.writepermission))
                    builder.setTitle(getString(R.string.writepermissiontitle))
                    builder.setPositiveButton("OK"){_,_ ->
                        //ok button request permission
                        ActivityCompat.requestPermissions(this  , arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),102)
                    }
                    builder.setNeutralButton(getString(R.string.cancel),null)
                    builder.create().show()
                }else{
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),102)
                }

            }
        }
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