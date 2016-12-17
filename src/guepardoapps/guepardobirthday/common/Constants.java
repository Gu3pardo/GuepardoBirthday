package guepardoapps.guepardobirthday.common;

public class Constants {
	/********************** Debugging *********************/
	public static final boolean DEBUGGING_ENABLED = true;

	/************************* Color **********************/
	public static final int ACTION_BAR_COLOR = 0xfff57c00;
	public static final int MARKED_BIRTHDAY_COLOR = 0xffff5252;

	/************************ Database ********************/
	public static final String DATABASE_NAME = "Birthdaydb";
	public static final String DATABASE_TABLE = "Birthdaytable";
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_KEY_ROWID = "_id";
	public static final String DATABASE_KEY_NAMES = "name_value";
	public static final String DATABASE_KEY_DAYS = "day_value";
	public static final String DATABASE_KEY_MONTHS = "month_value";
	public static final String DATABASE_KEY_YEARS = "year_value";

	/************************* Error **********************/
	public static final String NO_DATA_STRING = "N.a.";
	public static final int NO_DATA_INTEGER = 0;
	public static final String NO_DATA_TO_LOAD = "Nothing to load!";

	/********************** SharedPref *********************/
	public static final String SHARED_PREF_NAME = "GUEPARDO_BIRTHDAYS_DATA";

	/*********************** Bundles ***********************/
	public static final String NOTIFICATION_BUNDLE = "NOTIFICATION_BUNDLE";
	public static final String BIRTHDAY_DETAIL_BUNDLE_TYPE_ID = "BIRTHDAY_DETAIL_BUNDLE_TYPE_ID";
	public static final String BIRTHDAY_DETAIL_BUNDLE_TYPE_NAME = "BIRTHDAY_DETAIL_BUNDLE_TYPE_NAME";
	public static final String BIRTHDAY_DETAIL_BUNDLE_TYPE_DAY = "BIRTHDAY_DETAIL_BUNDLE_TYPE_DAY";
	public static final String BIRTHDAY_DETAIL_BUNDLE_TYPE_MONTH = "BIRTHDAY_DETAIL_BUNDLE_TYPE_MONTH";
	public static final String BIRTHDAY_DETAIL_BUNDLE_TYPE_YEAR = "BIRTHDAY_DETAIL_BUNDLE_TYPE_YEAR";
}