package com.example.khanh.bai3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddAlarm extends AppCompatActivity {

    private Button mBtnSave, mBtnCancel;
    private TimePicker mTimePicker;
    private Calendar mCalendar;
    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;
    private String mTimeAlarm;
    private MyDatabaseHelper db = new MyDatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        addControls();
        addEvents();
    }

    private void addControls() {
        mBtnSave = (Button) findViewById(R.id.btn_Save);
        mBtnCancel = (Button) findViewById(R.id.btn_Cancel);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true);
        mCalendar = Calendar.getInstance();
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        final Intent intent = new Intent(AddAlarm.this, AlarmReceiver.class);

    }

    private void addEvents() {
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
                mCalendar.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());

                int hour = mTimePicker.getCurrentHour();
                int minute = mTimePicker.getCurrentMinute();

                String string_hour = String.valueOf(hour);
                String string_minute = String.valueOf(minute);

                if (minute < 10) {
                    string_minute = "0" + String.valueOf(minute);
                }
                mTimeAlarm = string_hour + ":" + string_minute;

                final Intent intent = new Intent(AddAlarm.this, AlarmReceiver.class);
                intent.putExtra("extra", "on");
                // tồn tại suốt ứng dụng, ngay cả khi thoát khỏi ứng dụng
                mPendingIntent = PendingIntent.getBroadcast(
                        AddAlarm.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
                );
                mAlarmManager.set(
                        AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), mPendingIntent
                );
                Alarm alarm = new Alarm();
                alarm.setAlarmTime(mTimeAlarm);
                db.addAlarm(alarm);
                Toast.makeText(AddAlarm.this, "You set alarm: " + mTimeAlarm, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddAlarm.this, MainActivity.class));
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddAlarm.this, MainActivity.class));
            }
        });
    }
}
