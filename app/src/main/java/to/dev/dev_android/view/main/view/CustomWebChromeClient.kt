package to.dev.dev_android.view.main.view

import android.content.Context
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import to.dev.dev_android.databinding.ActivityMainBinding


class CustomWebChromeClient(val context: Context,
                            val binding: ActivityMainBinding,
                            private val listener: CustomListener) : WebChromeClient() {

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        listener.launchGallery(filePathCallback)
        return true
    }

    interface CustomListener {
        fun launchGallery(filePathCallback: ValueCallback<Array<Uri>>?)
    }
}