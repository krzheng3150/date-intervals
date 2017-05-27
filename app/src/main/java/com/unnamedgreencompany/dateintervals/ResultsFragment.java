package com.unnamedgreencompany.dateintervals;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultsFragment extends DialogFragment {
    private static final String RESULTS_PARAM = "results";
    private static final String ZERO_INDEX_PARAM = "zeroIndexed";

    private String[] results;
    private boolean isZeroIndexed;

    private Button backButton;

    private OnFragmentInteractionListener mListener;

    public ResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param results The list of dates to print.
     * @param isZeroIndexed Are the results zero-indexed.
     * @return A new instance of fragment ResultsFragment.
     */
    public static ResultsFragment newInstance(String[] results, boolean isZeroIndexed) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putSerializable(RESULTS_PARAM, results);
        args.putSerializable(ZERO_INDEX_PARAM, isZeroIndexed);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            results = (String[])getArguments().getSerializable(RESULTS_PARAM);
            isZeroIndexed = getArguments().getBoolean(ZERO_INDEX_PARAM);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        backButton = (Button)view.findViewById(R.id.back0_button);
        backButton.setOnClickListener((View v) -> dismiss());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TableLayout table = (TableLayout) getView().findViewById(R.id.results_table);

        for (int i = 0; i < results.length; i++) {
            TableRow newRow = new TableRow(getActivity().getApplicationContext());
            TableLayout.LayoutParams lp = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            newRow.setLayoutParams(lp);

            TextView rowNumber = new TextView(getActivity().getApplicationContext());
            rowNumber.setText(String.valueOf(isZeroIndexed ? i : i+1));
            rowNumber.setTextColor(Color.BLACK);
            rowNumber.setTextSize(18f);
            rowNumber.setBackgroundResource(R.drawable.border);

            TextView dateInfo = new TextView(getActivity().getApplicationContext());
            dateInfo.setText(results[i]);
            dateInfo.setTextColor(Color.BLACK);
            dateInfo.setTextSize(18f);
            dateInfo.setBackgroundResource(R.drawable.border);

            TableRow.LayoutParams rp1 = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
            rp1.setMargins(4, 2, 4, 2);
            TableRow.LayoutParams rp2 = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 5.0f);
            rp2.setMargins(4, 2, 4, 2);

            newRow.addView(rowNumber, rp1);
            newRow.addView(dateInfo, rp2);

            table.addView(newRow, lp);
        }

        //add a blank row so last row is not obstructed when table is too big
        table.addView(new TableRow(getActivity().getApplicationContext()));
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
        void onFragmentInteraction();
    }
}
