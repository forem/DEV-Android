package to.dev.dev_android.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import to.dev.dev_android.base.BuildConfig
import to.dev.dev_android.view.main.view.CustomWebViewClient
import java.util.*


/**
 * This class currently is empty because more methods would be added to it
 * when new bridge functionalities are added.
 */
class AndroidWebViewBridge(private val context: Context) : Player.EventListener {

    var webViewClient: CustomWebViewClient? = null
    private var player: SimpleExoPlayer? = null
    private val timer = java.util.Timer()
    private val timeUpdateTask = object: TimerTask() {
        override fun run() {
            val mainHandler = Handler(context.mainLooper)
            mainHandler.post(Runnable { podcastTimeUpdate() })
        }
    }

    /**
     * Every method that has to be accessed from web-view needs to be marked with
     * `@JavascriptInterface`.
     * This is currently just a sample method which logs an error to Logcat.
     */
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
        try {
            if (player == null) {
                player = SimpleExoPlayer.Builder(context).build()
                player?.addListener(this)
            }

            var dataSourceFactory = DefaultDataSourceFactory(context, BuildConfig.userAgent)
            var streamUri = Uri.parse(url)
            var mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(streamUri)
            player?.prepare(mediaSource)
        } catch (e: Exception) {
            Log.e("PODCAST", e.toString())
        }
    }

    @JavascriptInterface
    fun playPodcast(seconds: String) {
        player?.setPlayWhenReady(true)
        timer.schedule(timeUpdateTask, 0, 1000)
    }

    @JavascriptInterface
    fun pausePodcast() {
        player?.setPlayWhenReady(false)
    }

    @JavascriptInterface
    fun terminatePodcast() {
        timer.cancel()
        player?.release()
        player = null
    }

    fun podcastTimeUpdate() {
        val position = (player?.contentPosition ?: 0 / 1000.0).toString()
        val duration = (player?.duration ?: 0 / 1000.0).toString()
        val message = mapOf("action" to "tick", "duration" to duration, "currentTime" to position)
        Log.i("PODCAST", message.toString())
        webViewClient?.sendPodcastMessage(message)
    }
}
