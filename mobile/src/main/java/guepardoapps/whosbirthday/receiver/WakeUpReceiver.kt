package guepardoapps.whosbirthday.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import guepardoapps.whosbirthday.controller.BirthdayController
import guepardoapps.whosbirthday.utils.Logger

@ExperimentalUnsignedTypes
class WakeUpReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        Logger.instance.debug(WakeUpReceiver::class.java.simpleName, "onReceive")
        BirthdayController().checkForBirthday(context!!)
    }
}