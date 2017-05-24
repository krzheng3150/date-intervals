package com.unnamedgreencompany.dateintervals;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends FragmentActivity
        implements DatePickerFragment.OnFragmentInteractionListener, TimePickerFragment.OnFragmentInteractionListener {

    private java.text.DateFormat dateFormat;
    private java.text.DateFormat timeFormat;

    private EditText startDateEditor;
    private EditText startTimeEditor;
    private EditText endDateEditor;
    private EditText endTimeEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateFormat = DateFormat.getDateFormat(this);
        timeFormat = DateFormat.getTimeFormat(this);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);

        final Date now = c.getTime();
        c.add(Calendar.YEAR, 1);
        final Date future = c.getTime();

        startDateEditor = (EditText)findViewById(R.id.startDateInput);
        startDateEditor.setText(dateFormat.format(new Date(now.getTime())));
        startTimeEditor = (EditText)findViewById(R.id.startTimeInput);
        startTimeEditor.setText(timeFormat.format(new Date(now.getTime())));
        endDateEditor = (EditText)findViewById(R.id.endDateInput);
        endDateEditor.setText(dateFormat.format(new Date(future.getTime())));
        endTimeEditor = (EditText)findViewById(R.id.endTimeInput);
        endTimeEditor.setText(timeFormat.format(new Date(future.getTime())));
    }

    public void showDatePickerDialog(View v) {
        String fieldName = v.getTag().toString();
        int fieldId = getResources().getIdentifier(fieldName, "id", getPackageName());
        EditText dateEditor = (EditText)findViewById(fieldId);
        String value = dateEditor.getText().toString();
        DialogFragment newFragment = DatePickerFragment.newInstance(fieldId, value);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        String fieldName = v.getTag().toString();
        int fieldId = getResources().getIdentifier(fieldName, "id", getPackageName());
        EditText timeEditor = (EditText)findViewById(fieldId);
        String value = timeEditor.getText().toString();
        DialogFragment newFragment = TimePickerFragment.newInstance(fieldId, value);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onDateSelection(int fieldId, int year, int month, int day) {
        EditText dateEditor = (EditText)findViewById(fieldId);
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        dateEditor.setText(dateFormat.format(c.getTime()));
    }

    @Override
    public void onTimeSelection(int fieldId, int hourOfDay, int minute) {
        EditText timeEditor = (EditText)findViewById(fieldId);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        timeEditor.setText(timeFormat.format(c.getTime()));
    }
}
