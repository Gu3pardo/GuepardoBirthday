package guepardoapps.whosbirthday.extensions.common

fun Boolean.toInteger(): Int {
    if (this) {
        return 1
    }
    return 0
}