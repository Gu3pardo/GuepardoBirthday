package guepardoapps.whosbirthday.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import guepardoapps.whosbirthday.controller.BirthdayController
import guepardoapps.whosbirthday.utils.Logger

class WakeUpReceiver : BroadcastReceiver() {
    private val tag: String = WakeUpReceiver::class.java.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        Logger.instance.debug(tag, "onReceive")
        BirthdayController().checkForBirthday(context!!)
    }
}