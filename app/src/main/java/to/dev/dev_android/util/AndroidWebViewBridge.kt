package to.dev.dev_android.util

import android.content.*
import android.os.IBinder
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import to.dev.dev_android.activities.VideoPlayerActivity
import to.dev.dev_android.events.VideoPlayerPauseEvent
import to.dev.dev_android.events.VideoPlayerTickEvent
import to.dev.dev_android.media.AudioService
import to.dev.dev_android.webclients.CustomWebViewClient
import java.util.*

class AndroidWebViewBridge(private val context: Context) {

    var webViewClient: CustomWebViewClient? = null
    private var timer: Timer? = null

    // audioService is initialized when onServiceConnected is executed after/during binding is done
    private var audioService: AudioService? = null
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as AudioService.AudioServiceBinder
            audioService = binder.service
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioService = null
        }
    }

    @JavascriptInterface
    fun logError(errorTag: String, errorMessage: String) {
        Log.e(errorTag, errorMessage)
    }

    @JavascriptInterface
    fun copyToClipboard(copyText: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clipData = ClipData.newPlainText("DEV Community", copyText)
        clipboard?.setPrimaryClip(clipData)
    }

    @JavascriptInterface
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    @JavascriptInterface
    fun podcastMessage(message: String) {
        var map: Map<String, String> = HashMap()
        map = Gson().fromJson(message, map.javaClass)
        when(map["action"]) {
            "load" -> loadPodcast(map["url"])
            "play" -> audioService?.play(map["url"], map["seconds"])
            "pause" -> audioService?.pause()
            "seek" -> audioService?.seekTo(map["seconds"])
            "rate" -> audioService?.rate(map["rate"])
            "muted" -> audioService?.mute(map["muted"])
            "volume" -> audioService?.volume(map["volume"])
            "metadata" -> audioService?.loadMetadata(map["episodeName"], map["podcastName"], map["imageUrl"])
            "terminate" -> terminatePodcast()
            else -> logError("Podcast Error", "Unknown action")
        }
    }

    @JavascriptInterface
    fun videoMessage(message: String) {
        var map: Map<String, String> = HashMap()
        map = Gson().fromJson(message, map.javaClass)
        when(map["action"]) {
            "play" -> playVideo(map["url"], map["seconds"])
            else -> logError("Video Error", "Unknown action")
        }
    }

    fun playVideo(url: String?, seconds: String?) {
        url?.let {
            // Pause the audio player in case the user is currently listening to a audio (podcast)
            audioService?.pause()
            timer?.cancel()

            // Launch VideoPlayerActivity
            val intent = VideoPlayerActivity.newIntent(context, url, seconds ?: "0")
            context.startActivity(intent)

            EventBus.getDefault().register(this)
        }
    }

    fun loadPodcast(url: String?) {
        if (url == null) return

        AudioService.newIntent(context, url).also { intent ->
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        // Clear out lingering timer if it exists & recreate
        timer?.cancel()
        timer = Timer()
        val timeUpdateTask = object: TimerTask() {
            override fun run() {
                podcastTimeUpdate()
            }
        }
        timer?.schedule(timeUpdateTask, 0, 1000)
    }

    fun terminatePodcast() {
        timer?.cancel()
        timer = null
        audioService?.pause()
        context.unbindService(connection)
        audioService = null
        context.stopService(Intent(context, AudioService::class.java))
    }

    fun podcastTimeUpdate() {
        audioService?.let {
            val time = it.currentTimeInSec() / 1000
            val duration = it.durationInSec() / 1000
            if (duration < 0) {
                // The duration overflows into a negative when waiting to load audio (initializing)
                webViewClient?.sendBridgeMessage("podcast", mapOf("action" to "init"))
            } else {
                val message = mapOf("action" to "tick", "duration" to duration, "currentTime" to time)
                webViewClient?.sendBridgeMessage("podcast", message)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: VideoPlayerPauseEvent) {
        webViewClient?.sendBridgeMessage("video", mapOf("action" to event.action))
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: VideoPlayerTickEvent) {
        val message = mapOf("action" to event.action, "currentTime" to event.seconds)
        webViewClient?.sendBridgeMessage("video", message)
    }

    /**
     * This method is used to open the native share-sheet of Android when simple text is to be
     * shared from the web-view.
     */
    @JavascriptInterface
    fun shareText(text: String) {
        val shareIntent = Intent.createChooser(
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            },
            null
        )
        context.startActivity(shareIntent)
    }
}
