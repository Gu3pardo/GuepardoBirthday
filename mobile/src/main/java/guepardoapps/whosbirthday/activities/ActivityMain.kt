package guepardoapps.whosbirthday.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.controller.NavigationController
import guepardoapps.whosbirthday.customadapter.BirthdayListAdapter
import guepardoapps.whosbirthday.database.birthday.DbBirthday
import guepardoapps.whosbirthday.enum.DbBirthdayAction
import guepardoapps.whosbirthday.model.DbBirthdayActionPublishSubject
import guepardoapps.whosbirthday.utils.Logger
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import it.sephiroth.android.library.bottomnavigation.BottomNavigation
import kotlinx.android.synthetic.main.side_main.*

@ExperimentalUnsignedTypes
class ActivityMain : Activity(), BottomNavigation.OnMenuItemSelectionListener {
    private val tag: String = ActivityMain::class.java.simpleName

    private var subscription: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.side_main)
        bottomNavigation.setOnMenuItemClickListener(this)
        subscription = DbBirthdayActionPublishSubject.instance.publishSubject
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { dbBirthdayAction ->
                            when (dbBirthdayAction) {
                                DbBirthdayAction.Add -> listView.adapter = BirthdayListAdapter(this, DbBirthday(this).get().toTypedArray())
                                DbBirthdayAction.Update -> listView.adapter = BirthdayListAdapter(this, DbBirthday(this).get().toTypedArray())
                                DbBirthdayAction.Delete -> listView.adapter = BirthdayListAdapter(this, DbBirthday(this).get().toTypedArray())
                                else -> Logger.instance.verbose(tag, "No action needed for dbBirthdayAction $dbBirthdayAction")
                            }
                        },
                        { }
                )
    }

    override fun onResume() {
        super.onResume()
        if (subscription != null) {
            listView.adapter = BirthdayListAdapter(this, DbBirthday(this).get().toTypedArray())
            listView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription?.dispose()
        subscription = null
    }

    override fun onMenuItemSelect(itemId: Int, position: Int, fromUser: Boolean) {
        performMenuAction(itemId)
    }

    override fun onMenuItemReselect(itemId: Int, position: Int, fromUser: Boolean) {
        performMenuAction(itemId)
    }

    private fun performMenuAction(itemId: Int) {
        when (itemId) {
            R.id.item_add -> NavigationController(this).navigate(ActivityEdit::class.java, false)
            R.id.item_settings -> NavigationController(this).navigate(ActivitySettings::class.java, false)
            R.id.item_about -> NavigationController(this).navigate(ActivityAbout::class.java, false)
            else -> Logger.instance.error(tag, "Found no menu entry with id $itemId")
        }
    }
}