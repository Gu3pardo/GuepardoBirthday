package guepardoapps.whosbirthday.extensions.common

import java.util.*

/**
 * @param digits the numbers to show
 * @return returns a string with specified format and additional zeros
 */
fun Int.integerFormat(digits: Int): String = String.format(Locale.getDefault(), "%0${digits}d", this)

/**
 * @return returns a boolean, true if int is not 0
 */
fun Int.toBoolean(): Boolean = this != 0