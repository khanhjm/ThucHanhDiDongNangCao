package com.example.khanh.bai3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Alarm_Manager";
    private static final String TABLE_ALARM = "Alarm";
    private static final String COLUMN_ALARM_ID = "Alarm_Id";
    private static final String COLUMN_ALARM_TIME = "Alarm_Time";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ...");
        String script = "CREATE TABLE " + TABLE_ALARM + "("
                + COLUMN_ALARM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ALARM_TIME + " TEXT" + ")";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ...");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
        onCreate(db);
    }

    public void addAlarm(Alarm alarm) {
        Log.i(TAG, "MyDatabaseHelper.addAlarm ...");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ALARM_TIME, alarm.getAlarmTime());
        db.insert(TABLE_ALARM, null, values);
        db.close();
    }

    public Alarm getAlarm(int id) {
        Log.i(TAG, "MyDatabaseHelper.getAlarm ...");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ALARM, new String[]{COLUMN_ALARM_ID, COLUMN_ALARM_TIME},
                COLUMN_ALARM_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Alarm alarm = new Alarm(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        return alarm;
    }

    public Alarm getAlarmByTime(String time) {
        Log.i(TAG, "MyDatabaseHelper.getAlarm ...");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ALARM, new String[]{COLUMN_ALARM_ID, COLUMN_ALARM_TIME},
                COLUMN_ALARM_TIME+ "=?", new String[]{String.valueOf(time)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Alarm alarm = new Alarm(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        return alarm;
    }

    public ArrayList<Alarm> getAllAlarms() {
        Log.i(TAG, "MyDatabaseHelper.getAllAlarms ...");
        ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
        String selectQuery = "SELECT * FROM " + TABLE_ALARM;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Alarm alarm = new Alarm();
                alarm.setAlarmID(Integer.parseInt(cursor.getString(0)));
                alarm.setAlarmTime(cursor.getString(1));
                alarmList.add(alarm);
            } while (cursor.moveToNext());
        }
        return alarmList;
    }

    public int getAlarmsCount() {
        Log.i(TAG, "MyDatabaseHelper.getAlarmsCount ...");
        String countQuery = "SELECT * FROM " + TABLE_ALARM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateAlarm(Alarm alarm) {
        Log.i(TAG, "MyDatabaseHelper.updateAlarm ... ");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ALARM_TIME, alarm.getAlarmTime());
        return db.update(TABLE_ALARM, values,
                COLUMN_ALARM_ID + "=?",
                new String[]{String.valueOf(alarm.getAlarmID())});
    }

    public void deleteAlarm(Alarm alarm) {
        Log.i(TAG, "MyDatabaseHelper.deleteAlarm ...");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARM, COLUMN_ALARM_ID + "=?",
                new String[]{String.valueOf(alarm.getAlarmID())});
        db.close();
    }

    public void deleteAlarmByTime(String text) {
        Log.i(TAG, "MyDatabaseHelper.deleteAlarm ...");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ALARM + " WHERE " + COLUMN_ALARM_TIME + " = '" + text + "'");
    }
}