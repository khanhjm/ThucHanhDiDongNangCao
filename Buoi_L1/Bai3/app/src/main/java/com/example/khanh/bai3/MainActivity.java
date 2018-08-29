package com.example.khanh.bai3;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // khai báo đồng hồ báo thức
    private Button mBtnAdd, mBtnMusic;
    private ListView mLvAlarm;
    private ArrayList<Alarm> mArrayAlarm;
    private ArrayList<String> mListTime;
    private ArrayAdapter<String> mAdapterAlarm;
    private MyDatabaseHelper db = new MyDatabaseHelper(this);

    // khai báo đồng hồ bấm giờ
    private TextView mTvTime;
    private Button mBtnStart, mBtnLap;
    private Chronometer mChronometer;
    private Thread mThreadChronometer;
    private int mLaps = 1;
    private Context mContext;
    private EditText mEtLaps;

    // khai bao
    private EditText mEtTimer, mEtCount;
    public Button mBtnDem;
    private Countdown mCountdown;
    private Thread mThreadCountdown;
    CountDownTimer WorkingCountDown;
    public static int secondscandemnguoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addTab();

        addControlsAlarm();
        addEventsAlarm();

        addControlsStopWatch();
        addEventsStopWatch();

        mEtTimer = (EditText) findViewById(R.id.etTimer);
        mEtCount = (EditText) findViewById(R.id.etCount);
        mBtnDem = (Button) findViewById(R.id.btn_Dem);
        mContext = this;
        mBtnDem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                secondscandemnguoc = Integer.parseInt(mEtTimer.getText().toString());
