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
import android.widget.Button
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import guepardoapps.whosbirthday.R
import guepardoapps.whosbirthday.common.Constants
import guepardoapps.whosbirthday.controller.BirthdayController
import guepardoapps.whosbirthday.database.birthday.DbBirthday
import guepardoapps.whosbirthday.extensions.common.integerFormat
import guepardoapps.whosbirthday.model.Birthday
import kotlinx.android.synthetic.main.side_add.*
import java.util.*

@ExperimentalUnsignedTypes
@SuppressLint("SetTextI18n")
class ActivityEdit : Activity(), DatePickerDialog.OnDateSetListener {

    private lateinit var saveButton: Button

    private var year: Int = 1970

    private var month: Int = 0

    private var dayOfMonth: Int = 1

    private var birthday: Birthday? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.side_add)

        val now = Calendar.getInstance()
        year = now.get(Calendar.YEAR)
        month = now.get(Calendar.MONTH)
        dayOfMonth = now.get(Calendar.DAY_OF_MONTH)

        saveButton = findViewById(R.id.save_birthday_edit_button)
        saveButton.isEnabled = false

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                saveButton.isEnabled = true
            }

            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }
        }

        birthday_name_edit_textview.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, DbBirthday(this).getNames()))
        birthday_name_edit_textview.addTextChangedListener(textWatcher)

        birthday_group_edit_textview.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, DbBirthday(this).getGroups()))
        birthday_group_edit_textview.addTextChangedListener(textWatcher)

        val data = intent.extras
        if (data != null) {
            val id = data.getLong(Constants.bundleDataId)
            birthday = DbBirthday(this).findById(id).firstOrNull()
            if (birthday != null) {
                birthday_name_edit_textview.setText(birthday?.name)
                birthday_group_edit_textview.setText(birthday?.group)

                year = birthday?.year!!
                month = birthday?.month!!
                dayOfMonth = birthday?.day!!
                birthday_DatePickerEditText.setText("${this.dayOfMonth.integerFormat(2)}.${(this.month + 1).integerFormat(2)}.${this.year.integerFormat(4)}")
            }
        }

        val context = this
        var showedDialog = false
        birthday_DatePickerEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                showedDialog = showDatePickerDialog(showedDialog, context)
            }

            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
        birthday_DatePickerEditText.setOnClickListener {
            showedDialog = showDatePickerDialog(showedDialog, context)
        }

        saveButton.setOnClickListener {
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
                    DbBirthday(this).update(Birthday(birthday?.id!!, name, group, dayOfMonth, month + 1, year, birthday?.remindMe!!, birthday?.remindedMe!!))
                } else {
                    DbBirthday(this).add(Birthday(0, name, group, dayOfMonth, month + 1, year, true, false))
                }
                BirthdayController().checkForBirthday(this)
                finish()
            }
        }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, month: Int, dayOfMonth: Int) {
        this.year = year
        this.month = month
        this.dayOfMonth = dayOfMonth

        birthday_DatePickerEditText.setText("${this.dayOfMonth.integerFormat(2)}.${(this.month + 1).integerFormat(2)}.${this.year.integerFormat(4)}")
    }

    @Suppress("DEPRECATION")
    private fun showDatePickerDialog(showedDialog: Boolean, context: ActivityEdit): Boolean {
        return if (!showedDialog) {
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
    }

    private fun createErrorText(errorString: String): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder(errorString)
        spannableStringBuilder.setSpan(ForegroundColorSpan(Color.RED), 0, errorString.length, 0)
        return spannableStringBuilder
    }
}