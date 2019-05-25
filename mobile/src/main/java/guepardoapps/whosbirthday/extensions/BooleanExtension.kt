package guepardoapps.whosbirthday.extensions

fun Boolean.toInteger(): Int =
        if (this) {
            1
        } else {
            0
        }