package guepardoapps.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import guepardoapps.common.Constants;
import guepardoapps.guepardobirthdays.ActivityMain;
import guepardoapps.guepardobirthdays.R;

public class NotificationDisplayService extends Service {

	private Context _context;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		_context = this;

		String notificationContent = intent.getExtras().getString(Constants.NOTIFICATION_BUNDLE);

		if (Constants.DEBUGGING_ENABLED) {
			Log.d("NotificationDisplayService: notificationContent", notificationContent);
		}

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
