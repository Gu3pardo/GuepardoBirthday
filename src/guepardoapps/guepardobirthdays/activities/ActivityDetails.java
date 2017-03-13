package guepardoapps.guepardobirthdays.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import guepardoapps.guepardobirthday.common.*;
import guepardoapps.guepardobirthday.controller.DatabaseController;
import guepardoapps.guepardobirthday.model.Birthday;
import guepardoapps.guepardobirthdays.R;

import guepardoapps.particles.ParticleSystem;

import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.DialogController;

public class ActivityDetails extends Activity {

	private static final String TAG = ActivityDetails.class.getSimpleName();
	private Logger _logger;

	private Context _context;
	private DatabaseController _databaseController;
	private DialogController _dialogController;

	private Birthday _birthday;

	private TextView _nameTextView, _dateTextView, _ageTextView;
	private Button _btnDelete;
	private RelativeLayout _background;

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
		getActionBar().setBackgroundDrawable(new ColorDrawable(Colors.ACTION_BAR_COLOR));

		_logger = new Logger(TAG, Enables.DEBUGGING_ENABLED);
		_logger.Debug("onCreate");

		_context = this;

		_databaseController = new DatabaseController(_context);
		_dialogController = new DialogController(_context, getResources().getColor(R.color.TextIcon),
				getResources().getColor(R.color.Primary));

		Bundle detail = getIntent().getExtras();
		_birthday = new Birthday(detail.getInt(Bundles.BIRTHDAY_DETAIL_BUNDLE_TYPE_ID),
				detail.getString(Bundles.BIRTHDAY_DETAIL_BUNDLE_TYPE_NAME),
				detail.getInt(Bundles.BIRTHDAY_DETAIL_BUNDLE_TYPE_DAY),
				detail.getInt(Bundles.BIRTHDAY_DETAIL_BUNDLE_TYPE_MONTH),
				detail.getInt(Bundles.BIRTHDAY_DETAIL_BUNDLE_TYPE_YEAR));

		_background = (RelativeLayout) findViewById(R.id.detailBackground);
		if (_birthday.HasBirthday()) {
			_logger.Debug("_birthday.HasBirthday");

			_background.setBackgroundColor(Colors.MARKED_BIRTHDAY_COLOR);

			new ParticleSystem((Activity) _context, 100, R.drawable.particle, 750, 1, 255)
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

		_nameTextView = (TextView) findViewById(R.id.nameTextView);
		_nameTextView.setText(_birthday.GetName());
		_dateTextView = (TextView) findViewById(R.id.dateTextView);
		_dateTextView.setText("Birthday: " + dayString + "." + monthString + "." + yearString);
		_ageTextView = (TextView) findViewById(R.id.ageTextView);
		_ageTextView.setText("Age: " + String.valueOf(_birthday.GetAge()));

		_btnDelete = (Button) findViewById(R.id.detailDelete);
		_btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				_logger.Debug("_btnDelete onClick");
				_dialogController.ShowDialogDouble("Delete entry " + _birthday.GetName() + "?", "", "Delete",
						_deleteEntryRunnable, "Cancel", _dialogController.CloseDialogCallback, true);
			}
		});
	}
}