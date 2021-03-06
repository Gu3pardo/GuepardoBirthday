package guepardoapps.whosbirthday.activities

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import com.github.guepardoapps.kulid.ULID
import com.github.guepardoapps.timext.kotlin.extensions.millis
import com.github.guepardoapps.timext.kotlin.postDelayed
import guepardoapps.whosbirthday.R
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

        if (!sharedPreferenceController.load(getString(R.string.sharedPrefName), false)) {
            sharedPreferenceController.run {
                save(getString(R.string.sharedPrefBubbleState), true)
                save(getString(R.string.sharedPrefBubblePosX), resources.getInteger(R.integer.sharedPrefBubbleDefaultPosX))
                save(getString(R.string.sharedPrefBubblePosY), resources.getInteger(R.integer.sharedPrefBubbleDefaultPosY))
                save(getString(R.string.sharedPrefName), true)
            }

            DbBirthday(this)
                    .add(Birthday(
                            id = ULID.random(),
                            name = "Jonas Schubert",
                            group = getString(R.string.friends),
                            day = 2, month = 1, year = 1990,
                            remindMe = true, remindedMe = false))
        }
    }

    override fun onResume() {
        super.onResume()

        val navigationController = NavigationController(this)
        val systemInfoController = SystemInfoController(this)

        if (systemInfoController.currentAndroidApi() >= android.os.Build.VERSION_CODES.M) {
            if (systemInfoController.checkAPI23SystemPermission(resources.getInteger(R.integer.systemPermissionId))) {
                Handler().postDelayed({ navigationController.navigate(ActivityMain::class.java, true) }, resources.getInteger(R.integer.bootNavigationDelayInMs).millis)
            }
        } else {
            Handler().postDelayed({ navigationController.navigate(ActivityMain::class.java, true) }, resources.getInteger(R.integer.bootNavigationDelayInMs).millis)
        }
    }
}