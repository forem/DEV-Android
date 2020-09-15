package to.dev.dev_android.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebView
import com.pusher.pushnotifications.PushNotifications
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import to.dev.dev_android.R
import to.dev.dev_android.BuildConfig
import to.dev.dev_android.databinding.ActivityMainBinding
import to.dev.dev_android.util.AndroidWebViewBridge
import to.dev.dev_android.webclients.CustomWebChromeClient
import to.dev.dev_android.webclients.CustomWebViewClient

class MainActivity : BaseActivity<ActivityMainBinding>(), CustomWebChromeClient.CustomListener {
    private val webViewBridge: AndroidWebViewBridge = AndroidWebViewBridge(this)
    private lateinit var webViewClient: CustomWebViewClient

    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    private val mainActivityScope = MainScope()

    override fun layout(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWebViewSettings()
        savedInstanceState?.let { restoreState(it) } ?: navigateToHome()
        handleIntent(intent)
        PushNotifications.start(applicationContext, BuildConfig.pusherInstanceId)
        PushNotifications.addDeviceInterest("broadcast")
    }

    override fun onResume() {
        if (intent.extras != null && intent.extras!!["url"] != null) {
            val targetUrl = intent.extras!!["url"].toString()
            try {
                val targetHost = Uri.parse(targetUrl).host ?: ""
                if (targetHost.contains(BuildConfig.baseHostname)) {
                    binding.webView.loadUrl(targetUrl)
                }
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.message)
            }
        }

        super.onResume()
        webViewClient.observeNetwork()
    }

    override fun onStop() {
        super.onStop()
        webViewClient.unobserveNetwork()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Make sure we're not leaving any audio playing behind
        webViewBridge.terminatePodcast()

        // Coroutine cleanup
        mainActivityScope.cancel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding.webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val appLinkData: Uri? = intent.data
        appLinkData?.host?.let {
            binding.webView.loadUrl(appLinkData.toString())
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebViewSettings() {
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.userAgentString = BuildConfig.userAgent

        binding.webView.addJavascriptInterface(webViewBridge, "AndroidBridge")
        webViewClient = CustomWebViewClient(
            this@MainActivity,
            binding.webView,
            mainActivityScope
        ) {
            binding.splash.visibility = View.GONE
        }
        binding.webView.webViewClient = webViewClient
        webViewBridge.webViewClient = webViewClient
        binding.webView.webChromeClient = CustomWebChromeClient(BuildConfig.baseUrl, this)
    }

    private fun restoreState(savedInstanceState: Bundle) {
        binding.webView.restoreState(savedInstanceState)
    }

    private fun navigateToHome() {
        binding.webView.loadUrl(BuildConfig.baseUrl)
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun launchGallery(filePathCallback: ValueCallback<Array<Uri>>?) {
        this.filePathCallback = filePathCallback

        val galleryIntent = Intent().apply {
            // Show only images, no videos or anything else
            type = "image/*"
            action = Intent.ACTION_PICK
        }

        // Always show the chooser (if there are multiple options available)
        startActivityForResult(
            Intent.createChooser(galleryIntent, "Select Picture"),
            PIC_CHOOSER_REQUEST,
            null    // No additional data
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != PIC_CHOOSER_REQUEST) {
            return super.onActivityResult(requestCode, resultCode, data)
        }

        when (resultCode) {
            Activity.RESULT_OK -> data?.data?.let {
                filePathCallback?.onReceiveValue(arrayOf(it))
                filePathCallback = null
            }
            Activity.RESULT_CANCELED -> {
                filePathCallback?.onReceiveValue(null)
                filePathCallback = null
            }
        }
    }

    companion object {
        private const val PIC_CHOOSER_REQUEST = 100
        private const val LOG_TAG = "MainActivity"
    }
}
