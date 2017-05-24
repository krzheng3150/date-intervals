package com.unnamedgreencompany.dateintervals;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import android.text.format.DateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimePickerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FIELD_ID_PARAM = "field_id";
    private static final String FIELD_VALUE_PARAM = "field_value";

    private int fieldId;
    private String fieldValue;

    private OnFragmentInteractionListener mListener;

    public TimePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        int hour, minute;
        try {
            Date d = DateFormat.getTimeFormat(getActivity()).parse(fieldValue);
            final Calendar c = Calendar.getInstance();
            c.setTime(d);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }
        catch (Exception e) {
            hour = 0;
            minute = 0;
        }
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param fieldId The ID of the field to edit.
     * @param fieldValue The current fieldValue of that fieldId.
     * @return A new instance of fragment TimePickerFragment.
     */
    public static TimePickerFragment newInstance(int fieldId, String fieldValue) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt(FIELD_ID_PARAM, fieldId);
        args.putString(FIELD_VALUE_PARAM, fieldValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fieldId = getArguments().getInt(FIELD_ID_PARAM);
            fieldValue = getArguments().getString(FIELD_VALUE_PARAM);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mListener.onTimeSelection(fieldId, hourOfDay, minute);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onTimeSelection(int fieldId, int hourOfDay, int minute);
    }
}