//                CountTimeWorking();
                if(mCountdown==null) {
                    mEtCount.setText(mEtTimer.getText().toString());
                    mCountdown = new Countdown(mContext);
                    mThreadCountdown = new Thread(mCountdown);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mThreadCountdown.start();
                    mCountdown.start(mEtTimer.getText().toString());
                }
            }
        });
    }

    private void CountTimeWorking() {
        Handler handler = new Handler();
        Runnable RunnableWorking = new Runnable() {
            @Override
            public void run() {
                WorkingCountDown = new CountDownTimer(secondscandemnguoc*1000, 1000) {
                    @Override
                    public void onTick(long l) {
                        secondscandemnguoc = secondscandemnguoc-1;
                        mEtCount.setText(secondscandemnguoc);
                    }

                    @Override
                    public void onFinish() {
                        Toast.makeText(MainActivity.this, "Hết thời gian", Toast.LENGTH_SHORT).show();
                    }
                }.start();
            }
        };
        handler.postDelayed(RunnableWorking, 1000);
    }
    private void addTab() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        // tab alarm
        TabHost.TabSpec tabAlarm = tabHost.newTabSpec("tabAlarm");
        tabAlarm.setIndicator("1. Alarm");
        tabAlarm.setContent(R.id.tabAlarm);
        tabHost.addTab(tabAlarm);

        // tab stop watch
        TabHost.TabSpec tabStopWatch = tabHost.newTabSpec("tabStopWatch");
        tabStopWatch.setIndicator("2. StopWatch");
        tabStopWatch.setContent(R.id.tabStopWatch);
        tabHost.addTab(tabStopWatch);

        // tab
        TabHost.TabSpec tabTimer = tabHost.newTabSpec("tabTimer");
        tabTimer.setIndicator("3. Countdown");
        tabTimer.setContent(R.id.tabTimer);
        tabHost.addTab(tabTimer);
    }

    private void addControlsAlarm() {
        mBtnAdd = (Button) findViewById(R.id.btn_Add);
        mBtnMusic = (Button) findViewById(R.id.btn_Music);
        mLvAlarm = (ListView) findViewById(R.id.lv_Alarm);
        mArrayAlarm = db.getAllAlarms();
        mListTime = new ArrayList<String>();
        for (Alarm alarm : mArrayAlarm) {
            mListTime.add((mListTime.size() + 1) + ": " + alarm.getAlarmTime());
        }
        mAdapterAlarm = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mListTime);
        mLvAlarm.setAdapter(mAdapterAlarm);
    }

    private void addEventsAlarm() {
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddAlarm.class));
            }
        });
        mBtnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AlarmReceiver.class);
                i.putExtra("extra", "off");
                sendBroadcast(i);
            }
        });
        mLvAlarm.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertDialogDeleteAlarm(mListTime.get(position));
                return false;
            }
        });
    }

    public void showAlertDialogDeleteAlarm(final String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alarm");
        builder.setMessage("Do you want to delete alarm?");
        builder.setCancelable(false);
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "You don't delete alarm!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                db.deleteAlarmByTime(text.substring(text.length() - 5, text.length()));
                addControlsAlarm();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addControlsStopWatch() {
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mBtnStart = (Button) findViewById(R.id.btn_Start);
        mBtnLap = (Button) findViewById(R.id.btn_Lap);
        mBtnLap.setVisibility(View.GONE);
        mContext = this;
        mEtLaps = (EditText) findViewById(R.id.et_laps);
        mEtLaps.setEnabled(false);
    }

    private void addEventsStopWatch() {
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtnStart.getText().toString().toLowerCase().equals("start")) {
                    if (mChronometer == null) {
                        mChronometer = new Chronometer(mContext);
                        mThreadChronometer = new Thread(mChronometer);
                        mThreadChronometer.start();
                        mChronometer.start();
                        mLaps = 1;
                        mBtnLap.setVisibility(View.VISIBLE);
                        mEtLaps.setText("");
                        mBtnStart.setText(R.string.btnStop);
                        mBtnLap.setText(R.string.btnLap);
                    }
                } else if (mBtnStart.getText().toString().toLowerCase().equals("stop")) {
                    if (mChronometer != null) {
                        mChronometer.stop();
                        mThreadChronometer.interrupt();
                        mThreadChronometer = null;
                        mBtnStart.setText(R.string.btnResume);
                        mBtnLap.setText(R.string.btnReset);
                    }
                } else if (mBtnStart.getText().toString().toLowerCase().equals("resume")) {
                    if (mChronometer != null) {
                        mChronometer.resume();
                        mThreadChronometer = new Thread(mChronometer);
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mThreadChronometer.start();
                        mBtnStart.setText(R.string.btnStop);
                        mBtnLap.setText(R.string.btnLap);
                    }
                }
            }
        });
        mBtnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtnLap.getText().toString().toLowerCase().equals("stop")) {
                    if (mChronometer != null) {
                        mChronometer.stop();
                        mThreadChronometer.interrupt();
                        mThreadChronometer = null;
                        mChronometer = null;
                    }
                } else if (mBtnLap.getText().toString().toLowerCase().equals("lap")) {
                    if (mChronometer == null) return;
                    mEtLaps.append((mLaps < 10 ? ("0" + mLaps++) : mLaps++) + " :  " + String.valueOf(mTvTime.getText()) + "\n");
//                    if (mLaps == 16) {
//                        mTvTime.setText(R.string.tv_time);
//                        mBtnLap.setVisibility(View.GONE);
//                        mBtnStart.setText(R.string.btnStart);
//                        mBtnLap.setText(R.string.btnLap);
//                        mChronometer.stop();
//                        mThreadChronometer.interrupt();
//                        mThreadChronometer = null;
//                        mChronometer = null;
//                    }
                } else if (mBtnLap.getText().toString().toLowerCase().equals("reset")) {
                    if (mChronometer != null) {
                        mChronometer = null;
                        mTvTime.setText(R.string.tv_time);
                        mEtLaps.setText("");
                        mBtnLap.setVisibility(View.GONE);
                        mBtnStart.setText(R.string.btnStart);
                        mBtnLap.setText(R.string.btnLap);
                    }
                }
            }
        });
    }

    public void updateTimerText(final String time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvTime.setText(time);
            }
        });
    }

    public void update(final String time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEtCount.setText(time);
            }
        });
    }

    // override method có sẵn trong hàm main activity để tạo menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Quit");
        menu.add(0, 1, 0, "Turn off music");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int index = item.getItemId();
        switch (index) {
            case 0:
                showAlertDialogQuit();
//                this.finish();
                break;
            case 1:
                Intent i = new Intent(MainActivity.this, AlarmReceiver.class);
                i.putExtra("extra", "off");
                sendBroadcast(i);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // show dialog quit
    public void showAlertDialogQuit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Android");
        builder.setMessage("Do you want to quit application?");
        builder.setCancelable(false);
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "You don't quit application!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                MainActivity.this.finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
