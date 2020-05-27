package to.dev.dev_android.util.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import to.dev.dev_android.events.NetworkStatusEvent

class NetworkWatcher(val coroutineScope: CoroutineScope) : BroadcastReceiver() {
    companion object {
        val intentFilter = IntentFilter().apply {
            addAction("android.net.conn.CONNECTIVITY_CHANGE")
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        coroutineScope.launch {
            if (async { NetworkUtils.isOnline() }.await()) {
                EventBus.getDefault().post(NetworkStatusEvent(NetworkStatus.BACK_ONLINE))
            }
        }
    }
}