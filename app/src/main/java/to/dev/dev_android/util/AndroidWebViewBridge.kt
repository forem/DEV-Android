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
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import to.dev.dev_android.base.BuildConfig
import to.dev.dev_android.view.main.view.CustomWebViewClient
import java.util.*

class AndroidWebViewBridge(private val context: Context) {

    var webViewClient: CustomWebViewClient? = null
    private val timer = Timer()

    private var player: SimpleExoPlayer? = null
    private var playerHandler: Handler? = null

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
                initPlayer()
            }

            val dataSourceFactory = DefaultDataSourceFactory(context, BuildConfig.userAgent)
            val streamUri = Uri.parse(url)
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(streamUri)
            player?.prepare(mediaSource)
        } catch (e: Exception) {
            Log.e("PODCAST", e.toString())
        }
    }

    @JavascriptInterface
    fun playPodcast(seconds: String) {
        player?.playWhenReady = true
    }

    @JavascriptInterface
    fun pausePodcast() {
        player?.playWhenReady = false
    }

    @JavascriptInterface
    fun terminatePodcast() {
        player?.release()
        player = null
        playerHandler = null
    }

    @JavascriptInterface
    fun seekPodcast(seconds: Float) {
        player?.seekTo((seconds * 1000F).toLong())
    }

    @JavascriptInterface
    fun ratePodcast(rate: Float) {
        player?.setPlaybackParameters(PlaybackParameters(rate))
    }

    @JavascriptInterface
    fun mutePodcast(muted: Boolean) {
        if (muted) {
            player?.volume = 0F
        } else {
            player?.volume = 1F
        }
    }

    fun initPlayer() {
        player = SimpleExoPlayer.Builder(context).build()
        player?.audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_SPEECH)
            .build()

        // Creates a task that will update about every second. Has to use the player's thread
        // https://exoplayer.dev/hello-world.html#a-note-on-threading
        playerHandler = Handler(player?.applicationLooper)
        val timeUpdateTask = object: TimerTask() {
            override fun run() {
                playerHandler?.post(Runnable { podcastTimeUpdate() })
            }
        }
        timer.schedule(timeUpdateTask, 0, 1000)
    }

    fun podcastTimeUpdate() {
        if (player != null) {
            val time = player!!.currentPosition / 1000
            val duration = player!!.duration / 1000
            val message = mapOf("action" to "tick", "duration" to duration, "currentTime" to time)
            webViewClient?.sendPodcastMessage(message)
        }
    }
}
