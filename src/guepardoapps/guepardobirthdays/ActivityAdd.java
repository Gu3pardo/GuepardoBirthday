package guepardoapps.guepardobirthdays;

import guepardoapps.toolset.classes.Birthday;
import guepardoapps.common.Constants;
import guepardoapps.controller.DatabaseController;
import guepardoapps.toolset.controller.DialogController;
import guepardoapps.guepardobirthdays.R;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityAdd extends Activity {

	private Context _context;

	private DatabaseController _databaseController;
	private DialogController _dialogController;

	private String _name = null;
	private String _day = null;
	private String _month = null;
	private String _year = null;

	private EditText _nameEdit;
	private EditText _dayEdit;
	private EditText _monthEdit;
	private EditText _yearEdit;
	private Button _btnAdd;

	private Calendar _today;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.side_add);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		_context = this;

		_databaseController = new DatabaseController(_context);
		_dialogController = new DialogController(_context, getResources().getColor(R.color.TextIcon), getResources().getColor(R.color.Primary));

		_today = Calendar.getInstance();

		_nameEdit = (EditText) findViewById(R.id.nameEditText);
		_nameEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				_name = _nameEdit.getText().toString();
			}
		});

		_dayEdit = (EditText) findViewById(R.id.dayEditText);
		_dayEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				_day = _dayEdit.getText().toString();
			}
		});

		_monthEdit = (EditText) findViewById(R.id.monthEditText);
		_monthEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				_month = _monthEdit.getText().toString();
			}
		});

		_yearEdit = (EditText) findViewById(R.id.yearEditText);
		_yearEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				_year = _yearEdit.getText().toString();
			}
		});

		_btnAdd = (Button) findViewById(R.id.addButton);
		_btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				trySaveNewBirthdayCallback.run();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (_name != null || _day != null || _month != null || _year != null) {
				_dialogController.ShowDialogTriple("Warning!",
						"The created birthday is not saved! Do you want to save the birthday?", "Yes",
						trySaveNewBirthdayCallback, "No", finishCallback, "Cancel",
						_dialogController.closeDialogCallback, true);
			} else {
				finishCallback.run();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private Runnable trySaveNewBirthdayCallback = new Runnable() {
		public void run() {
			if (_name == null || _name.length() < 3) {
				Toast.makeText(_context, "Please enter a valid name!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (_day == null || _month == null || _year == null) {
				Toast.makeText(_context, "Please select a valid date!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (Integer.parseInt(_year) > _today.get(Calendar.YEAR)) {
				Toast.makeText(_context, "Please select a valid year!", Toast.LENGTH_SHORT).show();
				return;
			}

			_databaseController.CreateBirthday(
					new Birthday(0, _name, Integer.parseInt(_day), Integer.parseInt(_month), Integer.parseInt(_year)));

			finish();
		}
	};

	private Runnable finishCallback = new Runnable() {
		public void run() {
			finish();
		}
	};
}