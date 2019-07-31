package guepardoapps.whosbirthday.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.activities.ActivityMain
import guepardoapps.whosbirthday.controller.SharedPreferenceController
import guepardoapps.whosbirthday.controller.SystemInfoController
import guepardoapps.whosbirthday.utils.Logger

// https://stackoverflow.com/questions/7569937/unable-to-add-window-android-view-viewrootw44da9bc0-permission-denied-for-t#answer-34061521

@ExperimentalUnsignedTypes
class FloatingService : Service() {

    private val windowManager: WindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private val bubbleView: ImageView = ImageView(this)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        initBubbleView()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            windowManager.removeView(bubbleView)
        } catch (exception: Exception) {
            Logger.instance.error(FloatingService::class.java.simpleName, exception)
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("ClickableViewAccessibility")
    private fun initBubbleView() {
        val sharedPreferenceController = SharedPreferenceController(this)
        val systemInfoController = SystemInfoController(this)

        var bubbleMoved = false
        var bubbleParamsStore: WindowManager.LayoutParams? = null

        val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT).apply {
            gravity = Gravity.TOP or Gravity.START
            x = sharedPreferenceController.load(getString(R.string.sharedPrefBubblePosX), resources.getInteger(R.integer.sharedPrefBubbleDefaultPosX))
            y = sharedPreferenceController.load(getString(R.string.sharedPrefBubblePosY), resources.getInteger(R.integer.sharedPrefBubbleDefaultPosY))
        }

        if (systemInfoController.currentAndroidApi() >= Build.VERSION_CODES.O) {
            @RequiresApi(Build.VERSION_CODES.O)
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }

        val backgroundShape = GradientDrawable()
        backgroundShape.setColor(resources.getColor(R.color.colorPrimaryDark))
        backgroundShape.cornerRadius = 100.0f

        bubbleView.background = backgroundShape
        bubbleView.setImageResource(R.mipmap.ic_launcher)
        bubbleView.setOnTouchListener(object : View.OnTouchListener {
            private var initialX: Int = 0
            private var initialY: Int = 0
            private var initialTouchX: Float = 0.toFloat()
            private var initialTouchY: Float = 0.toFloat()

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (params.x < 0) {
                            params.x = 0
                            bubbleParamsStore = params
                            windowManager.updateViewLayout(bubbleView, bubbleParamsStore)
                        }

                        initialX = params.x
                        initialY = params.y

                        initialTouchX = event.rawX
                        initialTouchY = event.rawY

                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        view.performClick()
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = initialX + (event.rawX - initialTouchX).toInt()
                        params.y = initialY + (event.rawY - initialTouchY).toInt()

                        bubbleMoved = initialX - params.x > resources.getInteger(R.integer.bubbleMinMove)
                                || initialY - params.y > resources.getInteger(R.integer.bubbleMinMove)
                                || params.x - initialX > resources.getInteger(R.integer.bubbleMinMove)
                                || params.y - initialY > resources.getInteger(R.integer.bubbleMinMove)

                        bubbleParamsStore = params
                        windowManager.updateViewLayout(bubbleView, bubbleParamsStore)

                        return true
                    }
                }

                return false
            }
        })

        bubbleView.setOnClickListener {
            if (bubbleMoved) {
                bubbleMoved = false

                params.x = if (params.x > systemInfoController.displayDimension().width / 2) systemInfoController.displayDimension().width else 0

                sharedPreferenceController.save(getString(R.string.sharedPrefBubblePosX), params.x)
                sharedPreferenceController.save(getString(R.string.sharedPrefBubblePosY), params.y)

                bubbleParamsStore = params
                windowManager.updateViewLayout(bubbleView, bubbleParamsStore)
            } else {
                val intent = Intent(this, ActivityMain::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

                // TODO Add new layout or perform an action
                /*
                stopwatchView = View.inflate(applicationContext, R.layout.side_main, null)

                val clockView: ClockView = stopwatchView!!.findViewById(R.id.clockView)
                clockView.setCloseCallback {
                    subscription?.dispose()
                    subscription = null

                    windowManager.removeView(stopwatchView)
                    stopwatchView = null
                }

                subscription = ClockService.instance.timePublishSubject
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                { response -> clockView.updateViews(response) },
                                { error -> Logger.instance.error(tag, error) })

                val layoutParams = WindowManager.LayoutParams()

                layoutParams.gravity = Gravity.CENTER
                if (systemInfoController.currentAndroidApi() >= Build.VERSION_CODES.O) {
                    @RequiresApi(Build.VERSION_CODES.O)
                    layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                }
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                layoutParams.alpha = 1.0f
                layoutParams.packageName = packageName
                layoutParams.buttonBrightness = 1f
                layoutParams.windowAnimations = android.R.style.Animation_Dialog

                windowManager.addView(stopwatchView, layoutParams)
                */
            }
        }

        windowManager.addView(bubbleView, bubbleParamsStore)
    }
}