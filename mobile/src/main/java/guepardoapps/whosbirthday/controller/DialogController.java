package guepardoapps.whosbirthday.controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import guepardoapps.whosbirthday.R;
import guepardoapps.whosbirthday.runnable.EditDialogRunnable;
import guepardoapps.whosbirthday.tools.Logger;

public class DialogController {
    private static final String TAG = DialogController.class.getSimpleName();
    private Logger _logger;

    private int _textColor;
    private int _backgroundColor;

    private Context _context;
    private Dialog _dialog;

    public Runnable CloseDialogCallback = new Runnable() {
        @Override
        public void run() {
            if (_dialog != null) {
                _dialog.dismiss();
                _dialog = null;
            }
        }
    };

    public DialogController(Context context, int textColor, int backgroundColor) {
        _logger = new Logger(TAG);
        _logger.Debug("Created new " + TAG + "...");

        _context = context;

        _textColor = textColor;
        _backgroundColor = backgroundColor;

        _logger.Debug("_textColor: " + String.valueOf(_textColor));
        _logger.Debug("_backgroundColor: " + String.valueOf(_backgroundColor));
    }

    public void ShowDialogSingle(
            @NonNull String title,
            @NonNull String prompt,
            @NonNull String ok,
            @NonNull final Runnable okCallback,
            boolean isCancelable) {
        _dialog = new Dialog(_context);

        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setContentView(R.layout.custom_dialog_1button);

        LinearLayout backgroundView = _dialog.findViewById(R.id.custom_alert_dialog_background);
        backgroundView.setBackgroundColor(_backgroundColor);

        TextView titleView = _dialog.findViewById(R.id.custom_alert_dialog_title);
        titleView.setText(title);
        titleView.setTextColor(_textColor);

        TextView promptView = _dialog.findViewById(R.id.custom_alert_dialog_prompt);
        promptView.setText(prompt);
        promptView.setTextColor(_textColor);

        Button btnOk = _dialog.findViewById(R.id.custom_alert_dialog_btn_ok);
        btnOk.setText(ok);
        btnOk.setTextColor(_textColor);
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                okCallback.run();
            }
        });

        _dialog.setCancelable(isCancelable);
        _dialog.show();
    }

    public void ShowDialogDouble(
            @NonNull String title,
            @NonNull String prompt,
            @NonNull String ok,
            @NonNull final Runnable okCallback,
            @NonNull String cancel,
            @NonNull final Runnable cancelCallback,
            boolean isCancelable) {
        _dialog = new Dialog(_context);

        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setContentView(R.layout.custom_dialog_2buttons);

        LinearLayout backgroundView = _dialog.findViewById(R.id.custom_alert_dialog_background);
        backgroundView.setBackgroundColor(_backgroundColor);

        TextView titleView = _dialog.findViewById(R.id.custom_alert_dialog_title);
        titleView.setText(title);
        titleView.setTextColor(_textColor);

        TextView promptView = _dialog.findViewById(R.id.custom_alert_dialog_prompt);
        promptView.setText(prompt);
        promptView.setTextColor(_textColor);

        Button btnOk = _dialog.findViewById(R.id.custom_alert_dialog_btn_ok);
        btnOk.setText(ok);
        btnOk.setTextColor(_textColor);
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                okCallback.run();
            }
        });

        Button btnCancel = _dialog.findViewById(R.id.custom_alert_dialog_btn_cancel);
        btnCancel.setText(cancel);
        btnCancel.setTextColor(_textColor);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelCallback.run();
            }
        });

        _dialog.setCancelable(isCancelable);
        _dialog.show();
    }

    public void ShowDialogTriple(
            @NonNull String title,
            @NonNull String prompt,
            @NonNull String ok,
            @NonNull final Runnable okRunnable,
            @NonNull String neutral,
            @NonNull final Runnable neutralRunnable,
            @NonNull String cancel,
            @NonNull final Runnable cancelRunnable,
            boolean isCancelable) {
        _dialog = new Dialog(_context);

        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setContentView(R.layout.custom_dialog_3buttons);

        LinearLayout backgroundView = _dialog.findViewById(R.id.custom_alert_dialog_background);
        backgroundView.setBackgroundColor(_backgroundColor);

        TextView titleView = _dialog.findViewById(R.id.custom_alert_dialog_title);
        titleView.setText(title);
        titleView.setTextColor(_textColor);

        TextView promptView = _dialog.findViewById(R.id.custom_alert_dialog_prompt);
        promptView.setText(prompt);
        promptView.setTextColor(_textColor);

        Button btnOk = _dialog.findViewById(R.id.custom_alert_dialog_btn_ok);
        btnOk.setText(ok);
        btnOk.setTextColor(_textColor);
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                okRunnable.run();
            }
        });

        Button btnNeutral = _dialog.findViewById(R.id.custom_alert_dialog_btn_neutral);
        btnNeutral.setText(neutral);
        btnNeutral.setTextColor(_textColor);
        btnNeutral.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                neutralRunnable.run();
            }
        });

        Button btnCancel = _dialog.findViewById(R.id.custom_alert_dialog_btn_cancel);
        btnCancel.setText(cancel);
        btnCancel.setTextColor(_textColor);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRunnable.run();
            }
        });

        _dialog.setCancelable(isCancelable);
        _dialog.show();
    }

    public void ShowDialogEditText(
            @NonNull String title,
            @NonNull String prompt,
            @NonNull String ok,
            @NonNull final EditDialogRunnable okCallback,
            boolean isCancelable) {
        _dialog = new Dialog(_context);

        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setContentView(R.layout.custom_dialog_edittext);

        LinearLayout backgroundView = _dialog.findViewById(R.id.custom_alert_dialog_background);
        backgroundView.setBackgroundColor(_backgroundColor);

        TextView titleView = _dialog.findViewById(R.id.custom_alert_dialog_title);
        titleView.setText(title);
        titleView.setTextColor(_textColor);

        TextView promptView = _dialog.findViewById(R.id.custom_alert_dialog_prompt);
        promptView.setText(prompt);
        promptView.setTextColor(_textColor);

        final EditText inputEditText = _dialog.findViewById(R.id.custom_alert_dialog_edittext);
        inputEditText.setTextColor(_textColor);

        Button btnOk = _dialog.findViewById(R.id.custom_alert_dialog_btn_ok);
        btnOk.setText(ok);
        btnOk.setTextColor(_textColor);
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputEditText.getText().toString();

                if (input.length() == 0) {
                    _logger.Error("Input has invalid length 0!");
                    return;
                }

                String[] data = okCallback.GetData();
                if (data.length > 0) {
                    data[0] = input;
                } else {
                    data = new String[]{input};
                }
                okCallback.SetData(data);
                okCallback.run();
            }
        });

        _dialog.setCancelable(isCancelable);
        _dialog.show();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void ShowDialogWebview(
            @NonNull String title,
            @NonNull String prompt,
            @NonNull String url,
            @NonNull String ok,
            @NonNull final Runnable okCallback,
            boolean isCancelable) {
        _dialog = new Dialog(_context);

        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setContentView(R.layout.custom_dialog_webview);

        LinearLayout backgroundView = _dialog.findViewById(R.id.custom_webview_dialog_background);
        backgroundView.setBackgroundColor(_backgroundColor);

        TextView titleView = _dialog.findViewById(R.id.custom_webview_dialog_title);
        titleView.setText(title);
        titleView.setTextColor(_textColor);

        TextView promptView = _dialog.findViewById(R.id.custom_webview_dialog_prompt);
        promptView.setText(prompt);
        promptView.setTextColor(_textColor);

        Button btnOk = _dialog.findViewById(R.id.custom_webview_dialog_btn_ok);
        btnOk.setText(ok);
        btnOk.setTextColor(_textColor);
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                okCallback.run();
            }
        });

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            _logger.Warn("Fixing url!");
            url = "http://" + url;
        }
        _logger.Debug("Url is: " + url);

        WebView webview = _dialog.findViewById(R.id.custom_webview_dialog_webview);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.setWebViewClient(new WebViewClient());
        webview.setInitialScale(100);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(false);
        webview.loadUrl(url);

        _dialog.setCancelable(isCancelable);
        _dialog.show();
    }

    public void ShowAlertDialogWebView(
            @NonNull String title,
            @NonNull String url) {
        AlertDialog.Builder alert = new AlertDialog.Builder(_context);
        alert.setTitle(title);

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            _logger.Warn("Fixing url!");
            url = "http://" + url;
        }
        _logger.Debug("Url is: " + url);

        WebView webView = new WebView(_context);
        webView.setInitialScale(100);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        alert.setView(webView);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        alert.show();
    }
}