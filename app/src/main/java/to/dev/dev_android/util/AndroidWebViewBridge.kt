package to.dev.dev_android.util

import android.content.*
import android.os.IBinder
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import to.dev.dev_android.media.AudioService
import to.dev.dev_android.view.main.view.CustomWebViewClient
import java.util.*

class AndroidWebViewBridge(private val context: Context) {

    var webViewClient: CustomWebViewClient? = null
    private val timer = Timer()

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
        clipboard?.primaryClip = clipData
    }

    @JavascriptInterface
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    @JavascriptInterface
    fun loadPodcast(url: String) {
        AudioService.newIntent(context, url).also { intent ->
            // This service will get converted to foreground service using the PlayerNotificationManager notification Id.
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        val timeUpdateTask = object: TimerTask() {
            override fun run() {
                podcastTimeUpdate()
            }
        }
        timer.schedule(timeUpdateTask, 0, 1000)
    }

    @JavascriptInterface
    fun playPodcast(seconds: String) {
        audioService?.play()
    }

    @JavascriptInterface
    fun pausePodcast() {
        audioService?.pause()
    }

    @JavascriptInterface
    fun metadataPodcast(episodeName: String, podcastName: String, imageUrl: String) {
        audioService?.loadMetadata(episodeName, podcastName, imageUrl)
    }

    @JavascriptInterface
    fun terminatePodcast() {
        audioService?.pause()
        context.unbindService(connection)
        audioService = null
        context.stopService(Intent(context, AudioService::class.java))
    }

    @JavascriptInterface
    fun seekPodcast(seconds: Float) {
        audioService?.seekTo(seconds)
    }

    @JavascriptInterface
    fun ratePodcast(rate: Float) {
        audioService?.rate(rate)
    }

    @JavascriptInterface
    fun mutePodcast(muted: Boolean) {
        audioService?.mute(muted)
    }

    fun podcastTimeUpdate() {
        audioService?.let {
            val time = it.currentTimeInSec() / 1000
            val duration = it.durationInSec() / 1000
            val message = mapOf("action" to "tick", "duration" to duration, "currentTime" to time)
            webViewClient?.sendPodcastMessage(message)
        }
    }
}
