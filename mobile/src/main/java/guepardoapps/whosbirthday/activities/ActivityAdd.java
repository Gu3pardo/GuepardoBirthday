package guepardoapps.whosbirthday.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.rey.material.widget.Button;

import es.dmoral.toasty.Toasty;

import guepardoapps.whosbirthday.R;
import guepardoapps.whosbirthday.common.*;
import guepardoapps.whosbirthday.controller.DatabaseController;
import guepardoapps.whosbirthday.model.BirthdayDto;
import guepardoapps.whosbirthday.tools.Logger;

public class ActivityAdd extends Activity {
    private static final String TAG = ActivityAdd.class.getSimpleName();
    private Logger _logger;

    private Context _context;
    private DatabaseController _databaseController;

    private Button _saveButton;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_add);

        _logger = new Logger(TAG, Enables.LOGGING);
        _logger.Debug("onCreate");

        _context = this;

        _databaseController = DatabaseController.getInstance();
        _databaseController.Initialize(_context);

        final AutoCompleteTextView birthdayEditTextView = findViewById(R.id.birthday_edit_textview);
        final DatePicker birthdayEditDatePicker = findViewById(R.id.birthday_edit_datePicker);

        _saveButton = findViewById(R.id.save_birthday_edit_button);

        birthdayEditTextView.setAdapter(new ArrayAdapter<>(ActivityAdd.this, android.R.layout.simple_dropdown_item_1line, _databaseController.GetBirthdayNames()));
        birthdayEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                _saveButton.setEnabled(true);
            }
        });

        birthdayEditDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _saveButton.setEnabled(true);
            }
        });

        _saveButton.setEnabled(false);
        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthdayEditTextView.setError(null);
                boolean cancel = false;
                View focusView = null;

                String birthdayName = birthdayEditTextView.getText().toString();

                if (TextUtils.isEmpty(birthdayName)) {
                    birthdayEditTextView.setError(createErrorText(getString(R.string.error_field_required)));
                    focusView = birthdayEditTextView;
                    cancel = true;
                }

                int dayOfMonth = birthdayEditDatePicker.getDayOfMonth();
                int month = birthdayEditDatePicker.getMonth();
                int year = birthdayEditDatePicker.getYear();

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    _databaseController.CreateBirthday(new BirthdayDto(0, birthdayName, dayOfMonth, month, year));
                    Toasty.success(_context, "Saved new entry " + birthdayName, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        _logger.Debug("onPause");
        _databaseController.Dispose();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _logger.Debug("onResume");
        _databaseController.Initialize(_context);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _logger.Debug("onDestroy");
        _databaseController.Dispose();
    }

    private SpannableStringBuilder createErrorText(@NonNull String errorString) {
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
        spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
        return spannableStringBuilder;
    }
}