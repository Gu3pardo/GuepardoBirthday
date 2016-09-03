package guepardoapps.receiver;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import guepardoapps.toolset.classes.Birthday;
import guepardoapps.common.Constants;
import guepardoapps.controller.DatabaseController;
import guepardoapps.services.NotificationDisplayService;

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

		for (Birthday birthday : _birthdays) {
			if (birthday.HasBirthday()) {
				if (_notificationContent != "") {
					_notificationContent = "Some birthdays are today!";
				} else {
					_notificationContent = "Birthday of: " + birthday.GetName() + " is now " + birthday.GetAge()
							+ " years old!";
				}
			}
		}

		if (Constants.DEBUGGING_ENABLED) {
			Log.d("Receiver: _notificationContent", _notificationContent);
		}

		if (_notificationContent != "") {

			if (Constants.DEBUGGING_ENABLED) {
				Log.d("Receiver: Starting service", String.valueOf(true));
			}

			Intent in = new Intent(_context, NotificationDisplayService.class);
			in.putExtra(Constants.NOTIFICATION_BUNDLE, _notificationContent);
			_context.startService(in);
		}

	}
}
