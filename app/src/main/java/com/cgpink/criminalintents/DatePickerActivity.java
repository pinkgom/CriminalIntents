package com.cgpink.criminalintents;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Date;

/**
 * Created by pinkgom on 2016-12-16.
 */

public class DatePickerActivity extends SingleFragmentActivity {

    private static final String EXTRA_DATE_PICKER_ID = "com.cgpink.criminalintent.date_picker";

    @Override
    protected Fragment createFragment() {
        Date date = (Date) getIntent().getSerializableExtra(EXTRA_DATE_PICKER_ID);
        return DatePickerFragment.newInstance(date);
    }


    public static Intent newIntent(Context packageContext, Date date) {
        Intent intent = new Intent(packageContext, DatePickerActivity.class);
        intent.putExtra(EXTRA_DATE_PICKER_ID, date);

        return intent;
    }
}