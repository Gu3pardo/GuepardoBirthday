package guepardoapps.whosbirthday.model

import java.util.*

internal data class Birthday(val id: Long, var name: String, var group: String, var day: Int, var month: Int, var year: Int, var remindMe: Boolean, var remindedMe: Boolean) {
    fun getAge(): Int {
        if (Calendar.getInstance().get(Calendar.MONTH) + 1 >= month) {
            if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) >= day) {
                return Calendar.getInstance().get(Calendar.YEAR) - year
            }
        }
        return Calendar.getInstance().get(Calendar.YEAR) - year - 1
    }

    fun hasBirthday(): Boolean {
        return day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                && month == Calendar.getInstance().get(Calendar.MONTH) + 1
    }
}