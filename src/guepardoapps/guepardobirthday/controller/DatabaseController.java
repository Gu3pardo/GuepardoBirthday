package guepardoapps.guepardobirthday.controller;

import java.util.ArrayList;

import android.content.Context;

import guepardoapps.guepardobirthday.common.Enables;
import guepardoapps.guepardobirthday.database.Database;
import guepardoapps.guepardobirthday.model.Birthday;

import guepardoapps.library.toolset.common.Logger;

public class DatabaseController {

	private static final String TAG = DatabaseController.class.getSimpleName();
	private static Logger _logger;

	private static final DatabaseController DATABASE_CONTROLLER_SINGLETON = new DatabaseController();

	private boolean _initialized;

	private Context _context;
	private static Database _database;

	private DatabaseController() {
		_logger = new Logger(TAG, Enables.DEBUGGING_ENABLED);
		_logger.Debug(TAG + " created...");
	}

	public static DatabaseController getInstance() {
		_logger.Debug("getInstance");
		return DATABASE_CONTROLLER_SINGLETON;
	}

	public boolean Initialize(Context context) {
		if (_logger == null) {
			_logger = new Logger(TAG, Enables.DEBUGGING_ENABLED);
			_logger.Debug(TAG + " created...");
		}

		_logger.Debug("Initialize");

		if (_initialized) {
			_logger.Error("Already initialized!");
			return false;
		}

		_context = context;
		_database = new Database(_context);
		_database.Open();

		_initialized = true;

		return true;
	}

	public void CreateBirthday(Birthday newBirthday) {
		_logger.Debug("CreateBirthday");

		if (!_initialized) {
			_logger.Error("Not initilialized...");
			return;
		}

		_database.CreateEntry(newBirthday);
	}

	public ArrayList<Birthday> GetBirthdays() {
		_logger.Debug("GetBirthdays");

		if (!_initialized) {
			_logger.Error("Not initilialized...");
			return new ArrayList<Birthday>();
		}

		ArrayList<Birthday> birthdays = _database.GetBirthdays();
		return birthdays;
	}

	public void UpdateBirthday(Birthday updatedBirthday) {
		_logger.Debug("UpdateBirthday");

		if (!_initialized) {
			_logger.Error("Not initilialized...");
			return;
		}

		_database.Update(updatedBirthday);
	}

	public void DeleteBirthday(Birthday deleteBirthday) {
		_logger.Debug("DeleteBirthday");

		if (!_initialized) {
			_logger.Error("Not initilialized...");
			return;
		}

		_database.Delete(deleteBirthday);
	}

	public void ClearEntries() {
		_logger.Debug("ClearEntries");

		if (!_initialized) {
			_logger.Error("Not initilialized...");
			return;
		}

		ArrayList<Birthday> birthdays = _database.GetBirthdays();
		for (Birthday entry : birthdays) {
			_database.Delete(entry);
		}
	}

	public void Dispose() {
		_logger.Debug("Dispose");
		_database.Close();
		_database = null;
		_logger = null;
		_context = null;
		_initialized = false;
	}
}
