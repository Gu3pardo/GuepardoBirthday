package guepardoapps.whosbirthday.database.birthday

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import guepardoapps.whosbirthday.extensions.common.toBoolean
import guepardoapps.whosbirthday.extensions.common.toInteger
import guepardoapps.whosbirthday.model.Birthday

// Helpful
// https://developer.android.com/training/data-storage/sqlite
// https://www.techotopia.com/index.php/A_Kotlin_Android_SQLite_Database_Tutorial

internal class DbBirthday(context: Context)
    : SQLiteOpenHelper(context, DatabaseName, null, DatabaseVersion) {

    override fun onCreate(database: SQLiteDatabase) {
        val createTable = (
                "CREATE TABLE $DatabaseTable"
                        + "("
                        + "$ColumnId INTEGER PRIMARY KEY,"
                        + "$ColumnName TEXT,"
                        + "$ColumnGroup TEXT,"
                        + "$ColumnDay  INTEGER,"
                        + "$ColumnMonth  INTEGER,"
                        + "$ColumnYear  INTEGER,"
                        + "$ColumnRemindMe  INTEGER"
                        + ")")
        database.execSQL(createTable)
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.execSQL("DROP TABLE IF EXISTS $DatabaseTable")
        onCreate(database)
    }

    override fun onDowngrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(database, oldVersion, newVersion)
    }

    fun add(birthday: Birthday): Long {
        val values = ContentValues().apply {
            put(ColumnName, birthday.name)
            put(ColumnGroup, birthday.group)
            put(ColumnDay, birthday.day)
            put(ColumnMonth, birthday.month)
            put(ColumnYear, birthday.year)
            put(ColumnRemindMe, birthday.remindMe.toInteger())
        }

        val database = this.writableDatabase
        return database.insert(DatabaseTable, null, values)
    }

    fun update(birthday: Birthday): Int {
        val values = ContentValues().apply {
            put(ColumnName, birthday.name)
            put(ColumnGroup, birthday.group)
            put(ColumnDay, birthday.day)
            put(ColumnMonth, birthday.month)
            put(ColumnYear, birthday.year)
            put(ColumnRemindMe, birthday.remindMe.toInteger())
        }

        val selection = "$ColumnId LIKE ?"
        val selectionArgs = arrayOf(birthday.id.toString())

        val database = this.writableDatabase
        return database.update(DatabaseTable, values, selection, selectionArgs)
    }

    fun delete(id: Int): Int {
        val database = this.writableDatabase

        val selection = "$ColumnId LIKE ?"
        val selectionArgs = arrayOf(id.toString())

        return database.delete(DatabaseTable, selection, selectionArgs)
    }

    fun get(): MutableList<Birthday> {
        val database = this.readableDatabase

        val projection = arrayOf(ColumnId, ColumnName, ColumnGroup, ColumnDay, ColumnMonth, ColumnYear, ColumnRemindMe)

        val sortOrder = "$ColumnId ASC"

        val cursor = database.query(
                DatabaseTable, projection, null, null,
                null, null, sortOrder)

        val list = mutableListOf<Birthday>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(ColumnId))

                val name = getString(getColumnIndexOrThrow(ColumnName))
                val group = getString(getColumnIndexOrThrow(ColumnGroup))

                val day = getInt(getColumnIndexOrThrow(ColumnDay))
                val month = getInt(getColumnIndexOrThrow(ColumnMonth))
                val year = getInt(getColumnIndexOrThrow(ColumnYear))

                val remindMe = getInt(getColumnIndexOrThrow(ColumnRemindMe)).toBoolean()

                val birthday = Birthday(id, name, group, day, month, year, remindMe)

                list.add(birthday)
            }
        }

        return list
    }

    fun findById(id: Long): MutableList<Birthday> {
        val database = this.readableDatabase

        val projection = arrayOf(ColumnId, ColumnName, ColumnGroup, ColumnDay, ColumnMonth, ColumnYear, ColumnRemindMe)

        val selection = "$ColumnId = ?"
        val selectionArgs = arrayOf(id.toString())

        val sortOrder = "$ColumnId ASC"

        val cursor = database.query(
                DatabaseTable, projection, selection, selectionArgs,
                null, null, sortOrder)

        val list = mutableListOf<Birthday>()
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(ColumnName))
                val group = getString(getColumnIndexOrThrow(ColumnGroup))

                val day = getInt(getColumnIndexOrThrow(ColumnDay))
                val month = getInt(getColumnIndexOrThrow(ColumnMonth))
                val year = getInt(getColumnIndexOrThrow(ColumnYear))

                val remindMe = getInt(getColumnIndexOrThrow(ColumnRemindMe)).toBoolean()

                val birthday = Birthday(id, name, group, day, month, year, remindMe)

                list.add(birthday)
            }
        }

        return list
    }

    fun getNames(): MutableList<String> {
        val database = this.readableDatabase

        val projection = arrayOf(ColumnId, ColumnName, ColumnGroup, ColumnDay, ColumnMonth, ColumnYear, ColumnRemindMe)

        val sortOrder = "$ColumnId ASC"

        val cursor = database.query(
                DatabaseTable, projection, null, null,
                null, null, sortOrder)

        val list = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                list.add(getString(getColumnIndexOrThrow(ColumnName)))
            }
        }

        return list
    }

    fun getGroups(): MutableList<String> {
        val database = this.readableDatabase

        val projection = arrayOf(ColumnId, ColumnName, ColumnGroup, ColumnDay, ColumnMonth, ColumnYear, ColumnRemindMe)

        val sortOrder = "$ColumnId ASC"

        val cursor = database.query(
                DatabaseTable, projection, null, null,
                null, null, sortOrder)

        val list = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                list.add(getString(getColumnIndexOrThrow(ColumnGroup)))
            }
        }

        return list
    }

    companion object {
        private const val DatabaseVersion = 1
        private const val DatabaseName = "guepardoapps-whosbirthday.db"
        private const val DatabaseTable = "birthdays"

        private const val ColumnId = "_id"
        private const val ColumnName = "name"
        private const val ColumnGroup = "group"
        private const val ColumnDay = "day"
        private const val ColumnMonth = "month"
        private const val ColumnYear = "year"
        private const val ColumnRemindMe = "remindMe"
    }
}