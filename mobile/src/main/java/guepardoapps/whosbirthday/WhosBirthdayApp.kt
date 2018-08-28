package guepardoapps.whosbirthday

import android.app.Application
import guepardoapps.whosbirthday.utils.Logger

class WhosBirthdayApp : Application() {
    private val tag: String = WhosBirthdayApp::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        Logger.instance.initialize(this)
        Logger.instance.debug(tag, "onCreate")
    }
}