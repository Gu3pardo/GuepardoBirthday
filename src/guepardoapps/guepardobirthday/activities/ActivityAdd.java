package guepardoapps.guepardobirthday.activities;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import guepardoapps.library.toastview.ToastView;

import guepardoapps.guepardobirthday.R;
import guepardoapps.guepardobirthday.common.*;
import guepardoapps.guepardobirthday.controller.DatabaseController;
import guepardoapps.guepardobirthday.model.Birthday;

import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.DialogController;

public class ActivityAdd extends Activity {

	private static final String TAG = ActivityAdd.class.getSimpleName();
	private Logger _logger;

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

	private Runnable _trySaveNewBirthdayCallback = new Runnable() {
		public void run() {
			if (_name == null || _name.length() < 3) {
				ToastView.error(_context, "Please enter a valid name!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (_day == null || _month == null || _year == null || _day.length() < 1 || _month.length() < 1
					|| _year.length() != 4) {
				ToastView.error(_context, "Please select a valid date!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (Integer.parseInt(_year) < 1900 || Integer.parseInt(_year) > _today.get(Calendar.YEAR)) {
				_yearEdit.setText("");
				ToastView.error(_context, "Please select a valid year!", Toast.LENGTH_SHORT).show();
				return;
			}

			_databaseController.CreateBirthday(
					new Birthday(0, _name, Integer.parseInt(_day), Integer.parseInt(_month), Integer.parseInt(_year)));

			ToastView.success(_context, "Saved new entry " + _name, Toast.LENGTH_LONG).show();

			finish();
		}
	};

	private Runnable _finishCallback = new Runnable() {
		public void run() {
			finish();
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.side_add);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Colors.ACTION_BAR_COLOR));

		_logger = new Logger(TAG, Enables.DEBUGGING_ENABLED);
		_logger.Debug("onCreate");

		_context = this;

		_databaseController = new DatabaseController(_context);
		_dialogController = new DialogController(_context, getResources().getColor(R.color.TextIcon),
				getResources().getColor(R.color.Primary));

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

				try {
					if (Integer.parseInt(_day) < 1 || Integer.parseInt(_day) > 31) {
						_dayEdit.setText("");
					}
				} catch (Exception e) {
					_logger.Error(e.toString());
				}

				if (_day.length() > 2) {
					_dayEdit.setText("" + _day.charAt(0) + _day.charAt(1));
				}

				if (_day.length() == 2) {
					_monthEdit.requestFocus();
				}
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

				try {
					if (Integer.parseInt(_month) < 1 || Integer.parseInt(_month) > 12) {
						_monthEdit.setText("");
					}
				} catch (Exception e) {
					_logger.Error(e.toString());
				}

				if (_month.length() > 2) {
					_monthEdit.setText("" + _month.charAt(0) + _month.charAt(1));
				}

				if (_month.length() == 2) {
					_yearEdit.requestFocus();
				}
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

				try {
					if (Integer.parseInt(_year) < 1900
							|| Integer.parseInt(_year) > Calendar.getInstance().get(Calendar.YEAR)) {
						// _yearEdit.setText("");
					}
				} catch (Exception e) {
					_logger.Error(e.toString());
				}

				if (_year.length() > 4) {
					_yearEdit.setText("" + _year.charAt(0) + _year.charAt(1) + _year.charAt(2) + _year.charAt(3));
				}

				if (_year.length() == 4) {
					// Hide soft keyboard
					View view = ((Activity) _context).getCurrentFocus();
					if (view != null) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					}
				}
			}
		});

		_btnAdd = (Button) findViewById(R.id.addButton);
		_btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_logger.Debug("_btnAdd onClick");
				_trySaveNewBirthdayCallback.run();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		_logger.Debug("onKeyDown");

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (_name != null || _day != null || _month != null || _year != null) {
				_dialogController.ShowDialogTriple("Warning!",
						"The created birthday is not saved! Do you want to save the birthday?", "Yes",
						_trySaveNewBirthdayCallback, "No", _finishCallback, "Cancel",
						_dialogController.CloseDialogCallback, true);
			} else {
				_finishCallback.run();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}