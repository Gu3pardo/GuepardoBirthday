package guepardoapps.guepardobirthday.model;

import java.io.Serializable;
import java.util.Calendar;

public class Birthday implements Serializable {

	private static final long serialVersionUID = -5617469803156736803L;
	
	private int _id;
	private String _name;
	private int _day;
	private int _month;
	private int _year;

	private Calendar _today;

	public Birthday(int id, String name, int day, int month, int year) {
		_id = id;
		_name = name;
		_day = day;
		_month = month;
		_year = year;

		_today = Calendar.getInstance();
	}

	public int GetId() {
		return _id;
	}

	public String GetName() {
		return _name;
	}

	public int GetDay() {
		return _day;
	}

	public int GetMonth() {
		return _month;
	}

	public int GetYear() {
		return _year;
	}

	public int GetAge() {
		if ((_today.get(Calendar.MONTH) + 1) >= _month) {
			if (_today.get(Calendar.DAY_OF_MONTH) >= _day) {
				return _today.get(Calendar.YEAR) - _year;
			}
		}
		return _today.get(Calendar.YEAR) - _year - 1;
	}

	public boolean HasBirthday() {
		if (_day == _today.get(Calendar.DAY_OF_MONTH) && _month == (_today.get(Calendar.MONTH) + 1)) {
			return true;
		}
		return false;
	}
}