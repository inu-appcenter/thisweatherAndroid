package com.example.thisweather.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlarmDBHandler {

    private final String TAG = "AlarmDBHandler";

    private static final String DATABASE_NAME = "alarm.db";
    private static final int DATABASE_VERSION = 1;
    private Context mCtx;
    private SQLiteOpenHelper mHelper = null;
    private SQLiteDatabase mDB = null;

    public AlarmDBHandler(Context context) {
        mHelper = new AlarmDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static AlarmDBHandler open(Context context) {
        return new AlarmDBHandler(context);
    }

    public Cursor select()
    {
        mDB = mHelper.getReadableDatabase();
        Cursor c = mDB.query("alarm", null, null, null, null, null, null);
        return c;
    }

    public void insert(String day, String ampm, String hour, int isOn) {

        Log.d(TAG, "insert");

        mDB = mHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put("day", day);
        value.put("ampm", ampm);
        value.put("hour", hour);
        value.put("isOn", isOn);

        mDB.insert("alarm", null, value);

    }

    public void delete(Integer _id)
    {
        Log.d(TAG, "delete");
        mDB = mHelper.getWritableDatabase();
        mDB.delete("alarm", "_id=" + _id, null);
    }

    public void update(Integer _id, int isOn)
    {
        Log.d(TAG, "update");
        mDB = mHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put("isOn", isOn);

        mDB.update("alarm", value, "_id=" + _id, null);
    }

    public void close() {
        mHelper.close();
    }
}
