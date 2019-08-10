package guepardoapps.whosbirthday.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ArrayAdapter
import com.github.guepardoapps.kulid.ULID
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.controller.BirthdayController
import guepardoapps.whosbirthday.database.birthday.DbBirthday
import guepardoapps.whosbirthday.extensions.integerFormat
import guepardoapps.whosbirthday.model.Birthday
import kotlinx.android.synthetic.main.side_add.*
import java.util.*

@ExperimentalUnsignedTypes
@SuppressLint("SetTextI18n")
class ActivityEdit : Activity(), DatePickerDialog.OnDateSetListener {

    private val activityContext = this

    private var dayOfMonth: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    private var month: Int = Calendar.getInstance().get(Calendar.MONTH)

    private var year: Int = Calendar.getInstance().get(Calendar.YEAR)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.side_add)

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                save_birthday_edit_button.isEnabled = true
            }

            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
        }

        birthday_name_edit_textview.apply {
            setAdapter(ArrayAdapter(activityContext, android.R.layout.simple_dropdown_item_1line, DbBirthday(activityContext).getNames()))
            addTextChangedListener(textWatcher)
        }
        birthday_group_edit_textview.apply {
            setAdapter(ArrayAdapter(activityContext, android.R.layout.simple_dropdown_item_1line, DbBirthday(activityContext).getGroups()))
            addTextChangedListener(textWatcher)
        }

        val data = intent.extras
        var birthday: Birthday? = null

        if (data != null) {
            val id = data.getString(getString(R.string.bundleDataId))!!
            birthday = DbBirthday(this).findById(id).firstOrNull()
            if (birthday != null) {
                birthday_name_edit_textview.setText(birthday.name)
                birthday_group_edit_textview.setText(birthday.group)

                year = birthday.year
                month = birthday.month - 1
                dayOfMonth = birthday.day

                birthday_DatePickerEditText.setText("${this.dayOfMonth.integerFormat(2)}.${(this.month + 1).integerFormat(2)}.${this.year.integerFormat(4)}")
            }
        }

        var showedDialog = false
        birthday_DatePickerEditText.apply {
            setOnClickListener { showedDialog = showDatePickerDialog(showedDialog, activityContext) }
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editable: Editable?) {
                    showedDialog = showDatePickerDialog(showedDialog, activityContext)
                }

                override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            })
        }

        save_birthday_edit_button.apply {
            isEnabled = false
            setOnClickListener {
                birthday_name_edit_textview.error = null
                birthday_group_edit_textview.error = null

                var cancel = false
                var focusView: View? = null

                val name = birthday_name_edit_textview.text.toString()
                if (TextUtils.isEmpty(name)) {
                    birthday_name_edit_textview.error = createErrorText(getString(R.string.error_field_required))
                    focusView = birthday_name_edit_textview
                    cancel = true
                }

                val group = birthday_group_edit_textview.text.toString()
                if (TextUtils.isEmpty(group)) {
                    birthday_group_edit_textview.error = createErrorText(getString(R.string.error_field_required))
                    focusView = birthday_group_edit_textview
                    cancel = true
                }

                if (cancel) {
                    focusView?.requestFocus()
                } else {
                    if (birthday != null) {
                        DbBirthday(activityContext)
                                .update(Birthday(
                                        id = birthday.id,
                                        name = name, group = group,
                                        day = dayOfMonth, month = month + 1, year = year,
                                        remindMe = birthday.remindMe, remindedMe = birthday.remindedMe))
                    } else {
                        DbBirthday(activityContext)
                                .add(Birthday(
                                        id = ULID.random(),
                                        name = name, group = group,
                                        day = dayOfMonth, month = month + 1, year = year,
                                        remindMe = true, remindedMe = false))
                    }
                    BirthdayController().checkForBirthday(activityContext)
                    finish()
                }
            }
        }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, month: Int, dayOfMonth: Int) {
        this.year = year
        this.month = month
        this.dayOfMonth = dayOfMonth

        birthday_DatePickerEditText
                .setText("${this.dayOfMonth.integerFormat(2)}.${(this.month + 1).integerFormat(2)}.${this.year.integerFormat(4)}")
    }

    @Suppress("DEPRECATION")
    private fun showDatePickerDialog(showedDialog: Boolean, context: ActivityEdit): Boolean = if (!showedDialog) {
        val datePickerDialog: DatePickerDialog = DatePickerDialog.newInstance(
                context,
                year,
                month,
                dayOfMonth
        )
        datePickerDialog.show(fragmentManager, "DatePickerDialog")
        true
    } else {
        false
    }

    private fun createErrorText(errorString: String): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder(errorString)
        spannableStringBuilder.setSpan(ForegroundColorSpan(Color.RED), 0, errorString.length, 0)
        return spannableStringBuilder
    }
}