package to.dev.dev_android.view.main.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import to.dev.dev_android.R
import to.dev.dev_android.base.activity.BaseActivity
import to.dev.dev_android.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(), CustomWebChromeClient.CustomListener {

    override fun layout(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWebViewSettings()
        navigateToHome()
        handleIntent(intent)
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
        binding.webView.webChromeClient = CustomWebChromeClient(this@MainActivity, binding, this)
    }

    private fun navigateToHome() {
        binding.webView.loadUrl(resources.getString(R.string.main_url))
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun launchGallery() {
        val intent = Intent()
        // Show only images, no videos or anything else
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        // Always show the chooser (if there are multiple options available)
        ActivityCompat.startActivityForResult(
            this,
            Intent.createChooser(intent, "Select Picture"),
            PIC_CHOOSER_REQUEST,
            null    // No additional data
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PIC_CHOOSER_REQUEST) {
                if (data != null) {
                    Log.d("MainActivity", "onActivityResult (line 76): data: ${data.data}")
                    val realPathFromUri = getRealPathFromUri(this, data.data)
                    val picPath = realPathFromUri.split("/").last()
                    Log.d("MainActivity", "onActivityResult (line 87): picPath: $picPath" )
                    Log.d("MainActivity", "onActivityResult (line 82): $realPathFromUri")
                    // TODO: Access the file
                    // TODO: Make the API Call??
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getRealPathFromUri(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, projection, null, null, null)
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        } finally {
            cursor?.close()
        }
    }

    companion object {
        private const val PIC_CHOOSER_REQUEST = 100
    }
}
