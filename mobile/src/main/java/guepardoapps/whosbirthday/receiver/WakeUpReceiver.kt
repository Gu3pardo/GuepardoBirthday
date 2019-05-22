package guepardoapps.whosbirthday.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import guepardoapps.whosbirthday.controller.BirthdayController
import guepardoapps.whosbirthday.utils.Logger

@ExperimentalUnsignedTypes
class WakeUpReceiver : BroadcastReceiver() {
    private val tag: String = WakeUpReceiver::class.java.simpleName

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        Logger.instance.debug(tag, "onReceive")
        BirthdayController().checkForBirthday(context!!)
    }
}