package to.dev.dev_android.view.main.view

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import to.dev.dev_android.databinding.ActivityMainBinding


class CustomWebChromeClient(
    val baseURL: String,
    val binding: ActivityMainBinding,
    private val listener: CustomListener
) : WebChromeClient() {

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        listener.launchGallery(filePathCallback)
        return true
    }

    override fun onProgressChanged(view: WebView, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        if (newProgress == 100 && view.url == baseURL) {
            view.clearHistory()
        }
    }

    interface CustomListener {
        fun launchGallery(filePathCallback: ValueCallback<Array<Uri>>?)
    }
}