package com.gabrifermar.proyectodam

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.SearchView
import com.gabrifermar.proyectodam.databinding.ActivityChartsBinding

class Charts : AppCompatActivity() {

    private lateinit var binding: ActivityChartsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //refresh webview
        binding.chartsRlRefresh.setOnRefreshListener {
            binding.chartsWvWebview.reload()
        }

        //search
        binding.chartsSvSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if(URLUtil.isValidUrl(it)){
                        //valid URL
                        binding.chartsWvWebview.loadUrl(it)
                    }else{
                        //not full URL
                        binding.chartsWvWebview.loadUrl("https://aip.enaire.es/AIP/#${it.uppercase()}")
                    }
                }
                return false
            }
        })

        //TODO:download pdf with charts to see them

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

                binding.chartsRlRefresh.isRefreshing=true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                binding.chartsRlRefresh.isRefreshing=false

            }

        }

        val settings = binding.chartsWvWebview.settings
        settings.javaScriptEnabled = true

        //binding.chartsWvWebview.loadUrl("https://aip.enaire.es/AIP/contenido_AIP/AD/AD2/LEMD/LE_AD_2_LEMD_PDC_2_en.pdf")
        binding.chartsWvWebview.loadUrl("https://aip.enaire.es/AIP/")
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