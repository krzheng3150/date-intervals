package com.unnamedgreencompany.dateintervals;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class ResultsActivity extends AppCompatActivity implements ResultsFragment.OnFragmentInteractionListener {

    private Date[] results;
    private int displayThreshold;

    private TextView statusTextView;
    private Button viewResultsButton;
    private Button downloadResultsButton;
    private Button emailResultsButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        displayThreshold = Integer.parseInt(getString(R.string.display_max));

        viewResultsButton = (Button)findViewById(R.id.viewResultsButton);
        viewResultsButton.setVisibility(View.INVISIBLE);
        downloadResultsButton = (Button)findViewById(R.id.downloadResultsButton);
        downloadResultsButton.setVisibility(View.INVISIBLE);
        emailResultsButton = (Button)findViewById(R.id.emailResultsButton);
        emailResultsButton.setVisibility(View.INVISIBLE);
        backButton = (Button)findViewById(R.id.backButton);
        backButton.setVisibility(View.INVISIBLE);

        Intent submission = getIntent();
        Calendar startPoint = (Calendar)submission.getSerializableExtra(MainActivity.START_POINT_EXTRA);
        Calendar endPoint = (Calendar)submission.getSerializableExtra(MainActivity.END_POINT_EXTRA);
        int numIntervals = submission.getIntExtra(MainActivity.NUM_INTERVALS_EXTRA,
                Integer.parseInt(getString(R.string.default_num_intervals)));

        results = new Date[numIntervals + 1];
        double start = startPoint.getTimeInMillis();
        double end = endPoint.getTimeInMillis();
        results[0] = new Date((long) start);
        results[numIntervals] = new Date((long) end);
        for (int i = 1; i < numIntervals; i++) {
            results[i] = new Date(Math.round(start + ((end - start) / numIntervals * i)));
        }

        statusTextView = (TextView)findViewById(R.id.statusTextView);
        statusTextView.setText(getString(R.string.done_status));
        viewResultsButton.setVisibility(View.VISIBLE);
        downloadResultsButton.setVisibility(View.VISIBLE);
        emailResultsButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
    }

    public void displayResults(View v) {
        if (results.length + 1 > displayThreshold) {
            //Tell the user the results are too big to display
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setMessage(getString(R.string.too_many_results));
            b.setNeutralButton(getString(R.string.ok), (DialogInterface dialog, int id) -> dialog.cancel());
            AlertDialog dialog = b.create();
            dialog.show();
        }
        else {
            DialogFragment resultsFragment = ResultsFragment.newInstance(results);
            resultsFragment.show(getSupportFragmentManager(), "results");
        }
    }

    @Override
    public void onFragmentInteraction() {
    }
}
