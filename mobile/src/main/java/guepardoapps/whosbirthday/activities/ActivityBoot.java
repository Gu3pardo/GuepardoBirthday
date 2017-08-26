package guepardoapps.whosbirthday.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import guepardoapps.whosbirthday.R;
import guepardoapps.whosbirthday.common.*;
import guepardoapps.whosbirthday.controller.*;
import guepardoapps.whosbirthday.model.BirthdayDto;
import guepardoapps.whosbirthday.tools.Logger;

public class ActivityBoot extends Activity {
    private static final String TAG = ActivityBoot.class.getSimpleName();
    private Logger _logger;

    private Context _context;
    private DatabaseController _databaseController;
    private NavigationController _navigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_boot);

        _logger = new Logger(TAG, Enables.LOGGING);
        _logger.Debug("onCreate");

        _context = this;

        _databaseController = DatabaseController.getInstance();
        _databaseController.Initialize(_context);

        _navigationController = new NavigationController(_context);
        SharedPrefController sharedPrefController = new SharedPrefController(_context, SharedPrefConstants.SHARED_PREF_NAME);

        SharedPreferences initialStart = getSharedPreferences(SharedPrefConstants.SHARED_PREF_NAME, 0);
        boolean dataInstalled = initialStart.getBoolean(SharedPrefConstants.SHARED_PREF_NAME, false);
        if (!dataInstalled) {
            _databaseController.CreateBirthday(new BirthdayDto(0, "Jonas Schubert", 2, 1, 1990));
            sharedPrefController.SaveBooleanValue(SharedPrefConstants.SHARED_PREF_NAME, true);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                _navigationController.NavigateTo(ActivityMain.class, true);
            }
        }, 1500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        _logger.Debug("onPause");
        _databaseController.Dispose();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _logger.Debug("onResume");
        _databaseController.Initialize(_context);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _logger.Debug("onDestroy");
        _databaseController.Dispose();
    }
}