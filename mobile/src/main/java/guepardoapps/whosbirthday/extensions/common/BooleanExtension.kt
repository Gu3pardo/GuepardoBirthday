package guepardoapps.whosbirthday.extensions.common

fun Boolean.toInteger(): Int =
        if (this) {
            1
        } else {
            0
        }