package guepardoapps.whosbirthday.extensions.common

fun Char.div(divider: Int): Char {
    return (this.toInt() / divider).toChar()
}