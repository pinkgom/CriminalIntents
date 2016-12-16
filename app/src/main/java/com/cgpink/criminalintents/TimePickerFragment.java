package com.cgpink.criminalintents;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

/**
 * Created by pinkgom on 2016-12-16.
 */

public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_HOUR = "crime_hour";
    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance() {

        TimePickerFragment fragment = new TimePickerFragment();

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        mTimePicker = (TimePicker) view.findViewById(R.id.dialog_time_time_picker);


        return new AlertDialog.Builder(getActivity())
                .setView(mTimePicker)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_HOUR, mTimePicker.getCurrentHour());
                        getTargetFragment().onActivityResult(getTargetRequestCode(),
                                Activity.RESULT_OK,
                                intent);
                    }
                })
                .create();


    }
}

