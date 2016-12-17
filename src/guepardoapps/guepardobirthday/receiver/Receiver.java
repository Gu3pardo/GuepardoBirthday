package guepardoapps.guepardobirthday.receiver;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import guepardoapps.guepardobirthday.common.Constants;
import guepardoapps.guepardobirthday.controller.DatabaseController;
import guepardoapps.guepardobirthday.model.Birthday;
import guepardoapps.guepardobirthday.services.NotificationDisplayService;

public class Receiver extends BroadcastReceiver {

	private Context _context;
	private ArrayList<Birthday> _birthdays;

	private DatabaseController _databaseController;

	private String _notificationContent = "";

	@Override
	public void onReceive(Context context, Intent intent) {

		if (Constants.DEBUGGING_ENABLED) {
			Log.d("Receiver", "onReceive");
		}

		_context = context;
		_databaseController = new DatabaseController(_context);

		_birthdays = _databaseController.GetBirthdays();

		int todayBirthdayCount = 0;
		for (Birthday birthday : _birthdays) {
			if (birthday.HasBirthday()) {
				todayBirthdayCount++;
				if (todayBirthdayCount > 1) {
					_notificationContent = String.format("%s%d%s", "Today are ", todayBirthdayCount, " birthdays!");
				} else {
					_notificationContent = "Birthday of: " + birthday.GetName() + " is now " + birthday.GetAge()
							+ " years old!";
				}
			}
		}

		if (todayBirthdayCount > 0) {

			if (Constants.DEBUGGING_ENABLED) {
				Log.d("Receiver: _notificationContent", _notificationContent);
			}

			if (_notificationContent != "") {
				if (Constants.DEBUGGING_ENABLED) {
					Log.d("Receiver: Starting service", String.valueOf(true));
				}

				Intent notificationIntent = new Intent(_context, NotificationDisplayService.class);
				notificationIntent.putExtra(Constants.NOTIFICATION_BUNDLE, _notificationContent);
				_context.startService(notificationIntent);
			}
		}
	}
}
