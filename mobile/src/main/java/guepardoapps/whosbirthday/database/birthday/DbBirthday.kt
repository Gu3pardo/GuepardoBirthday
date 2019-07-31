package guepardoapps.whosbirthday.database.birthday

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import guepardoapps.whosbirthday.enum.DbBirthdayAction
import guepardoapps.whosbirthday.extensions.toBoolean
import guepardoapps.whosbirthday.extensions.toInteger
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
                        + "$ColumnId TEXT PRIMARY KEY,"
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

    override fun onDowngrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) = onUpgrade(database, oldVersion, newVersion)

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.execSQL("DROP TABLE IF EXISTS $DatabaseTable")
        onCreate(database)
    }

    fun add(birthday: Birthday): Long {
        val values = ContentValues().apply {
            put(ColumnId, birthday.id)
            put(ColumnName, birthday.name)
            put(ColumnGroup, birthday.group)
            put(ColumnDay, birthday.day)
            put(ColumnMonth, birthday.month)
            put(ColumnYear, birthday.year)
            put(ColumnRemindMe, birthday.remindMe.toInteger())
            put(ColumnRemindedMe, birthday.remindedMe.toInteger())
        }

        val returnValue = this.writableDatabase.insert(DatabaseTable, null, values)
        DbBirthdayActionPublishSubject.instance.publishSubject.onNext(DbBirthdayAction.Add)
        return returnValue
    }

    fun delete(id: Int): Int {
        val returnValue = this.writableDatabase.delete(DatabaseTable, "$ColumnId LIKE ?", arrayOf(id.toString()))
        DbBirthdayActionPublishSubject.instance.publishSubject.onNext(DbBirthdayAction.Delete)
        return returnValue
    }

    fun getGroups(): MutableList<String> {
        val cursor = this.readableDatabase.query(DatabaseTable,
                arrayOf(ColumnId, ColumnName, ColumnGroup, ColumnDay, ColumnMonth, ColumnYear, ColumnRemindMe, ColumnRemindedMe),
                null, null, null, null, "$ColumnId ASC")

        val list = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                list.add(getString(getColumnIndexOrThrow(ColumnGroup)))
            }
        }

        DbBirthdayActionPublishSubject.instance.publishSubject.onNext(DbBirthdayAction.Get)
        return list
    }

    fun getNames(): MutableList<String> {
        val cursor = this.readableDatabase.query(DatabaseTable,
                arrayOf(ColumnId, ColumnName, ColumnGroup, ColumnDay, ColumnMonth, ColumnYear, ColumnRemindMe, ColumnRemindedMe),
                null, null, null, null, "$ColumnId ASC")

        val list = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                list.add(getString(getColumnIndexOrThrow(ColumnName)))
            }
        }

        DbBirthdayActionPublishSubject.instance.publishSubject.onNext(DbBirthdayAction.Get)
        return list
    }

    fun findById(id: String): MutableList<Birthday> {
        val cursor = this.readableDatabase.query(DatabaseTable,
                arrayOf(ColumnId, ColumnName, ColumnGroup, ColumnDay, ColumnMonth, ColumnYear, ColumnRemindMe, ColumnRemindedMe),
                "$ColumnId = ?", arrayOf(id), null, null, "$ColumnId ASC")

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

    fun get(): MutableList<Birthday> {
        val cursor = this.readableDatabase.query(DatabaseTable,
                arrayOf(ColumnId, ColumnName, ColumnGroup, ColumnDay, ColumnMonth, ColumnYear, ColumnRemindMe, ColumnRemindedMe),
                null, null, null, null, "$ColumnId ASC")

        val list = mutableListOf<Birthday>()
        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(ColumnId))

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

        val returnValue = this.writableDatabase.update(DatabaseTable, values, "$ColumnId LIKE ?", arrayOf(birthday.id))
        DbBirthdayActionPublishSubject.instance.publishSubject.onNext(DbBirthdayAction.Update)
        return returnValue
    }

    companion object {
        private const val DatabaseVersion = 1
        private const val DatabaseName = "guepardoapps-whosbirthday-birthday-2.db"
        private const val DatabaseTable = "birthdayTable"

        private const val ColumnId = "id"
        private const val ColumnName = "name"
        private const val ColumnGroup = "groupType"
        private const val ColumnDay = "day"
        private const val ColumnMonth = "month"
        private const val ColumnYear = "year"
        private const val ColumnRemindMe = "remindMe"
        private const val ColumnRemindedMe = "remindedMe"
    }
}