package to.dev.dev_android.activities

import androidx.multidex.MultiDexApplication
import org.greenrobot.eventbus.EventBus
import to.dev.dev_android.webclients.EventBusClientIndex


class StarterApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        EventBus.builder().addIndex(EventBusClientIndex()).build()
        EventBus.builder().addIndex(EventBusClientIndex()).installDefaultEventBus()
    }

}