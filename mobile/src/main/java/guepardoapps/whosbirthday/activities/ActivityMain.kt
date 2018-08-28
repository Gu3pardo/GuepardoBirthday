package guepardoapps.whosbirthday.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.controller.NavigationController
import guepardoapps.whosbirthday.customadapter.BirthdayListAdapter
import guepardoapps.whosbirthday.database.birthday.DbBirthday
import kotlinx.android.synthetic.main.side_main.*

class ActivityMain : Activity() {
    private var created = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.side_main)

        btnAdd.setOnClickListener { NavigationController(this).navigate(ActivityAdd::class.java, false) }
        btnAbout.setOnClickListener { NavigationController(this).navigate(ActivityAbout::class.java, false) }

        created = true
    }

    override fun onResume() {
        super.onResume()
        if (created) {
            listView.adapter = BirthdayListAdapter(this, DbBirthday(this).get().toTypedArray())
            listView.visibility = View.VISIBLE

            progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        created = false
    }
}