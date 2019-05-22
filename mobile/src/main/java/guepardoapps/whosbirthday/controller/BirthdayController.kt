package guepardoapps.whosbirthday.controller

import android.content.Context
import android.content.Intent
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.activities.ActivityMain
import guepardoapps.whosbirthday.common.Constants
import guepardoapps.whosbirthday.database.birthday.DbBirthday
import guepardoapps.whosbirthday.extensions.common.integerFormat
import guepardoapps.whosbirthday.model.NotificationContent
import guepardoapps.whosbirthday.services.FloatingService

@ExperimentalUnsignedTypes
internal class BirthdayController : IBirthdayController {
    override fun checkForBirthday(context: Context) {
        DbBirthday(context).get().forEach { birthday ->
            if (birthday.hasBirthday()
                    && birthday.remindMe
                    && !birthday.remindedMe) {

                val notificationContent = NotificationContent(
                        3453 + birthday.id.toInt(),
                        "Who's Birthday",
                        "Birthday of ${birthday.name}. Is now ${birthday.getAge().integerFormat(2)} years old!",
                        R.mipmap.ic_launcher,
                        R.mipmap.ic_launcher,
                        ActivityMain::class.java,
                        false)

                NotificationController(context).create(notificationContent)

                // TODO show bubble starting service
                if ((SharedPreferenceController(context).load(Constants.bubbleState, false))) {
                    context.startService(Intent(context, FloatingService::class.java))
                }

                birthday.remindedMe = true
                DbBirthday(context).update(birthday)
            } else if (!birthday.hasBirthday()) {
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