package guepardoapps.whosbirthday.activities

import android.app.Activity
import android.os.Bundle
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.controller.BirthdayController
import guepardoapps.whosbirthday.controller.SharedPreferenceController
import guepardoapps.whosbirthday.controller.SystemInfoController
import kotlinx.android.synthetic.main.side_settings.*

@ExperimentalUnsignedTypes
class ActivitySettings : Activity() {

    private val sharedPreferenceController: SharedPreferenceController = SharedPreferenceController(this)

    private val systemInfoController: SystemInfoController = SystemInfoController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.side_settings)

        switchBubbleState.isChecked = sharedPreferenceController.load(getString(R.string.sharedPrefBubbleState), false)
        switchBubbleState.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferenceController.save(getString(R.string.sharedPrefBubbleState), isChecked)
            if (isChecked) {
                if (systemInfoController.canDrawOverlay()) {
                    systemInfoController.checkAPI23SystemPermission(resources.getInteger(R.integer.systemPermissionId))
                } else {
                    BirthdayController().checkForBirthday(this)
                }
            } else {
                BirthdayController().closeFloating(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if ((sharedPreferenceController.load(getString(R.string.sharedPrefBubbleState), false)) && systemInfoController.canDrawOverlay()) {
            BirthdayController().checkForBirthday(this)
        }
    }
}