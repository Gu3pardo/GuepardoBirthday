package guepardoapps.whosbirthday.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import guepardoapps.whosbirthday.R;
import guepardoapps.whosbirthday.common.*;
import guepardoapps.whosbirthday.controller.MailController;
import guepardoapps.whosbirthday.tools.Logger;

public class ActivityImpressum extends Activity {
    private static final String TAG = ActivityImpressum.class.getSimpleName();
    private Logger _logger;

    private MailController _mailController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_impressum);

        _logger = new Logger(TAG, Enables.LOGGING);
        _logger.Debug("onCreate");

        _mailController = new MailController(this);
    }

    public void SendMail(View view) {
        _logger.Debug("SendMail");
        _mailController.SendMail("guepardoapps@gmail.com", true);
    }

    public void GoToGitHub(View view) {
        _logger.Debug("GoToGitHub");
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/GuepardoApps/WhosBirthday/")));
    }
}