package guepardoapps.whosbirthday.activities

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import com.github.guepardoapps.timext.kotlin.extensions.milliseconds
import com.github.guepardoapps.timext.kotlin.postDelayed
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.common.Constants
import guepardoapps.whosbirthday.controller.NavigationController
import guepardoapps.whosbirthday.controller.SharedPreferenceController
import guepardoapps.whosbirthday.controller.SystemInfoController
import guepardoapps.whosbirthday.database.birthday.DbBirthday
import guepardoapps.whosbirthday.model.Birthday

@ExperimentalUnsignedTypes
class ActivityBoot : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.side_boot)

        val sharedPreferenceController = SharedPreferenceController(this)

        if (!sharedPreferenceController.load(Constants.sharedPrefName, false)) {
            sharedPreferenceController.run {
                save(Constants.bubbleState, true)
                save(Constants.bubblePosX, Constants.bubbleDefaultPosX)
                save(Constants.bubblePosY, Constants.bubbleDefaultPosY)
                save(Constants.sharedPrefName, true)
            }

            DbBirthday(this).add(Birthday(0, "Jonas Schubert", "Friends", 2, 1, 1990, true, false))
        }
    }

    override fun onResume() {
        super.onResume()

        val navigationController = NavigationController(this)
        val systemInfoController = SystemInfoController(this)

        if (systemInfoController.currentAndroidApi() >= android.os.Build.VERSION_CODES.M) {
            if (systemInfoController.checkAPI23SystemPermission(Constants.systemPermissionId)) {
                Handler().postDelayed({ navigationController.navigate(ActivityMain::class.java, true) }, resources.getInteger(R.integer.bootNavigationDelayInMs).milliseconds)
            }
        } else {
            Handler().postDelayed({ navigationController.navigate(ActivityMain::class.java, true) }, resources.getInteger(R.integer.bootNavigationDelayInMs).milliseconds)
        }
    }
}