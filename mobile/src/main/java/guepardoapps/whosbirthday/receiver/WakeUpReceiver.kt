package guepardoapps.whosbirthday.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.activities.ActivityMain
import guepardoapps.whosbirthday.controller.NotificationController
import guepardoapps.whosbirthday.database.birthday.DbBirthday
import guepardoapps.whosbirthday.extensions.common.integerFormat
import guepardoapps.whosbirthday.model.NotificationContent
import guepardoapps.whosbirthday.utils.Logger

class WakeUpReceiver : BroadcastReceiver() {
    private val tag: String = WakeUpReceiver::class.java.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        Logger.instance.debug(tag, "onReceive")

        val birthdayList = DbBirthday(context!!).get()

        birthdayList.forEach { birthday ->
            if (birthday.hasBirthday()) {
                val notificationContent = NotificationContent(
                        birthday.id.toInt(),
                        "Who's Birthday",
                        "Birthday of ${birthday.name}. Is now ${birthday.getAge().integerFormat(2)} years old!",
                        R.mipmap.ic_launcher,
                        R.mipmap.ic_launcher,
                        ActivityMain::class.java,
                        false)
                NotificationController(context).create(notificationContent)
            }
        }
    }
}