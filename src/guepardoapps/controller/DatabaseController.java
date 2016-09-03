package guepardoapps.controller;

import java.util.ArrayList;

import guepardoapps.toolset.classes.Birthday;
import guepardoapps.database.Database;

import android.content.Context;

public class DatabaseController {

	private Context _context;
	private static Database _database;

	public DatabaseController(Context context) {
		_context = context;
		_database = new Database(_context);
	}

	public void CreateBirthday(Birthday newBirthday) {
		_database.open();
		_database.CreateEntry(newBirthday);
		_database.close();
	}

	public ArrayList<Birthday> GetBirthdays() {
		_database.open();
		ArrayList<Birthday> birthdays = _database.GetBirthdays();
		_database.close();

		return birthdays;
	}

	public void UpdateBirthday(Birthday updatedBirthday) {
		_database.open();
		_database.Update(updatedBirthday);
		_database.close();
	}

	public void DeleteBirthday(Birthday deleteBirthday) {
		_database.open();
		_database.Delete(deleteBirthday);
		_database.close();
	}

	public void RemoveBirthdays() {
		_database.open();
		_database.close();
		_database.Remove();
	}
}
