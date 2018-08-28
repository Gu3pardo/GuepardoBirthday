package guepardoapps.whosbirthday.controller

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.Window
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.rey.material.app.Dialog
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.R.string.ok
import guepardoapps.whosbirthday.runnable.EditDialogRunnable
import guepardoapps.whosbirthday.utils.Logger

class DialogController(private val context: Context) : IDialogController {
    private val tag: String = DialogController::class.java.simpleName

    private var textColor: Int = 0xFFFFFF
    private var backgroundColor: Int = 0x000000

    override fun setColors(textColor: Int, backgroundColor: Int) {
        this.textColor = textColor
        this.backgroundColor = backgroundColor
    }

    override fun singleButton(title: String, prompt: String,
                              btn1: String, btn1Callback: () -> Unit,
                              isCancelable: Boolean): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_1button)

        val backgroundView: LinearLayout = dialog.findViewById(R.id.custom_alert_dialog_background)
        backgroundView.setBackgroundColor(backgroundColor)

        val titleView: TextView = dialog.findViewById(R.id.custom_alert_dialog_title)
        titleView.text = title
        titleView.setTextColor(textColor)

        val promptView: TextView = dialog.findViewById(R.id.custom_alert_dialog_prompt)
        promptView.text = prompt
        promptView.setTextColor(textColor)

        val btnOk: Button = dialog.findViewById(R.id.custom_alert_dialog_btn_ok)
        btnOk.text = btn1
        btnOk.setTextColor(textColor)
        btnOk.setOnClickListener { btn1Callback.invoke() }

        dialog.setCancelable(isCancelable)
        dialog.show()

        return dialog
    }

    override fun doubleButton(title: String, prompt: String,
                              btn1: String, btn1Callback: () -> Unit,
                              btn2: String, btn2Callback: () -> Unit,
                              isCancelable: Boolean): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_2buttons)

        val backgroundView: LinearLayout = dialog.findViewById(R.id.custom_alert_dialog_background)
        backgroundView.setBackgroundColor(backgroundColor)

        val titleView: TextView = dialog.findViewById(R.id.custom_alert_dialog_title)
        titleView.text = title
        titleView.setTextColor(textColor)

        val promptView: TextView = dialog.findViewById(R.id.custom_alert_dialog_prompt)
        promptView.text = prompt
        promptView.setTextColor(textColor)

        val btnOk: Button = dialog.findViewById(R.id.custom_alert_dialog_btn_ok)
        btnOk.text = btn1
        btnOk.setTextColor(textColor)
        btnOk.setOnClickListener { btn1Callback.invoke() }

        val btnCancel: Button = dialog.findViewById(R.id.custom_alert_dialog_btn_cancel)
        btnCancel.text = btn2
        btnCancel.setTextColor(textColor)
        btnCancel.setOnClickListener { btn2Callback.invoke() }

        dialog.setCancelable(isCancelable)
        dialog.show()

        return dialog
    }

    override fun tripleButton(title: String, prompt: String,
                              btn1: String, btn1Callback: () -> Unit,
                              btn2: String, btn2Callback: () -> Unit,
                              btn3: String, btn3Callback: () -> Unit,
                              isCancelable: Boolean): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_3buttons)

        val backgroundView: LinearLayout = dialog.findViewById(R.id.custom_alert_dialog_background)
        backgroundView.setBackgroundColor(backgroundColor)

        val titleView: TextView = dialog.findViewById(R.id.custom_alert_dialog_title)
        titleView.text = title
        titleView.setTextColor(textColor)

        val promptView: TextView = dialog.findViewById(R.id.custom_alert_dialog_prompt)
        promptView.text = prompt
        promptView.setTextColor(textColor)

        val btnOk: Button = dialog.findViewById(R.id.custom_alert_dialog_btn_ok)
        btnOk.text = btn1
        btnOk.setTextColor(textColor)
        btnOk.setOnClickListener { btn1Callback.invoke() }

        val btnNeutral: Button = dialog.findViewById(R.id.custom_alert_dialog_btn_neutral)
        btnNeutral.text = btn2
        btnNeutral.setTextColor(textColor)
        btnNeutral.setOnClickListener { btn2Callback.invoke() }

        val btnCancel: Button = dialog.findViewById(R.id.custom_alert_dialog_btn_cancel)
        btnCancel.text = btn3
        btnCancel.setTextColor(textColor)
        btnCancel.setOnClickListener { btn3Callback.invoke() }

        dialog.setCancelable(isCancelable)
        dialog.show()

        return dialog
    }

    override fun editText(title: String, prompt: String,
                          btn1: String, btn1Callback: EditDialogRunnable,
                          isCancelable: Boolean): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_edittext)

        val backgroundView: LinearLayout = dialog.findViewById(R.id.custom_alert_dialog_background)
        backgroundView.setBackgroundColor(backgroundColor)

        val titleView: TextView = dialog.findViewById(R.id.custom_alert_dialog_title)
        titleView.text = title
        titleView.setTextColor(textColor)

        val promptView: TextView = dialog.findViewById(R.id.custom_alert_dialog_prompt)
        promptView.text = prompt
        promptView.setTextColor(textColor)

        val inputEditText: EditText = dialog.findViewById(R.id.custom_alert_dialog_edittext)
        inputEditText.setTextColor(textColor)

        val btnOk: Button = dialog.findViewById(R.id.custom_alert_dialog_btn_ok)
        btnOk.setText(ok)
        btnOk.setTextColor(textColor)
        btnOk.setOnClickListener {
            val input = inputEditText.text.toString()
            if (input.isEmpty()) {
                Logger.instance.error(tag, "Input has invalid length 0!")
                return@setOnClickListener
            }

            var data = btn1Callback.data
            if (data.isNotEmpty()) {
                data[0] = input
            } else {
                data = arrayOf(input)
            }
            btn1Callback.data = data
            btn1Callback.run()
        }

        dialog.setCancelable(isCancelable)
        dialog.show()

        return dialog
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun webView(title: String, prompt: String, url: String,
                         btn1: String, btn1Callback: () -> Unit,
                         isCancelable: Boolean): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_webview)

        val backgroundView: LinearLayout = dialog.findViewById(R.id.custom_webview_dialog_background)
        backgroundView.setBackgroundColor(backgroundColor)

        val titleView: TextView = dialog.findViewById(R.id.custom_webview_dialog_title)
        titleView.text = title
        titleView.setTextColor(textColor)

        val promptView: TextView = dialog.findViewById(R.id.custom_webview_dialog_prompt)
        promptView.text = prompt
        promptView.setTextColor(textColor)

        val btnOk: Button = dialog.findViewById(R.id.custom_webview_dialog_btn_ok)
        btnOk.text = btn1
        btnOk.setTextColor(textColor)
        btnOk.setOnClickListener { btn1Callback.invoke() }

        var usedUrl = url
        if (!usedUrl.startsWith("http://") && !usedUrl.startsWith("https://")) {
            Logger.instance.warning(tag, "Fixing url!")
            usedUrl = "http://$usedUrl"
        }
        Logger.instance.debug(tag, "UsedUrl is: $usedUrl")

        val webView: WebView = dialog.findViewById(R.id.custom_webview_dialog_webview)
        webView.settings.useWideViewPort = true
        webView.settings.builtInZoomControls = true
        webView.settings.setSupportZoom(true)
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.webViewClient = WebViewClient()
        webView.setInitialScale(100)
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(false)
        webView.loadUrl(url)

        dialog.setCancelable(isCancelable)
        dialog.show()

        return dialog
    }

    @Suppress("OverridingDeprecatedMember")
    override fun alertWebView(title: String, url: String): AlertDialog.Builder {
        val alert = AlertDialog.Builder(context)
        alert.setTitle(title)

        var usedUrl = url
        if (!usedUrl.startsWith("http://") && !usedUrl.startsWith("https://")) {
            Logger.instance.warning(tag, "Fixing url!")
            usedUrl = "http://$usedUrl"
        }
        Logger.instance.debug(tag, "UsedUrl is: $usedUrl")

        val webView = WebView(context)
        webView.setInitialScale(100)
        webView.loadUrl(usedUrl)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }

        alert.setView(webView)
        alert.setNegativeButton("Close") { dialog, _ -> dialog.dismiss() }
        alert.show()

        return alert
    }
}