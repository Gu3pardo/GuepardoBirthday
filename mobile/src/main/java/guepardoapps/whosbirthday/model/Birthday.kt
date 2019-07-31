package guepardoapps.whosbirthday.model

import guepardoapps.whosbirthday.extensions.integerFormat
import java.util.*

internal data class Birthday(val id: String, var name: String, var group: String, var day: Int, var month: Int, var year: Int, var remindMe: Boolean, var remindedMe: Boolean) {

    val age: Int =
            if (Calendar.getInstance().get(Calendar.MONTH) + 1 >= month && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) >= day) {
                Calendar.getInstance().get(Calendar.YEAR) - year
            } else {
                Calendar.getInstance().get(Calendar.YEAR) - year - 1
            }

    val dateText: String = "${day.integerFormat(2)}.${month.integerFormat(2)}.${year.integerFormat(4)}"

    val hasBirthday: Boolean = day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && month == Calendar.getInstance().get(Calendar.MONTH) + 1
}