package guepardoapps.whosbirthday.activities

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.common.Constants
import guepardoapps.whosbirthday.controller.NavigationController
import guepardoapps.whosbirthday.controller.SharedPreferenceController
import guepardoapps.whosbirthday.controller.SystemInfoController
import guepardoapps.whosbirthday.database.birthday.DbBirthday
import guepardoapps.whosbirthday.model.Birthday

class ActivityBoot : Activity() {

    private val delayInMillis: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.side_boot)

        val sharedPreferenceController = SharedPreferenceController(this)

        if (!(sharedPreferenceController.load(Constants.sharedPrefName, false) as Boolean)) {
            sharedPreferenceController.save(Constants.bubbleState, true)
            sharedPreferenceController.save(Constants.bubblePosX, Constants.bubbleDefaultPosX)
            sharedPreferenceController.save(Constants.bubblePosY, Constants.bubbleDefaultPosY)
            sharedPreferenceController.save(Constants.sharedPrefName, true)

            DbBirthday(this).add(Birthday(0, "Jonas Schubert", "Friends", 2, 1, 1990, true, false))
        }
    }

    override fun onResume() {
        super.onResume()

        val navigationController = NavigationController(this)
        val systemInfoController = SystemInfoController(this)

        if (systemInfoController.currentAndroidApi() >= android.os.Build.VERSION_CODES.M) {
            if (systemInfoController.checkAPI23SystemPermission(Constants.systemPermissionId)) {
                Handler().postDelayed({ navigationController.navigate(ActivityMain::class.java, true) }, delayInMillis)
            }
        } else {
            Handler().postDelayed({ navigationController.navigate(ActivityMain::class.java, true) }, delayInMillis)
        }
    }
}