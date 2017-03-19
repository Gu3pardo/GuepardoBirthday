package guepardoapps.guepardobirthday.customadapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import guepardoapps.guepardobirthday.R;
import guepardoapps.guepardobirthday.activities.ActivityDetails;
import guepardoapps.guepardobirthday.common.*;
import guepardoapps.guepardobirthday.model.Birthday;
import guepardoapps.library.particles.ParticleSystem;

import guepardoapps.library.toastview.ToastView;

import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.NavigationController;

public class BirthdayListAdapter extends BaseAdapter {

	private static final String TAG = BirthdayListAdapter.class.getSimpleName();
	private Logger _logger;

	private Context _context;
	private NavigationController _navigationController;

	private ArrayList<Birthday> _birthdays;

	private static LayoutInflater _inflater = null;

	public BirthdayListAdapter(Context context, ArrayList<Birthday> birthdays) {
		_logger = new Logger(TAG, Enables.DEBUGGING_ENABLED);
		_logger.Debug("created...");

		_context = context;
		_navigationController = new NavigationController(_context);

		_birthdays = birthdays;

		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return _birthdays.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class Holder {
		Button _name;
		TextView _date;
		ImageView _image;
	}

	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(final int index, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		View rowView = _inflater.inflate(R.layout.list_item, null);

		if (_birthdays.get(index).HasBirthday()) {
			rowView.setBackgroundColor(Colors.MARKED_BIRTHDAY_COLOR);
			new ParticleSystem((Activity) _context, 150, R.drawable.particle, 1250, 1, 255).setSpeedRange(0.2f, 0.5f)
					.oneShot(rowView, 150);
		}

		String dayString = String.valueOf(_birthdays.get(index).GetDay());
		if (dayString.length() == 1) {
			dayString = "0" + dayString;
		}
		String monthString = String.valueOf(_birthdays.get(index).GetMonth());
		if (monthString.length() == 1) {
			monthString = "0" + monthString;
		}

		holder._date = (TextView) rowView.findViewById(R.id.birthday_item_date);
		holder._date.setText(dayString + "." + monthString + "." + String.valueOf(_birthdays.get(index).GetYear()));

		holder._image = (ImageView) rowView.findViewById(R.id.birthday_item_image);
		if (_birthdays.get(index).HasBirthday()) {
			holder._image.setVisibility(View.VISIBLE);
		}

		holder._name = (Button) rowView.findViewById(R.id.birthday_item_name);
		holder._name.setText(_birthdays.get(index).GetName());
		holder._name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_logger.Debug("_name onClick");

				if (_birthdays.get(index).GetName().contains(ErrorConstants.NO_DATA_STRING)) {
					ToastView.warning(_context, ErrorConstants.NO_DATA_TO_LOAD, Toast.LENGTH_SHORT).show();
					return;
				}

				Bundle details = new Bundle();
				details.putInt(Bundles.BIRTHDAY_DETAIL_BUNDLE_TYPE_ID, _birthdays.get(index).GetId());
				details.putString(Bundles.BIRTHDAY_DETAIL_BUNDLE_TYPE_NAME, _birthdays.get(index).GetName());
				details.putInt(Bundles.BIRTHDAY_DETAIL_BUNDLE_TYPE_DAY, _birthdays.get(index).GetDay());
				details.putInt(Bundles.BIRTHDAY_DETAIL_BUNDLE_TYPE_MONTH, _birthdays.get(index).GetMonth());
				details.putInt(Bundles.BIRTHDAY_DETAIL_BUNDLE_TYPE_YEAR, _birthdays.get(index).GetYear());

				_navigationController.NavigateWithData(ActivityDetails.class, details, false);
			}
		});

		return rowView;
	}
}