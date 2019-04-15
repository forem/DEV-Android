package to.dev.dev_android.view.main.view

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import to.dev.dev_android.databinding.ActivityMainBinding

class CustomWebViewClient(val context: Context, val binding: ActivityMainBinding) : WebViewClient() {
    override fun onPageFinished(view: WebView, url: String?) {
        binding.splash.visibility = View.GONE
        view.visibility = View.VISIBLE
        super.onPageFinished(view, url)
    }
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