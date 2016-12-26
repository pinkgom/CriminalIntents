package com.cgpink.criminalintents;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.cgpink.criminalintents.database.CrimeBaseHelper;
import com.cgpink.criminalintents.database.CrimeCursorWrapper;
import com.cgpink.criminalintents.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by pinkgom on 2016-12-16.
 */

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }

    public List<Crime> getCrimes() {

        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursorWrapper = queryCrimes(null, null);

        try {
            cursorWrapper.moveToFirst();
            while(!cursorWrapper.isAfterLast()) {
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursorWrapper = queryCrimes(CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() });

        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }

            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        } finally {
            cursorWrapper.close();
        }
    }

    public void addCrime(Crime crime) {
        mDatabase.insert(CrimeTable.NAME, null, getContentValues(crime));
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME,
                values,
                CrimeTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public void remove(UUID id) {

        mDatabase.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() });
    }

    public File getPhotoFile(Crime crime) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, crime.getPhotoFilename());
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1:0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new CrimeCursorWrapper(cursor);
    }
}
