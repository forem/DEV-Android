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
import to.dev.dev_android.BuildConfig
import to.dev.dev_android.R
import to.dev.dev_android.databinding.ActivityVideoPlayerBinding

class VideoPlayerActivity : BaseActivity<ActivityVideoPlayerBinding>() {

    override fun layout(): Int {
        return R.layout.activity_video_player
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoUrl = intent.getStringExtra(argVideoUrl)
        val videoTime = intent.getStringExtra(argVideoTime)

        val streamUri= Uri.parse(videoUrl)
        val dataSourceFactory: DataSource.Factory =
            DefaultHttpDataSourceFactory(BuildConfig.userAgent)
        val mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(streamUri)
        var player: SimpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        binding.playerView.player = player
        player.prepare(mediaSource)
        player.seekTo(videoTime.toLong() * 1000)
        player.playWhenReady = true
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
