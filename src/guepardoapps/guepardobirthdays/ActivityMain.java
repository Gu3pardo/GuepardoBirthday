package guepardoapps.guepardobirthdays;

import guepardoapps.common.Constants;
import guepardoapps.controller.DatabaseController;
import guepardoapps.toolset.controller.NavigationController;
import guepardoapps.customadapter.BirthdayListAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

public class ActivityMain extends Activity {

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
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		_context = this;
		_databaseController = new DatabaseController(_context);
		_navigationController = new NavigationController(_context);

		_listView = (ListView) findViewById(R.id.listView);
		_listView.setAdapter(
				new BirthdayListAdapter(_context, _databaseController.GetBirthdays(), _navigationController));

		_btnAdd = (Button) findViewById(R.id.goToAddView);
		_btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_navigationController.NavigateTo(ActivityAdd.class, false);
			}
		});

		_btnImpressum = (Button) findViewById(R.id.btnImpressum);
		_btnImpressum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_navigationController.NavigateTo(ActivityImpressum.class, false);
			}
		});

		_progressBar = (ProgressBar) findViewById(R.id.progressBar);
		_progressBar.setVisibility(View.GONE);
		_listView.setVisibility(View.VISIBLE);
	}

	protected void onResume() {
		super.onResume();
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