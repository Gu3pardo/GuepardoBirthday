package guepardoapps.guepardobirthday.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import guepardoapps.guepardobirthday.R;
import guepardoapps.guepardobirthday.common.*;
import guepardoapps.guepardobirthday.controller.*;
import guepardoapps.guepardobirthday.model.Birthday;

import guepardoapps.library.toolset.common.Logger;
import guepardoapps.library.toolset.controller.NavigationController;
import guepardoapps.library.toolset.controller.SharedPrefController;

public class ActivityBoot extends Activity {

	private static final String TAG = ActivityBoot.class.getSimpleName();
	private Logger _logger;

	private Context _context;

	private DatabaseController _databaseController;
	private NavigationController _navigationController;
	private SharedPrefController _sharedPrefController;

	private SharedPreferences initialStart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.side_boot);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Colors.ACTION_BAR_COLOR));

		_logger = new Logger(TAG, Enables.DEBUGGING_ENABLED);
		_logger.Debug("onCreate");

		_context = this;

		_databaseController = DatabaseController.getInstance();
		_databaseController.Initialize(_context);

		_navigationController = new NavigationController(_context);
		_sharedPrefController = new SharedPrefController(_context, SharedPrefConstants.SHARED_PREF_NAME);

		initialStart = getSharedPreferences(SharedPrefConstants.SHARED_PREF_NAME, 0);
		boolean dataInstalled = initialStart.getBoolean(SharedPrefConstants.SHARED_PREF_NAME, false);
		if (!dataInstalled) {
			_databaseController.CreateBirthday(new Birthday(0, "Jonas Schubert", 2, 1, 1990));
			_sharedPrefController.SaveBooleanValue(SharedPrefConstants.SHARED_PREF_NAME, true);
		}

		_navigationController.NavigateTo(ActivityMain.class, true);
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
		_navigationController.NavigateTo(ActivityMain.class, true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		_logger.Debug("onDestroy");
		_databaseController.Dispose();
	}
}