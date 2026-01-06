package com.lesite.app

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var loadingContainer: FrameLayout
    private lateinit var errorContainer: LinearLayout
    private lateinit var errorText: TextView
    private lateinit var retryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupWebView()
        setupSwipeRefresh()
        setupErrorHandling()
        hideSystemUI()
        
        loadWebsite()
    }

    private fun initViews() {
        webView = findViewById(R.id.webView)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        loadingContainer = findViewById(R.id.loadingContainer)
        errorContainer = findViewById(R.id.errorContainer)
        errorText = findViewById(R.id.errorText)
        retryButton = findViewById(R.id.retryButton)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = false
            displayZoomControls = false
            allowFileAccess = true
            allowContentAccess = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showLoading()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                hideLoading()
                swipeRefresh.isRefreshing = false
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                if (request?.isForMainFrame == true) {
                    showError(error?.description?.toString() ?: getString(R.string.error_loading))
                }
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                // Optionnel: afficher la progression
            }
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.primary),
            ContextCompat.getColor(this, R.color.accent)
        )
        swipeRefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(this, R.color.bg)
        )
        swipeRefresh.setOnRefreshListener {
            webView.reload()
        }
    }

    private fun setupErrorHandling() {
        retryButton.setOnClickListener {
            loadWebsite()
        }
    }

    private fun loadWebsite() {
        if (isNetworkAvailable()) {
            hideError()
            val url = getString(R.string.website_url)
            webView.loadUrl(url)
        } else {
            showError(getString(R.string.no_internet))
        }
    }

    private fun showLoading() {
        loadingContainer.visibility = View.VISIBLE
        errorContainer.visibility = View.GONE
    }

    private fun hideLoading() {
        loadingContainer.visibility = View.GONE
    }

    private fun showError(message: String) {
        loadingContainer.visibility = View.GONE
        errorContainer.visibility = View.VISIBLE
        errorText.text = message
        swipeRefresh.isRefreshing = false
    }

    private fun hideError() {
        errorContainer.visibility = View.GONE
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}