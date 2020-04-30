package to.dev.dev_android.view.main.view

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.browser.customtabs.CustomTabsIntent
import org.json.JSONObject
import com.pusher.pushnotifications.PushNotifications
import java.lang.Exception

class CustomWebViewClient(
    private val context: Context,
    private val view: WebView,
    private val onPageFinish: () -> Unit
) : WebViewClient() {

    private val overrideUrlList = listOf(
        "://dev.to",
        "api.twitter.com/oauth",
        "api.twitter.com/account/login_verification",
        "github.com/login",
        "github.com/sessions/"
    )

    private var registeredUserNotifications = false

    override fun onPageFinished(view: WebView, url: String?) {
        onPageFinish()
        view.visibility = View.VISIBLE
        super.onPageFinished(view, url)
    }

    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
        val javascript = "JSON.parse(document.getElementsByTagName('body')[0].getAttribute('data-user')).id"
        view?.evaluateJavascript(javascript) { result ->
            if (result != "null" && !registeredUserNotifications) {
                try {
                    val userId = result.toString().toInt()
                    PushNotifications.addDeviceInterest("user-notifications-$userId")
                    registeredUserNotifications = true
                }
                catch (e: Exception) {
                    println(e)
                }
            }
        }

        super.doUpdateVisitedHistory(view, url, isReload)
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

        if (overrideUrlList.any { url.contains(it) }) {
            return false
        }

        CustomTabsIntent.Builder()
            .setToolbarColor(Color.parseColor("#00000000"))
            .build()
            .also { it.launchUrl(context, Uri.parse(url)) }

        return true
    }

    fun sendPodcastMessage(message: Map<String, Any>) {
        val jsonMessage = JSONObject(message).toString()
        val javascript = "document.getElementById('audiocontent').setAttribute('data-podcast', '$jsonMessage')"
        view?.post(Runnable {
            view?.evaluateJavascript(javascript, null)
        })
    }
}