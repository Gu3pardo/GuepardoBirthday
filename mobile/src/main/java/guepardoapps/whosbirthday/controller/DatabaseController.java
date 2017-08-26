package guepardoapps.whosbirthday.controller;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import guepardoapps.whosbirthday.database.Database;
import guepardoapps.whosbirthday.model.BirthdayDto;

public class DatabaseController {
    private static final String TAG = DatabaseController.class.getSimpleName();

    private boolean _initialized;
    private Context _context;

    private static final DatabaseController DATABASE_CONTROLLER_SINGLETON = new DatabaseController();
    private static Database _database;

    private DatabaseController() {
    }

    public static DatabaseController getInstance() {
        return DATABASE_CONTROLLER_SINGLETON;
    }

    public boolean Initialize(@NonNull Context context) {
        if (_initialized) {
            return false;
        }

        _context = context;
        _database = new Database(_context);
        _database.Open();

        _initialized = true;

        return true;
    }

    public void CreateBirthday(@NonNull BirthdayDto newBirthday) {
        if (!_initialized) {
            return;
        }

        _database.CreateEntry(newBirthday);
    }

    public ArrayList<BirthdayDto> GetBirthdays() {
        if (!_initialized) {
            return new ArrayList<>();
        }

        return _database.GetBirthdays();
    }

    public ArrayList<String> GetBirthdayNames() {
        if (!_initialized) {
            return new ArrayList<>();
        }

        return _database.GetBirthdayNames();
    }

    public void UpdateBirthday(@NonNull BirthdayDto updatedBirthday) {
        if (!_initialized) {
            return;
        }

        _database.Update(updatedBirthday);
    }

    public void DeleteBirthday(@NonNull BirthdayDto deleteBirthday) {
        if (!_initialized) {
            return;
        }

        _database.Delete(deleteBirthday);
    }

    public void ClearEntries() {
        if (!_initialized) {
            return;
        }

        ArrayList<BirthdayDto> birthdays = _database.GetBirthdays();
        for (BirthdayDto entry : birthdays) {
            _database.Delete(entry);
        }
    }

    public void Dispose() {
        if (_database != null) {
            _database.Close();
        }
        _database = null;
        _context = null;
        _initialized = false;
    }
}
