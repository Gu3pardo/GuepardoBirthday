package guepardoapps.common;

/**********************************************************
 * Class holding variables used all over the application *
 **********************************************************/
public class Constants {

	/*********************** Debugging **********************/

	public static boolean DEBUGGING_ENABLED = false;

	/************************* Values ***********************/

	public static int ACTION_BAR_COLOR = 0xfff57c00;
	public static int MARKED_BIRTHDAY_COLOR = 0xffff5252;

	public static String DATABASE_NAME = "Birthdaydb";
	public static String DATABASE_TABLE = "Birthdaytable";
	public static int DATABASE_VERSION = 1;

	public static String DATABASE_KEY_ROWID = "_id";
	public static String DATABASE_KEY_NAMES = "name_value";
	public static String DATABASE_KEY_DAYS = "day_value";
	public static String DATABASE_KEY_MONTHS = "month_value";
	public static String DATABASE_KEY_YEARS = "year_value";

	public static String NO_DATA_STRING = "N.a.";
	public static int NO_DATA_INTEGER = 0;
	
	public static String NO_DATA_TO_LOAD = "Nothing to load!";

	/********************** SharedPref *********************/

	public static String SHARED_PREF_NAME = "GUEPARDO_BIRTHDAYS_DATA";

	/*********************** Bundles ***********************/

	public static String NOTIFICATION_BUNDLE = "NOTIFICATION_BUNDLE";
	
	public static String BIRTHDAY_DETAIL_BUNDLE_TYPE_ID = "BIRTHDAY_DETAIL_BUNDLE_TYPE_ID";
	public static String BIRTHDAY_DETAIL_BUNDLE_TYPE_NAME = "BIRTHDAY_DETAIL_BUNDLE_TYPE_NAME";
	public static String BIRTHDAY_DETAIL_BUNDLE_TYPE_DAY = "BIRTHDAY_DETAIL_BUNDLE_TYPE_DAY";
	public static String BIRTHDAY_DETAIL_BUNDLE_TYPE_MONTH = "BIRTHDAY_DETAIL_BUNDLE_TYPE_MONTH";
	public static String BIRTHDAY_DETAIL_BUNDLE_TYPE_YEAR = "BIRTHDAY_DETAIL_BUNDLE_TYPE_YEAR";
}