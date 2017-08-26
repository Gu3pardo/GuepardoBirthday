package guepardoapps.whosbirthday.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import guepardoapps.whosbirthday.R;
import guepardoapps.whosbirthday.activities.ActivityMain;
import guepardoapps.whosbirthday.common.Bundles;
import guepardoapps.whosbirthday.common.Enables;
import guepardoapps.whosbirthday.tools.Logger;

public class NotificationDisplayService extends Service {
    private static final String TAG = NotificationDisplayService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger logger = new Logger(TAG, Enables.LOGGING);
        logger.Debug("onStartCommand");

        String notificationContent = intent.getExtras().getString(Bundles.NOTIFICATION_BUNDLE);

        logger.Debug("notificationContent is " + notificationContent);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 23, new Intent(this, ActivityMain.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Who's Birthday")
                .setContentText(notificationContent)
                .setTicker(notificationContent).setContentIntent(pendingIntent)
                .setAutoCancel(false);

        Notification notification = builder.build();
        notificationManager.notify(R.mipmap.ic_launcher, notification);

        stopService(new Intent(this, NotificationDisplayService.class));

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
