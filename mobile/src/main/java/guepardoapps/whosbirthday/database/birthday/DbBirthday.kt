package guepardoapps.whosbirthday.database.birthday

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import guepardoapps.whosbirthday.enum.DbBirthdayAction
import guepardoapps.whosbirthday.extensions.common.toBoolean
import guepardoapps.whosbirthday.extensions.common.toInteger
import guepardoapps.whosbirthday.model.Birthday
import guepardoapps.whosbirthday.model.DbBirthdayActionPublishSubject

// Helpful
// https://developer.android.com/training/data-storage/sqlite
// https://www.techotopia.com/index.php/A_Kotlin_Android_SQLite_Database_Tutorial
// https://github.com/cbeust/kotlin-android-example/blob/master/app/src/main/kotlin/com/beust/example/DbHelper.kt

internal class DbBirthday(context: Context)
    : SQLiteOpenHelper(context, DatabaseName, null, DatabaseVersion) {

    override fun onCreate(database: SQLiteDatabase) {
        val createTable = (
                "CREATE TABLE IF NOT EXISTS $DatabaseTable"
                        + "("
                        + "$ColumnId INTEGER PRIMARY KEY autoincrement,"
                        + "$ColumnName TEXT,"
                        + "$ColumnGroup TEXT,"
                        + "$ColumnDay  INTEGER,"
                        + "$ColumnMonth  INTEGER,"
                        + "$ColumnYear  INTEGER,"
                        + "$ColumnRemindMe  INTEGER,"
                        + "$ColumnRemindedMe  INTEGER"
                        + ")")
        database.execSQL(createTable)
        DbBirthdayActionPublishSubject.instance.publishSubject.onNext(DbBirthdayAction.Null)
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
            put(ColumnRemindedMe, birthday.remindedMe.toInteger())
        }

        val database = this.writableDatabase
        val returnValue = database.insert(DatabaseTable, null, values)

        DbBirthdayActionPublishSubject.instance.publishSubject.onNext(DbBirthdayAction.Add)
        return returnValue
    }

    fun update(birthday: Birthday): Int {
        val values = ContentValues().apply {
            put(ColumnName, birthday.name)
            put(ColumnGroup, birthday.group)
            put(ColumnDay, birthday.day)
            put(ColumnMonth, birthday.month)
            put(ColumnYear, birthday.year)
            put(ColumnRemindMe, birthday.remindMe.toInteger())
            put(ColumnRemindedMe, birthday.remindedMe.toInteger())
        }

        val selection = "$ColumnId LIKE ?"
        val selectionArgs = arrayOf(birthday.id.toString())

        val database = this.writableDatabase
        val returnValue = database.update(DatabaseTable, values, selection, selectionArgs)

        DbBirthdayActionPublishSubject.instance.publishSubject.onNext(DbBirthdayAction.Update)
        return returnValue
    }

    fun delete(id: Int): Int {
        val database = this.writableDatabase

        val selection = "$ColumnId LIKE ?"
        val selectionArgs = arrayOf(id.toString())
        val returnValue = database.delete(DatabaseTable, selection, selectionArgs)

        DbBirthdayActionPublishSubject.instance.publishSubject.onNext(DbBirthdayAction.Delete)
        return returnValue
    }

    fun get(): MutableList<Birthday> {
        val database = this.readableDatabase

        val projection = arrayOf(ColumnId, ColumnName, ColumnGroup, ColumnDay, ColumnMonth, ColumnYear, ColumnRemindMe, ColumnRemindedMe)

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
                val remindedMe = getInt(getColumnIndexOrThrow(ColumnRemindedMe)).toBoolean()

                val birthday = Birthday(id, name, group, day, month, year, remindMe, remindedMe)

                list.add(birthday)
            }
        }

        DbBirthdayActionPublishSubject.instance.publishSubject.onNext(DbBirthdayAction.Get)
        return list
    }

    fun findById(id: Long): MutableList<Birthday> {
        val database = this.readableDatabase

        val projection = arrayOf(ColumnId, ColumnName, ColumnGroup, ColumnDay, ColumnMonth, ColumnYear, ColumnRemindMe, ColumnRemindedMe)

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
                val remindedMe = getInt(getColumnIndexOrThrow(ColumnRemindedMe)).toBoolean()

                val birthday = Birthday(id, name, group, day, month, year, remindMe, remindedMe)

                list.add(birthday)
            }
        }

        DbBirthdayActionPublishSubject.instance.publishSubject.onNext(DbBirthdayAction.Get)
        return list
    }

    fun getNames(): MutableList<String> {
        val database = this.readableDatabase

        val projection = arrayOf(ColumnId, ColumnName, ColumnGroup, ColumnDay, ColumnMonth, ColumnYear, ColumnRemindMe, ColumnRemindedMe)

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

        DbBirthdayActionPublishSubject.instance.publishSubject.onNext(DbBirthdayAction.Get)
        return list
    }

    fun getGroups(): MutableList<String> {
        val database = this.readableDatabase

        val projection = arrayOf(ColumnId, ColumnName, ColumnGroup, ColumnDay, ColumnMonth, ColumnYear, ColumnRemindMe, ColumnRemindedMe)

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

        DbBirthdayActionPublishSubject.instance.publishSubject.onNext(DbBirthdayAction.Get)
        return list
    }

    companion object {
        private const val DatabaseVersion = 1
        private const val DatabaseName = "guepardoapps-whosbirthday-birthday.db"
        private const val DatabaseTable = "birthdayTable"

        private const val ColumnId = "_id"
        private const val ColumnName = "name"
        private const val ColumnGroup = "groupType"
        private const val ColumnDay = "day"
        private const val ColumnMonth = "month"
        private const val ColumnYear = "year"
        private const val ColumnRemindMe = "remindMe"
        private const val ColumnRemindedMe = "remindedMe"
    }
}