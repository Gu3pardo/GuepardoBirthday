package guepardoapps.whosbirthday.model

internal data class NotificationContent(
        val id: String,
        val title: String,
        val text: String,
        val iconId: Int,
        val largeIconId: Int,
        val receiver: Class<*>,
        val cancelable: Boolean)