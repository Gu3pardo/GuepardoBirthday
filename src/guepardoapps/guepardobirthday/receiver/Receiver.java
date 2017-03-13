package guepardoapps.guepardobirthday.receiver;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import guepardoapps.guepardobirthday.common.Bundles;
import guepardoapps.guepardobirthday.common.Enables;
import guepardoapps.guepardobirthday.controller.DatabaseController;
import guepardoapps.guepardobirthday.model.Birthday;
import guepardoapps.guepardobirthday.services.NotificationDisplayService;

import guepardoapps.toolset.common.Logger;

public class Receiver extends BroadcastReceiver {

	private static final String TAG = Receiver.class.getSimpleName();
	private Logger _logger;

	private Context _context;
	private ArrayList<Birthday> _birthdays;

	private DatabaseController _databaseController;

	private String _notificationContent = "";

	@Override
	public void onReceive(Context context, Intent intent) {

		if (_logger == null) {
			_logger = new Logger(TAG, Enables.DEBUGGING_ENABLED);
		}

		_logger.Debug("Receiver onReceive");

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
			_logger.Debug("_notificationContent " + _notificationContent);

			if (_notificationContent != "") {
				_logger.Debug("Receiver: Starting service");

				Intent notificationIntent = new Intent(_context, NotificationDisplayService.class);
				notificationIntent.putExtra(Bundles.NOTIFICATION_BUNDLE, _notificationContent);
				_context.startService(notificationIntent);
			}
		}
	}
}
