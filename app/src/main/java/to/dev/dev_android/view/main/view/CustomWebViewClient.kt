package to.dev.dev_android.view.main.view

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent

class CustomWebViewClient(val context: Context) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (url.contains("://dev.to")) {
            return false;
        } else {
            if(url.contains("api.twitter.com/oauth") or url.contains("github.com/login")) {
                openBrowser(url)
                return true
            }
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(-0x1000000)
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(url))
            return true;
        }

    }

    private fun openBrowser(url: String): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
        return true
    }
}