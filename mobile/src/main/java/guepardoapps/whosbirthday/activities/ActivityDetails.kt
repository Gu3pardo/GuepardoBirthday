package guepardoapps.whosbirthday.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import com.plattysoft.leonids.ParticleSystem
import com.rey.material.app.Dialog
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.common.Constants
import guepardoapps.whosbirthday.controller.DialogController
import guepardoapps.whosbirthday.database.birthday.DbBirthday
import guepardoapps.whosbirthday.extensions.common.integerFormat
import guepardoapps.whosbirthday.utils.Logger
import kotlinx.android.synthetic.main.side_details.*

class ActivityDetails : Activity() {
    private val tag: String = ActivityDetails::class.java.simpleName

    private lateinit var dialogController: DialogController
    private lateinit var dialog: Dialog

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.side_details)

        val data = intent.extras
        if (data == null) {
            Logger.instance.error(tag, "received data is null! finishing...")
            finish()
        }

        val id = data?.getLong(Constants.bundleDataId)
        val birthday = DbBirthday(this).findById(id!!).firstOrNull()
        if (birthday == null) {
            Logger.instance.error(tag, "received birthday is null! finishing...")
            finish()
        }

        if (birthday!!.hasBirthday()) {
            detailBackground.setBackgroundColor(getColor(R.color.colorPrimaryDark))

            ParticleSystem(this, 100, R.drawable.particle, 750)
                    .setSpeedModuleAndAngleRange(0f, 0.3f, 0, 90)
                    .setRotationSpeed(25f)
                    .setAcceleration(0.00005f, 45)
                    .emit(nameTextView, 100)
        }

        nameTextView.text = birthday.name
        groupTextView.text = birthday.group
        dateTextView.text = "Birthday: ${birthday.day.integerFormat(2)}.${birthday.month.integerFormat(2)}.${birthday.year.integerFormat(4)}"
        ageTextView.text = "Age: ${birthday.getAge()}"
        remindMeCheckBox.isChecked = birthday.remindMe

        remindMeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            birthday.remindMe = isChecked
            DbBirthday(this).update(birthday)
        }

        dialogController = DialogController(this)
        dialogController.setColors(getColor(R.color.TextIcon), getColor(R.color.colorPrimary))

        detailDelete.setOnClickListener {
            dialog = dialogController.doubleButton(
                    "Delete entry ${birthday.name}?", "",
                    "Delete", {
                DbBirthday(this).delete(birthday.id.toInt())
                dialog.dismiss()
                finish()
            },
                    "Cancel", { dialog.dismiss() },
                    true)
        }
    }
}