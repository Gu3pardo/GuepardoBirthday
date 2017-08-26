package guepardoapps.whosbirthday.receiver;

import java.util.ArrayList;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import guepardoapps.whosbirthday.common.Bundles;
import guepardoapps.whosbirthday.common.Enables;
import guepardoapps.whosbirthday.controller.DatabaseController;
import guepardoapps.whosbirthday.model.BirthdayDto;
import guepardoapps.whosbirthday.services.NotificationDisplayService;
import guepardoapps.whosbirthday.tools.Logger;

public class Receiver extends BroadcastReceiver {
    private static final String TAG = Receiver.class.getSimpleName();

    private String _notificationContent = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger logger = new Logger(TAG, Enables.LOGGING);
        logger.Debug("Receiver onReceive");

        DatabaseController databaseController = DatabaseController.getInstance();
        databaseController.Initialize(context);

        ArrayList<BirthdayDto> birthdays = databaseController.GetBirthdays();

        int todayBirthdayCount = 0;
        for (BirthdayDto birthday : birthdays) {
            if (birthday.HasBirthday()) {
                todayBirthdayCount++;
                if (todayBirthdayCount > 1) {
                    _notificationContent = String.format(Locale.getDefault(), "%s%d%s", "Today are ", todayBirthdayCount, " birthdays!");
                } else {
                    _notificationContent =
                            "Birthday of: "
                                    + birthday.GetName()
                                    + " is now "
                                    + birthday.GetAge()
                                    + " years old!";
                }
            }
        }

        if (todayBirthdayCount > 0) {
            logger.Debug("_notificationContent " + _notificationContent);

            if (_notificationContent.length() > 0) {
                logger.Debug("Receiver: Starting service");

                Intent notificationIntent = new Intent(context, NotificationDisplayService.class);
                notificationIntent.putExtra(Bundles.NOTIFICATION_BUNDLE, _notificationContent);
                context.startService(notificationIntent);
            }
        }

        databaseController.Dispose();
    }
}
