package com.unnamedgreencompany.dateintervals;

import android.content.Intent;
import android.net.Uri;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements DatePickerFragment.OnFragmentInteractionListener,
                   TimePickerFragment.OnFragmentInteractionListener,
                   ResultsFragment.OnFragmentInteractionListener {

    private java.text.DateFormat dateFormat;
    private java.text.DateFormat timeFormat;

    private EditText startDateEditor;
    private EditText startTimeEditor;
    private EditText endDateEditor;
    private EditText endTimeEditor;
    private EditText numIntervalsEditor;

    private int minIntervals;
    private int maxIntervals;

    static final String START_POINT_EXTRA = "start_point";
    static final String END_POINT_EXTRA = "end_point";
    static final String NUM_INTERVALS_EXTRA = "num_intervals";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        minIntervals = Integer.parseInt(getString(R.string.interval_min));
        maxIntervals = Integer.parseInt(getString(R.string.interval_max));

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

        numIntervalsEditor = (EditText)findViewById(R.id.numIntervalsInput);
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

    private Calendar mergeDateAndTime(Date date, Date time) {
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(time);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        dateCal.set(Calendar.SECOND, 0);
        dateCal.set(Calendar.MILLISECOND, 0);
        return dateCal;
    }

    public void submitInputs(View v) {
        //Validate the inputs first
        int numIntervals = Integer.parseInt(numIntervalsEditor.getText().toString());
        if (numIntervals < minIntervals || numIntervals > maxIntervals) {
            showError(R.string.interval_count_bounds);
            return;
        }
        Calendar startPoint, endPoint;
        try {
            Date startDate = dateFormat.parse(startDateEditor.getText().toString());
            Date startTime = timeFormat.parse(startTimeEditor.getText().toString());
            startPoint = mergeDateAndTime(startDate, startTime);
            Date endDate = dateFormat.parse(endDateEditor.getText().toString());
            Date endTime = timeFormat.parse(endTimeEditor.getText().toString());
            endPoint = mergeDateAndTime(endDate, endTime);
        }
        catch (Exception e) {
            showError(R.string.invalid_dates);
            return;
        }
        if (endPoint.before(startPoint)) {
            showError(R.string.end_before_start);
            return;
        }

        Intent resultsIntent = new Intent(this, ResultsActivity.class);
        resultsIntent.putExtra(START_POINT_EXTRA, startPoint);
        resultsIntent.putExtra(END_POINT_EXTRA, endPoint);
        resultsIntent.putExtra(NUM_INTERVALS_EXTRA, numIntervals);
        startActivity(resultsIntent);
    }

    private void showError(int messageId) {
        Toast toast = Toast.makeText(getApplicationContext(), messageId, Toast.LENGTH_LONG);
        TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
        toastMessage.setBackgroundColor(Color.RED);
        toast.show();
    }

    @Override
    public void onTimeSelection(int fieldId, int hourOfDay, int minute) {
        EditText timeEditor = (EditText)findViewById(fieldId);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        timeEditor.setText(timeFormat.format(c.getTime()));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
