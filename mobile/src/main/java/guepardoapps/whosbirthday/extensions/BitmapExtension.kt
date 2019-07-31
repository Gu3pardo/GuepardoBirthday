package guepardoapps.whosbirthday.extensions

import android.graphics.*

/**
 * @param height the height set for the bitmap
 * @param width the width set for the bitmap
 * @param newColor the new color set for the bitmap
 * @return returns a rounded bitmap
 */
fun Bitmap.circleBitmap(height: Int, width: Int, newColor: Int): Bitmap {
    val output: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val rect = Rect(0, 0, width, height)
    val paint = Paint().apply {
        isAntiAlias = true
        color = newColor
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    val canvas = Canvas(output)
    canvas.drawARGB(0, 0, 0, 0)
    canvas.drawOval(RectF(rect), paint)
    canvas.drawBitmap(this, rect, rect, paint)

    this.recycle()

    return output
}