package guepardoapps.guepardobirthday.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import guepardoapps.guepardobirthday.common.Bundles;
import guepardoapps.guepardobirthday.common.Enables;
import guepardoapps.guepardobirthdays.R;
import guepardoapps.guepardobirthdays.activities.ActivityMain;

import guepardoapps.toolset.common.Logger;

public class NotificationDisplayService extends Service {

	private static final String TAG = NotificationDisplayService.class.getSimpleName();
	private Logger _logger;

	private Context _context;

	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		_logger = new Logger(TAG, Enables.DEBUGGING_ENABLED);
		_logger.Debug("onStartCommand");

		_context = this;

		String notificationContent = intent.getExtras().getString(Bundles.NOTIFICATION_BUNDLE);

		_logger.Debug("notificationContent is " + notificationContent);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 23, new Intent(_context, ActivityMain.class),
				PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationManager notificationManager = (NotificationManager) _context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification.Builder builder = new Notification.Builder(_context);
		builder.setSmallIcon(R.drawable.ic_launcher).setContentTitle("Guepardo Birthday")
				.setContentText(notificationContent).setTicker(notificationContent).setContentIntent(pendingIntent)
				.setAutoCancel(false);

		Notification notification = builder.build();
		notificationManager.notify(R.drawable.ic_launcher, notification);

		stopService(new Intent(_context, NotificationDisplayService.class));

		return 0;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
