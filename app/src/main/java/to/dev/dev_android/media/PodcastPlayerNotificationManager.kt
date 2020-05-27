package to.dev.dev_android.media

import android.content.Context
import androidx.annotation.IntegerRes
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.NotificationUtil
import java.util.*

/*
 * This subclass of PlayerNotificationManager customizes the controls available in the
 * notification by overriding the getActions method.
 */
class PodcastPlayerNotificationManager(
    context: Context,
    channelId: String,
    notificationId: Int,
    mediaDescriptionAdapter: MediaDescriptionAdapter,
    playerNotificationManager: NotificationListener
): PlayerNotificationManager(
    context,
    channelId,
    notificationId,
    mediaDescriptionAdapter,
    playerNotificationManager) {

    companion object {
        fun createWithNotificationChannel(
            context: Context,
            channelId: String,
            channelName: Int,
            channelDescription: Int,
            notificationId: Int,
            mediaDescriptionAdapter: MediaDescriptionAdapter,
            playerNotificationManager: NotificationListener): PodcastPlayerNotificationManager {

            NotificationUtil.createNotificationChannel(
                context, channelId, channelName, channelDescription, NotificationUtil.IMPORTANCE_LOW
            )
            return PodcastPlayerNotificationManager(
                context, channelId, notificationId, mediaDescriptionAdapter, playerNotificationManager
            )
        }
    }

    override fun getActions(player: Player): List<String> {
        var stringActions: List<String> = ArrayList()
        stringActions += ACTION_REWIND
        stringActions += if (shouldShowPauseButton(player)) {
            ACTION_PAUSE
        } else {
            ACTION_PLAY
        }
        stringActions += ACTION_FAST_FORWARD
        return stringActions
    }

    private fun shouldShowPauseButton(player: Player): Boolean {
        val state = player.playbackState
        return state != Player.STATE_ENDED && state != Player.STATE_IDLE && player.playWhenReady
    }
}