package guepardoapps.whosbirthday.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;

import java.util.Locale;

import guepardoapps.whosbirthday.R;
import guepardoapps.whosbirthday.common.*;
import guepardoapps.whosbirthday.controller.DatabaseController;
import guepardoapps.whosbirthday.controller.DialogController;
import guepardoapps.whosbirthday.model.BirthdayDto;
import guepardoapps.whosbirthday.tools.Logger;

public class ActivityDetails extends Activity {
    private static final String TAG = ActivityDetails.class.getSimpleName();
    private Logger _logger;

    private Context _context;
    private DatabaseController _databaseController;
    private DialogController _dialogController;

    private BirthdayDto _birthday;

    private Runnable _deleteEntryRunnable = new Runnable() {
        @Override
        public void run() {
            _dialogController.CloseDialogCallback.run();
            _databaseController.DeleteBirthday(_birthday);
            finish();
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_details);

        _logger = new Logger(TAG, Enables.LOGGING);
        _logger.Debug("onCreate");

        _context = this;

        _databaseController = DatabaseController.getInstance();
        _databaseController.Initialize(_context);

        _dialogController = new DialogController(
                _context,
                getResources().getColor(R.color.TextIcon),
                getResources().getColor(R.color.Primary));

        Bundle detail = getIntent().getExtras();
        if (detail != null) {
            int id = detail.getInt(Bundles.BIRTHDAY_DETAIL_BUNDLE_TYPE_ID);

            String name = detail.getString(Bundles.BIRTHDAY_DETAIL_BUNDLE_TYPE_NAME);
            if (name == null) {
                name = "Not found...";
            }

            int day = detail.getInt(Bundles.BIRTHDAY_DETAIL_BUNDLE_TYPE_DAY);
            int month = detail.getInt(Bundles.BIRTHDAY_DETAIL_BUNDLE_TYPE_MONTH);
            int year = detail.getInt(Bundles.BIRTHDAY_DETAIL_BUNDLE_TYPE_YEAR);

            _birthday = new BirthdayDto(id, name, day, month, year);
        }

        RelativeLayout background = findViewById(R.id.detailBackground);
        if (_birthday.HasBirthday()) {
            _logger.Debug("_birthday.HasBirthday");

            background.setBackgroundColor(Colors.MARKED_BIRTHDAY_COLOR);

            new ParticleSystem((Activity) _context, 100, R.drawable.particle, 750)
                    .setSpeedModuleAndAngleRange(0f, 0.3f, 0, 90).setRotationSpeed(25).setAcceleration(0.00005f, 45)
                    .emit(findViewById(R.id.nameTextView), 100);
        }

        String dayString = String.valueOf(_birthday.GetDay());
        if (dayString.length() == 1) {
            dayString = "0" + dayString;
        }
        String monthString = String.valueOf(_birthday.GetMonth());
        if (monthString.length() == 1) {
            monthString = "0" + monthString;
        }
        String yearString = String.valueOf(_birthday.GetYear());

        TextView nameTextView = findViewById(R.id.nameTextView);
        nameTextView.setText(_birthday.GetName());
        TextView dateTextView = findViewById(R.id.dateTextView);
        dateTextView.setText("Birthday: " + dayString + "." + monthString + "." + yearString);
        TextView ageTextView = findViewById(R.id.ageTextView);
        ageTextView.setText(String.format(Locale.GERMAN, "Age: %d", _birthday.GetAge()));

        Button btnDelete = findViewById(R.id.detailDelete);
        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                _logger.Debug("_btnDelete onClick");
                _dialogController.ShowDialogDouble("Delete entry " + _birthday.GetName() + "?", "", "Delete",
                        _deleteEntryRunnable, "Cancel", _dialogController.CloseDialogCallback, true);
            }
        });
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