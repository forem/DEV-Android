package to.dev.dev_android.view.main.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import androidx.core.app.ActivityCompat
import to.dev.dev_android.R
import to.dev.dev_android.base.BuildConfig
import to.dev.dev_android.base.activity.BaseActivity
import to.dev.dev_android.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(), CustomWebChromeClient.CustomListener {

    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null

    override fun layout(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWebViewSettings()
        savedInstanceState?.let { restoreState(it) }?: navigateToHome()
        handleIntent(intent)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        binding.webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val appLinkData: Uri? = intent.data
        if (appLinkData != null) {
            binding.webView.loadUrl(appLinkData.toString())
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebViewSettings() {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.webViewClient = CustomWebViewClient(this@MainActivity, binding)
        binding.webView.webChromeClient = CustomWebChromeClient(BuildConfig.baseUrl, binding, this)
    }

    private fun restoreState(savedInstanceState: Bundle){
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
        mFilePathCallback = filePathCallback

        val galleryIntent = Intent()
        // Show only images, no videos or anything else
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_PICK

        // Always show the chooser (if there are multiple options available)
        ActivityCompat.startActivityForResult(
            this,
            Intent.createChooser(galleryIntent, "Select Picture"),
            PIC_CHOOSER_REQUEST,
            null    // No additional data
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != PIC_CHOOSER_REQUEST) {
            return super.onActivityResult(requestCode, resultCode, data)
        }

        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                mFilePathCallback?.onReceiveValue(arrayOf(data.data))
                mFilePathCallback = null
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            mFilePathCallback?.onReceiveValue(null)
            mFilePathCallback = null
        }
    }

    companion object {
        private const val PIC_CHOOSER_REQUEST = 100
    }
}
