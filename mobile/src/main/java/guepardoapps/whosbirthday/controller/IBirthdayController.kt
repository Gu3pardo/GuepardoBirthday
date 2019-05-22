package guepardoapps.whosbirthday.controller

import android.content.Context

internal interface IBirthdayController {
    fun checkForBirthday(context: Context)

    fun closeFloating(context: Context)
}