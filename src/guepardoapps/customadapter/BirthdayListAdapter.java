package guepardoapps.customadapter;

import guepardoapps.toolset.classes.Birthday;
import guepardoapps.common.Constants;
import guepardoapps.toolset.controller.NavigationController;
import guepardoapps.guepardobirthdays.ActivityDetails;
import guepardoapps.guepardobirthdays.R;
import guepardoapps.particles.ParticleSystem;

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

/**********************************************************
 ********* Custom List adapter for the birthdays *********
 **********************************************************/
public class BirthdayListAdapter extends BaseAdapter {
	/*
	 * variables for the birthday displayed in the list and in the details
	 */
	private ArrayList<Birthday> _birthdays;

	/*
	 * variable to prevent "this"
	 */
	private Context _context;

	/*
	 * controller used for navigating through the activities
	 */
	private NavigationController _navigationController;

	/*
	 * variable to handle the layout
	 */
	private static LayoutInflater _inflater = null;

	/*
	 * constructor where the entered data is assigned to the arrays and the
	 * current date is calculated
	 */
	public BirthdayListAdapter(Context context, ArrayList<Birthday> birthdays,
			NavigationController navigationController) {
		_context = context;
		_navigationController = navigationController;

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

	/*
	 * internal class which contains the views a button for the name and loading
	 * the details and a textView for the date of the birthday
	 */
	public class Holder {
		Button _name;
		TextView _date;
		ImageView _image;
	}

	/*
	 * creating the view for each entered item
	 */
	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(final int index, View convertView, ViewGroup parent) {
		/*
		 * creating an instance of the class Holder for each item
		 */
		Holder holder = new Holder();

		/*
		 * inflate the defined view in the layout for each item
		 */
		View rowView = _inflater.inflate(R.layout.list_item, null);

		/*
		 * if day and month of the birthday are the same as the current day and
		 * month it will be marked with an different color
		 */
		if (_birthdays.get(index).HasBirthday()) {
			rowView.setBackgroundColor(Constants.MARKED_BIRTHDAY_COLOR);
			new ParticleSystem((Activity) _context, 150, R.drawable.particle, 1250).setSpeedRange(0.2f, 0.5f)
					.oneShot(rowView, 150);
		}

		/*
		 * creating the dayString and the monthString to check the length
		 * prepending a 0 if a string has a length of 1
		 */
		String dayString = String.valueOf(_birthdays.get(index).GetDay());
		if (dayString.length() == 1) {
			dayString = "0" + dayString;
		}
		String monthString = String.valueOf(_birthdays.get(index).GetMonth());
		if (monthString.length() == 1) {
			monthString = "0" + monthString;
		}

		/*
		 * setting the text of the date textView to the created date
		 */
		holder._date = (TextView) rowView.findViewById(R.id.birthday_item_date);
		holder._date.setText(dayString + "." + monthString + "." + String.valueOf(_birthdays.get(index).GetYear()));

		/*
		 * setting the visibility of the imageView to true if a birthday is
		 * today
		 */
		holder._image = (ImageView) rowView.findViewById(R.id.birthday_item_image);
		if (_birthdays.get(index).HasBirthday()) {
			holder._image.setVisibility(View.VISIBLE);
		}

		/*
		 * setting the text of the name button to the birthday name and adding a
		 * onClickListener to go to the details if clicked
		 */
		holder._name = (Button) rowView.findViewById(R.id.birthday_item_name);
		holder._name.setText(_birthdays.get(index).GetName());
		holder._name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * error handling: if there is no data no navigation to the
				 * details will be performed
				 */
				if (_birthdays.get(index).GetName().contains(Constants.NO_DATA_STRING)) {
					Toast.makeText(_context, Constants.NO_DATA_TO_LOAD, Toast.LENGTH_SHORT).show();
					return;
				}

				/*
				 * create a bundle used in a intent which will be send to the
				 * details activity
				 */
				Bundle details = new Bundle();
				details.putInt(Constants.BIRTHDAY_DETAIL_BUNDLE_TYPE_ID, _birthdays.get(index).GetId());
				details.putString(Constants.BIRTHDAY_DETAIL_BUNDLE_TYPE_NAME, _birthdays.get(index).GetName());
				details.putInt(Constants.BIRTHDAY_DETAIL_BUNDLE_TYPE_DAY, _birthdays.get(index).GetDay());
				details.putInt(Constants.BIRTHDAY_DETAIL_BUNDLE_TYPE_MONTH, _birthdays.get(index).GetMonth());
				details.putInt(Constants.BIRTHDAY_DETAIL_BUNDLE_TYPE_YEAR, _birthdays.get(index).GetYear());

				_navigationController.NavigateWithData(ActivityDetails.class, details, false);
			}
		});
		return rowView;
	}
}