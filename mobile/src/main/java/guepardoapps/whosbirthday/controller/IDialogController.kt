package guepardoapps.whosbirthday.controller

import android.app.AlertDialog
import com.rey.material.app.Dialog
import guepardoapps.whosbirthday.runnable.EditDialogRunnable

interface IDialogController {
    fun setColors(textColor: Int, backgroundColor: Int)

    fun singleButton(title: String, prompt: String,
                     btn1: String, btn1Callback: () -> Unit,
                     isCancelable: Boolean): Dialog

    fun doubleButton(title: String, prompt: String,
                     btn1: String, btn1Callback: () -> Unit,
                     btn2: String, btn2Callback: () -> Unit,
                     isCancelable: Boolean): Dialog

    fun tripleButton(title: String, prompt: String,
                     btn1: String, btn1Callback: () -> Unit,
                     btn2: String, btn2Callback: () -> Unit,
                     btn3: String, btn3Callback: () -> Unit,
                     isCancelable: Boolean): Dialog

    fun editText(title: String, prompt: String,
                 btn1: String, btn1Callback: EditDialogRunnable,
                 isCancelable: Boolean): Dialog

    fun webView(title: String, prompt: String,
                url: String,
                btn1: String, btn1Callback: () -> Unit,
                isCancelable: Boolean): Dialog

    fun alertWebView(title: String, url: String): AlertDialog.Builder
}