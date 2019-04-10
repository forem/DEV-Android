package to.dev.dev_android.view.main.view

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.webkit.WebView
import android.webkit.WebViewClient

class CustomWebViewClient(val context: Context) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (url.startsWith("https://dev.to")) {
            return false;
        } else {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(-0x1000000)
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(url))
            return true;
        }

    }
}