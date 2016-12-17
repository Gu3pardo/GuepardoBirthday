package guepardoapps.guepardobirthdays.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import guepardoapps.guepardobirthday.common.Constants;
import guepardoapps.guepardobirthday.controller.DatabaseController;
import guepardoapps.guepardobirthday.customadapter.BirthdayListAdapter;
import guepardoapps.guepardobirthdays.R;

import guepardoapps.toolset.services.NavigationService;

public class ActivityMain extends Activity {

	private boolean _created;

	private Context _context;
	private DatabaseController _databaseController;
	private NavigationService _navigationService;

	private ProgressBar _progressBar;
	private ListView _listView;
	private Button _btnAdd, _btnImpressum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.side_main);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		_context = this;
		_databaseController = new DatabaseController(_context);
		_navigationService = new NavigationService(_context);

		_listView = (ListView) findViewById(R.id.listView);

		_btnAdd = (Button) findViewById(R.id.goToAddView);
		_btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_navigationService.NavigateTo(ActivityAdd.class, false);
			}
		});

		_btnImpressum = (Button) findViewById(R.id.btnImpressum);
		_btnImpressum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_navigationService.NavigateTo(ActivityImpressum.class, false);
			}
		});

		_progressBar = (ProgressBar) findViewById(R.id.progressBar);

		_created = true;
	}

	protected void onResume() {
		super.onResume();

		if (_created) {
			_listView.setAdapter(new BirthdayListAdapter(_context, _databaseController.GetBirthdays()));
			_progressBar.setVisibility(View.GONE);
			_listView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}