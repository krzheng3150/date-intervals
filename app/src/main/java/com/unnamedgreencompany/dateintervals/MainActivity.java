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

        EditText startDateEditor = (EditText)findViewById(R.id.startDateInput);
        startDateEditor.setText(dateFormat.format(new Date(now.getTime())));
        EditText startTimeEditor = (EditText)findViewById(R.id.startTimeInput);
        startTimeEditor.setText(timeFormat.format(new Date(now.getTime())));
        EditText endDateEditor = (EditText)findViewById(R.id.endDateInput);
        endDateEditor.setText(dateFormat.format(new Date(future.getTime())));
        EditText endTimeEditor = (EditText)findViewById(R.id.endTimeInput);
        endTimeEditor.setText(timeFormat.format(new Date(future.getTime())));
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = DatePickerFragment.newInstance(v.getTag().toString(), null);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = TimePickerFragment.newInstance(v.getTag().toString(), null);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onDateSelection(String field, int year, int month, int day) {
        int fieldId = getResources().getIdentifier(field, "id", getPackageName());
        EditText dateEditor = (EditText)findViewById(fieldId);
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        dateEditor.setText(dateFormat.format(c.getTime()));
    }

    @Override
    public void onTimeSelection(String field, int hourOfDay, int minute) {
        int fieldId = getResources().getIdentifier(field, "id", getPackageName());
        EditText timeEditor = (EditText)findViewById(fieldId);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        timeEditor.setText(timeFormat.format(c.getTime()));
    }
}
