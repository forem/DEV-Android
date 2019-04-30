package to.dev.dev_android.view.main.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.browser.customtabs.CustomTabsIntent
import to.dev.dev_android.databinding.ActivityMainBinding

class CustomWebViewClient(private val context: Context, private val binding: ActivityMainBinding) : WebViewClient() {
    override fun onPageFinished(view: WebView, url: String?) {
        binding.splash.visibility = View.GONE
        view.visibility = View.VISIBLE
        super.onPageFinished(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (view.originalUrl == "https://dev.to/signout_confirm" && url == "https://dev.to/") {
            view.clearCache(true)
            view.clearFormData()
            view.clearHistory()
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                CookieManager.getInstance().removeAllCookie()
            } else {
                CookieManager.getInstance().removeAllCookies(null)
            }
        }

        if (url.contains("://dev.to")) {
            return false
        } else {
            if (url.contains("api.twitter.com/oauth") ||
                url.contains("api.twitter.com/account/login_verification") ||
                url.contains("github.com/login") ||
                url.contains("github.com/sessions/")
            ) {
                return false
            }
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(-0x1000000)
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(url))
            return true
        }

    }

    private fun openBrowser(url: String): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
        return true
    }
}