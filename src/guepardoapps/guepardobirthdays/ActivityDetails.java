package guepardoapps.guepardobirthdays;

import guepardoapps.toolset.classes.Birthday;
import guepardoapps.common.*;
import guepardoapps.controller.DatabaseController;
import guepardoapps.guepardobirthdays.R;
import guepardoapps.particles.ParticleSystem;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityDetails extends Activity {

	private Context _context;
	private DatabaseController _databaseController;

	private Birthday _birthday;

	private TextView _nameTextView, _dateTextView, _ageTextView;
	private Button _btnDelete;
	private RelativeLayout _background;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.side_details);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		_context = this;
		_databaseController = new DatabaseController(_context);

		Bundle detail = getIntent().getExtras();
		_birthday = new Birthday(detail.getInt(Constants.BIRTHDAY_DETAIL_BUNDLE_TYPE_ID),
				detail.getString(Constants.BIRTHDAY_DETAIL_BUNDLE_TYPE_NAME),
				detail.getInt(Constants.BIRTHDAY_DETAIL_BUNDLE_TYPE_DAY),
				detail.getInt(Constants.BIRTHDAY_DETAIL_BUNDLE_TYPE_MONTH),
				detail.getInt(Constants.BIRTHDAY_DETAIL_BUNDLE_TYPE_YEAR));

		_background = (RelativeLayout) findViewById(R.id.detailBackground);
		if (_birthday.HasBirthday()) {
			_background.setBackgroundColor(Constants.MARKED_BIRTHDAY_COLOR);

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

		_nameTextView = (TextView) findViewById(R.id.nameTextView);
		_nameTextView.setText(_birthday.GetName());
		_dateTextView = (TextView) findViewById(R.id.dateTextView);
		_dateTextView.setText("Birthday: " + dayString + "." + monthString + "." + yearString);
		_ageTextView = (TextView) findViewById(R.id.ageTextView);
		_ageTextView.setText("Age: " + String.valueOf(_birthday.GetAge()));

		_btnDelete = (Button) findViewById(R.id.detailDelete);
		_btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_databaseController.DeleteBirthday(_birthday);
				finish();
			}
		});
	}
}