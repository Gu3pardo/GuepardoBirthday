package guepardoapps.guepardobirthday.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import guepardoapps.guepardobirthday.R;
import guepardoapps.guepardobirthday.common.*;
import guepardoapps.guepardobirthday.controller.DatabaseController;
import guepardoapps.guepardobirthday.customadapter.BirthdayListAdapter;

import guepardoapps.library.toolset.common.Logger;
import guepardoapps.library.toolset.controller.NavigationController;

public class ActivityMain extends Activity {

	private static final String TAG = ActivityMain.class.getSimpleName();
	private Logger _logger;

	private boolean _created;

	private Context _context;
	private DatabaseController _databaseController;
	private NavigationController _navigationController;

	private ProgressBar _progressBar;
	private ListView _listView;
	private Button _btnAdd, _btnImpressum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.side_main);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Colors.ACTION_BAR_COLOR));

		_logger = new Logger(TAG, Enables.DEBUGGING_ENABLED);
		_logger.Debug("onCreate");

		_context = this;

		_databaseController = DatabaseController.getInstance();
		_databaseController.Initialize(_context);

		_navigationController = new NavigationController(_context);

		_listView = (ListView) findViewById(R.id.listView);

		_btnAdd = (Button) findViewById(R.id.goToAddView);
		_btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_logger.Debug("_btnAdd onClick");
				_navigationController.NavigateTo(ActivityAdd.class, false);
			}
		});

		_btnImpressum = (Button) findViewById(R.id.btnImpressum);
		_btnImpressum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_logger.Debug("_btnImpressum onClick");
				_navigationController.NavigateTo(ActivityImpressum.class, false);
			}
		});

		_progressBar = (ProgressBar) findViewById(R.id.progressBar);

		_created = true;
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

		if (_created) {
			_listView.setAdapter(new BirthdayListAdapter(_context, _databaseController.GetBirthdays()));
			_progressBar.setVisibility(View.GONE);
			_listView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		_logger.Debug("onDestroy");
		_databaseController.Dispose();
	}
}