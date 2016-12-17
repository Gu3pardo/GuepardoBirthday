package guepardoapps.guepardobirthdays.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import guepardoapps.guepardobirthday.common.Constants;
import guepardoapps.guepardobirthday.controller.*;
import guepardoapps.guepardobirthday.model.Birthday;
import guepardoapps.guepardobirthdays.R;

import guepardoapps.toolset.controller.SharedPrefController;
import guepardoapps.toolset.services.NavigationService;

public class ActivityBoot extends Activity {

	private Context _context;

	private DatabaseController _databaseController;
	private NavigationService _navigationService;
	private SharedPrefController _sharedPrefController;

	private SharedPreferences initialStart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.side_boot);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		_context = this;

		_databaseController = new DatabaseController(_context);
		_navigationService = new NavigationService(_context);
		_sharedPrefController = new SharedPrefController(_context, Constants.SHARED_PREF_NAME);

		initialStart = getSharedPreferences(Constants.SHARED_PREF_NAME, 0);
		boolean dataInstalled = initialStart.getBoolean(Constants.SHARED_PREF_NAME, false);
		if (!dataInstalled) {
			_databaseController.CreateBirthday(new Birthday(0, "Jonas Schubert", 2, 1, 1990));
			_sharedPrefController.SaveBooleanValue(Constants.SHARED_PREF_NAME, true);
		}

		_navigationService.NavigateTo(ActivityMain.class, true);
	}

	protected void onResume() {
		super.onResume();
		_navigationService.NavigateTo(ActivityMain.class, true);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}