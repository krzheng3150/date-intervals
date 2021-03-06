package com.unnamedgreencompany.dateintervals;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ResultsActivity extends AppCompatActivity implements ResultsFragment.OnFragmentInteractionListener {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private java.text.DateFormat displayDateTimeFormat;
    private java.text.DateFormat fileNameDateTimeFormat;

    private String[] results;
    private int displayThreshold;

    private TextView statusTextView;
    private CheckBox zeroIndexCheckbox;
    private Button viewResultsButton;
    private Button downloadResultsButton;
    private Button emailResultsButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        displayDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd.HH:MM:SS");
        fileNameDateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        displayThreshold = Integer.parseInt(getString(R.string.display_max));

        zeroIndexCheckbox = (CheckBox)findViewById(R.id.zeroIndexCheckbox);
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

        Long[] timestamps = new Long[numIntervals + 1];
        double start = startPoint.getTimeInMillis();
        double end = endPoint.getTimeInMillis();
        timestamps[0] = (long) start;
        timestamps[numIntervals] = (long) end;
        for (int i = 1; i < numIntervals; i++) {
            timestamps[i] = Math.round(start + ((end - start) / numIntervals * i));
        }

        results = new String[numIntervals + 1];
        for (int i = 0; i <= numIntervals; i++) {
            results[i] = displayDateTimeFormat.format(new Date(timestamps[i]));
        }

        statusTextView = (TextView)findViewById(R.id.statusTextView);
        statusTextView.setText(getString(R.string.done_status));
        viewResultsButton.setVisibility(View.VISIBLE);
        downloadResultsButton.setVisibility(View.VISIBLE);
        emailResultsButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
    }

    public void displayResults(View v) {
        if (results.length > displayThreshold + 1) {
            alert(getString(R.string.too_many_results));
        }
        else {
            DialogFragment resultsFragment = ResultsFragment.newInstance(results, zeroIndexCheckbox.isChecked());
            resultsFragment.show(getSupportFragmentManager(), "results");
        }
    }

    public void downloadResults(View v) {
        try {
            //Note: Permission check is asynchronous so we tell the user to press download again after it's granted.
            if (!checkPermission()) {
                requestPermission();
            }
            else {
                String fileName = getDefaultFileName();
                saveFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                alert(String.format("%s %s", getString(R.string.file_save_success), fileName));
            }
        }
        catch (Exception e) {
            alert(String.format("%s %s", getString(R.string.file_save_failure), e.getMessage()));
        }
    }

    public void emailResults(View v) {
        try {
            File file = saveFile(getApplicationContext().getExternalCacheDir(), getDefaultFileName());
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_SUBJECT, "DateIntervals App Data");
            intent.putExtra(Intent.EXTRA_TEXT, "See attachment.");
            Uri uri = Uri.parse("file://" + file.getAbsolutePath());
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            if (intent.resolveActivity(getPackageManager()) == null) {
                alert(getString(R.string.email_need_setup));
            }
            else {
                startActivity(Intent.createChooser(intent, "sendEmail"));
            }
        }
        catch (Exception e) {
            alert(String.format("%s %s", getString(R.string.email_send_failure), e.getMessage()));
        }
    }

    private String getDefaultFileName() {
        return String.format("DateIntervals%s.csv", fileNameDateTimeFormat.format(new Date()));
    }

    private File saveFile(File dir, String fileName) {
        try {
            File file = new File(dir, fileName);
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            for (int i = 0; i < results.length; i++) {
                writer.println(String.format(Locale.getDefault(), "%d,%s",
                        zeroIndexCheckbox.isChecked() ? i : i+1, results[i]));
            }
            writer.close();
            return file;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private void alert(String message) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage(message);
        b.setNeutralButton(getString(R.string.ok), (DialogInterface dialog, int id) -> dialog.cancel());
        AlertDialog dialog = b.create();
        dialog.show();
    }

    public void back(View v) {
        this.finish();
    }

    @Override
    public void onFragmentInteraction() {
    }

    private boolean checkPermission() {
        //We always have permission using the WRITE_EXTERNAL_STORAGE flag before Android M
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        int result = ContextCompat.checkSelfPermission(ResultsActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ResultsActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            alert(getString(R.string.permission_granted));
        }
        else {
            alert(getString(R.string.permission_denied));
        }
    }
}
