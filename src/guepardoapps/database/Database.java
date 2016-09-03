package guepardoapps.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import guepardoapps.toolset.classes.Birthday;
import guepardoapps.common.Constants;

public class Database {

	// Context
	public static final String KEY_ROWID = Constants.DATABASE_KEY_ROWID;
	public static final String KEY_NAMES = Constants.DATABASE_KEY_NAMES;
	public static final String KEY_DAYS = Constants.DATABASE_KEY_DAYS;
	public static final String KEY_MONTHS = Constants.DATABASE_KEY_MONTHS;
	public static final String KEY_YEARS = Constants.DATABASE_KEY_YEARS;

	// Database Data
	private static final String DATABASE_NAME = Constants.DATABASE_NAME;
	private static final String DATABASE_TABLE = Constants.DATABASE_TABLE;
	private static final int DATABASE_VERSION = Constants.DATABASE_VERSION;

	// Other Stuff
	private DbHelper helper;
	private final Context context;
	private SQLiteDatabase database;

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(" CREATE TABLE " + DATABASE_TABLE + " ( " + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_NAMES + " TEXT NOT NULL, " + KEY_DAYS + " TEXT NOT NULL, " + KEY_MONTHS + " TEXT NOT NULL, "
					+ KEY_YEARS + " TEXT NOT NULL); ");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(" DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}

		public void remove(Context context) {
			context.deleteDatabase(Constants.DATABASE_NAME);
		}
	}

	public Database(Context _context) {
		context = _context;
	}

	public Database open() throws SQLException {
		helper = new DbHelper(context);
		database = helper.getWritableDatabase();
		return this;
	}

	public void close() {
		helper.close();
	}

	public long CreateEntry(Birthday newBirthday) {
		ContentValues cv = new ContentValues();

		cv.put(KEY_NAMES, newBirthday.GetName());
		cv.put(KEY_DAYS, newBirthday.GetDay());
		cv.put(KEY_MONTHS, newBirthday.GetMonth());
		cv.put(KEY_YEARS, newBirthday.GetYear());

		return database.insert(DATABASE_TABLE, null, cv);
	}

	public ArrayList<Birthday> GetBirthdays() {
		String[] birthdayColumns = new String[] { KEY_ROWID, KEY_NAMES, KEY_DAYS, KEY_MONTHS, KEY_YEARS };

		Cursor c = database.query(DATABASE_TABLE, birthdayColumns, null, null, null, null, null);

		ArrayList<Birthday> birthdayResult = new ArrayList<Birthday>();

		int idIndex = c.getColumnIndex(KEY_ROWID);
		int nameIndex = c.getColumnIndex(KEY_NAMES);
		int dayIndex = c.getColumnIndex(KEY_DAYS);
		int monthIndex = c.getColumnIndex(KEY_MONTHS);
		int yearIndex = c.getColumnIndex(KEY_YEARS);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			birthdayResult.add(new Birthday(c.getInt(idIndex), c.getString(nameIndex), c.getInt(dayIndex),
					c.getInt(monthIndex), c.getInt(yearIndex)));
		}

		return birthdayResult;
	}

	public void Update(Birthday updatedBirthday) throws SQLException {
		ContentValues cv = new ContentValues();

		cv.put(KEY_NAMES, updatedBirthday.GetName());
		cv.put(KEY_DAYS, updatedBirthday.GetDay());
		cv.put(KEY_MONTHS, updatedBirthday.GetMonth());
		cv.put(KEY_YEARS, updatedBirthday.GetYear());

		database.update(DATABASE_TABLE, cv, KEY_ROWID + "=" + updatedBirthday.GetId(), null);
	}

	public void Delete(Birthday deletedBirthday) throws SQLException {
		database.delete(DATABASE_TABLE, KEY_ROWID + "=" + deletedBirthday.GetId(), null);
	}

	public void Remove() {
		helper.remove(context);
	}
}
