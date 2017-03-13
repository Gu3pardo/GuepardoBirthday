package guepardoapps.guepardobirthday.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import guepardoapps.guepardobirthday.common.DatabaseConstants;
import guepardoapps.guepardobirthday.model.Birthday;

public class Database {

	public static final String KEY_ROWID = DatabaseConstants.DATABASE_KEY_ROWID;
	public static final String KEY_NAMES = DatabaseConstants.DATABASE_KEY_NAMES;
	public static final String KEY_DAYS = DatabaseConstants.DATABASE_KEY_DAYS;
	public static final String KEY_MONTHS = DatabaseConstants.DATABASE_KEY_MONTHS;
	public static final String KEY_YEARS = DatabaseConstants.DATABASE_KEY_YEARS;

	private static final String DATABASE_NAME = DatabaseConstants.DATABASE_NAME;
	private static final String DATABASE_TABLE = DatabaseConstants.DATABASE_TABLE;
	private static final int DATABASE_VERSION = DatabaseConstants.DATABASE_VERSION;

	private DatabaseHelper _databaseHelper;
	private final Context _context;
	private SQLiteDatabase _database;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(" CREATE TABLE " + DATABASE_TABLE + " ( " + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_NAMES + " TEXT NOT NULL, " + KEY_DAYS + " TEXT NOT NULL, " + KEY_MONTHS + " TEXT NOT NULL, "
					+ KEY_YEARS + " TEXT NOT NULL); ");
		}

		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
			database.execSQL(" DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(database);
		}

		public void remove(Context context) {
			context.deleteDatabase(DatabaseConstants.DATABASE_NAME);
		}
	}

	public Database(Context context) {
		_context = context;
	}

	public Database open() throws SQLException {
		_databaseHelper = new DatabaseHelper(_context);
		_database = _databaseHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		_databaseHelper.close();
	}

	public long CreateEntry(Birthday newBirthday) {
		ContentValues contentValues = new ContentValues();

		contentValues.put(KEY_NAMES, newBirthday.GetName());
		contentValues.put(KEY_DAYS, newBirthday.GetDay());
		contentValues.put(KEY_MONTHS, newBirthday.GetMonth());
		contentValues.put(KEY_YEARS, newBirthday.GetYear());

		return _database.insert(DATABASE_TABLE, null, contentValues);
	}

	public ArrayList<Birthday> GetBirthdays() {
		String[] birthdayColumns = new String[] { KEY_ROWID, KEY_NAMES, KEY_DAYS, KEY_MONTHS, KEY_YEARS };

		Cursor cursor = _database.query(DATABASE_TABLE, birthdayColumns, null, null, null, null, null);

		ArrayList<Birthday> birthdayResult = new ArrayList<Birthday>();

		int idIndex = cursor.getColumnIndex(KEY_ROWID);
		int nameIndex = cursor.getColumnIndex(KEY_NAMES);
		int dayIndex = cursor.getColumnIndex(KEY_DAYS);
		int monthIndex = cursor.getColumnIndex(KEY_MONTHS);
		int yearIndex = cursor.getColumnIndex(KEY_YEARS);

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			birthdayResult.add(new Birthday(cursor.getInt(idIndex), cursor.getString(nameIndex),
					cursor.getInt(dayIndex), cursor.getInt(monthIndex), cursor.getInt(yearIndex)));
		}

		return birthdayResult;
	}

	public void Update(Birthday updatedBirthday) throws SQLException {
		ContentValues contentValues = new ContentValues();

		contentValues.put(KEY_NAMES, updatedBirthday.GetName());
		contentValues.put(KEY_DAYS, updatedBirthday.GetDay());
		contentValues.put(KEY_MONTHS, updatedBirthday.GetMonth());
		contentValues.put(KEY_YEARS, updatedBirthday.GetYear());

		_database.update(DATABASE_TABLE, contentValues, KEY_ROWID + "=" + updatedBirthday.GetId(), null);
	}

	public void Delete(Birthday deletedBirthday) throws SQLException {
		_database.delete(DATABASE_TABLE, KEY_ROWID + "=" + deletedBirthday.GetId(), null);
	}

	public void Remove() {
		_databaseHelper.remove(_context);
	}
}
