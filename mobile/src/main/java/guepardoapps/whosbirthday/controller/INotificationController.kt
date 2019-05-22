package guepardoapps.whosbirthday.controller

import androidx.annotation.NonNull
import guepardoapps.whosbirthday.model.NotificationContent

internal interface INotificationController {
    fun create(@NonNull notificationContent: NotificationContent)

    fun close(id: Int)
}