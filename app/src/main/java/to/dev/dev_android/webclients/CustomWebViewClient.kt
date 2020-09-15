package to.dev.dev_android.webclients

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.View
import android.webkit.*
import androidx.browser.customtabs.CustomTabsIntent
import org.json.JSONObject
import com.pusher.pushnotifications.PushNotifications
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import to.dev.dev_android.events.NetworkStatusEvent
import to.dev.dev_android.util.network.*
import java.lang.Exception
import java.lang.Runnable

class CustomWebViewClient(
    private val context: Context,
    private val view: WebView,
    private val coroutineScope: CoroutineScope,
    private val onPageFinish: () -> Unit
) : WebViewClient() {

    private val overrideUrlList = listOf(
        "://dev.to",
        "api.twitter.com/oauth",
        "api.twitter.com/login/error",
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

    fun sendBridgeMessage(type: String, message: Map<String, Any>) {
        val jsonMessage = JSONObject(message).toString()
        var javascript = ""
        when(type) {
            "podcast" -> javascript = "document.getElementById('audiocontent').setAttribute('data-podcast', '$jsonMessage')"
            "video" -> javascript = "document.getElementById('video-player-source').setAttribute('data-message', '$jsonMessage')"
            else -> return
        }
        view?.post(Runnable {
            view?.evaluateJavascript(javascript, null)
        })
    }

    private var networkWatcher: NetworkWatcher? = null
    private fun registerNetworkWatcher() {
        if (networkWatcher != null) return

        unregisterNetworkWatcher()

        networkWatcher = NetworkWatcher(coroutineScope)
    }

    private fun unregisterNetworkWatcher() {
        networkWatcher?.let {
            context.unregisterReceiver(it)

            networkWatcher = null
        }
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)

        coroutineScope.launch {
            if (async { NetworkUtils.isOffline() }.await()) {
                EventBus.getDefault().post(NetworkStatusEvent(NetworkStatus.OFFLINE))
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onNetworkStatusEvent(event: NetworkStatusEvent) {
        when (event.networkStatus) {
            NetworkStatus.OFFLINE -> {
                registerNetworkWatcher()

                context.registerReceiver(networkWatcher, NetworkWatcher.intentFilter)
            }
            NetworkStatus.BACK_ONLINE -> {
                unregisterNetworkWatcher()

                coroutineScope.launch {
                    withContext(Dispatchers.Main) {
                        view.loadUrl(view.url)
                    }
                }
            }
        }
    }

    fun observeNetwork() {
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
        EventBus.getDefault().register(this)
    }

    fun unobserveNetwork() {
        coroutineScope.cancel()
        EventBus.getDefault().unregister(this)

        unregisterNetworkWatcher()
    }
}