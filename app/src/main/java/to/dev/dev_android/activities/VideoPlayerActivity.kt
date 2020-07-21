package to.dev.dev_android.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.MainThread
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import org.greenrobot.eventbus.EventBus
import to.dev.dev_android.BuildConfig
import to.dev.dev_android.R
import to.dev.dev_android.databinding.ActivityVideoPlayerBinding
import to.dev.dev_android.events.VideoPlayerPauseEvent
import to.dev.dev_android.events.VideoPlayerTickEvent
import to.dev.dev_android.webclients.CustomWebViewClient
import java.util.*

class VideoPlayerActivity : BaseActivity<ActivityVideoPlayerBinding>() {

    private var player: SimpleExoPlayer? = null
    private val timer = Timer()

    override fun layout(): Int {
        return R.layout.activity_video_player
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoUrl = intent.getStringExtra(argVideoUrl)
        val videoTime = intent.getStringExtra(argVideoTime)

        val streamUri= Uri.parse(videoUrl)
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSourceFactory(BuildConfig.userAgent)
        val mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(streamUri)

        player = SimpleExoPlayer.Builder(this).build()
        binding.playerView.player = player
        player?.prepare(mediaSource)
        player?.seekTo(videoTime.toLong() * 1000)
        player?.playWhenReady = true

        val timeUpdateTask = object: TimerTask() {
            override fun run() {
                timeUpdate()
            }
        }
        timer.schedule(timeUpdateTask, 0, 1000)
    }

    override fun onDestroy() {
        player?.playWhenReady = false
        timer.cancel()
        EventBus.getDefault().post(VideoPlayerPauseEvent())
        super.onDestroy()
    }

    fun timeUpdate() {
        val milliseconds = (player?.currentPosition ?: 0)
        val currentTime = (milliseconds / 1000).toString()
        EventBus.getDefault().post(VideoPlayerTickEvent(currentTime))
    }

    companion object {
        @MainThread
        fun newIntent(
            context: Context,
            url: String,
            time: String
        ) = Intent(context, VideoPlayerActivity::class.java).apply {
            putExtra(argVideoUrl, url)
            putExtra(argVideoTime, time)
        }

        const val argVideoUrl = "ARG_VIDEO_URL"
        const val argVideoTime = "ARG_VIDEO_TIME"
    }
}
