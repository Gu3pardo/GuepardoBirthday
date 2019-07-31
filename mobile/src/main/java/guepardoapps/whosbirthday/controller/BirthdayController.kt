package guepardoapps.whosbirthday.controller

import android.content.Context
import android.content.Intent
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.activities.ActivityMain
import guepardoapps.whosbirthday.database.birthday.DbBirthday
import guepardoapps.whosbirthday.model.NotificationContent
import guepardoapps.whosbirthday.services.FloatingService

@ExperimentalUnsignedTypes
internal class BirthdayController : IBirthdayController {
    override fun checkForBirthday(context: Context) {
        DbBirthday(context).get().forEach { birthday ->
            if (birthday.hasBirthday && birthday.remindMe && !birthday.remindedMe) {

                val notificationContent = NotificationContent(
                        id = birthday.id,
                        title = context.getString(R.string.notificationTitle),
                        text = String.format(context.getString(R.string.notificationContent), birthday.name, birthday.age),
                        iconId = R.mipmap.ic_launcher,
                        largeIconId = R.mipmap.ic_launcher,
                        receiver = ActivityMain::class.java,
                        cancelable = false)

                NotificationController(context).create(notificationContent)

                // TODO show bubble starting service
                if ((SharedPreferenceController(context).load(context.getString(R.string.sharedPrefBubbleState), false))) {
                    context.startService(Intent(context, FloatingService::class.java))
                }

                birthday.remindedMe = true
                DbBirthday(context).update(birthday)
            } else if (!birthday.hasBirthday) {
                birthday.remindedMe = false
                DbBirthday(context).update(birthday)
            }
        }
    }

    override fun closeFloating(context: Context) {
        if (SystemInfoController(context).isServiceRunning(FloatingService::class.java)) {
            context.stopService(Intent(context, FloatingService::class.java))
        }
    }
}