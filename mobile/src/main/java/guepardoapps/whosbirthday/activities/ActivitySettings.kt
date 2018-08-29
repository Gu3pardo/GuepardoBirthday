package guepardoapps.whosbirthday.activities

import android.app.Activity
import android.os.Bundle
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.common.Constants
import guepardoapps.whosbirthday.controller.BirthdayController
import guepardoapps.whosbirthday.controller.SharedPreferenceController
import guepardoapps.whosbirthday.controller.SystemInfoController
import kotlinx.android.synthetic.main.side_settings.*

class ActivitySettings : Activity() {
    private lateinit var sharedPreferenceController: SharedPreferenceController
    private lateinit var systemInfoController: SystemInfoController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.side_settings)

        sharedPreferenceController = SharedPreferenceController(this)
        systemInfoController = SystemInfoController(this)

        switchBubbleState.isChecked = sharedPreferenceController.load(Constants.bubbleState, false) as Boolean
        switchBubbleState.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferenceController.save(Constants.bubbleState, isChecked)
            if (isChecked) {
                if (systemInfoController.canDrawOverlay()) {
                    systemInfoController.checkAPI23SystemPermission(Constants.systemPermissionId)
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
        if ((sharedPreferenceController.load(Constants.bubbleState, false) as Boolean)
                && systemInfoController.canDrawOverlay()) {
            BirthdayController().checkForBirthday(this)
        }
    }
}