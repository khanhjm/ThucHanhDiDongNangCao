package com.example.khanh.bai2;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.PropertyPermission;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler = new Handler();
    private int start;
    private int start2;
    private TextView tv1, tv2, tv3;
    private Button btn1, btn2, btn3;
    private Thread t1, t2, t3;

    private boolean b1 = false;
    private boolean b2 = false;
    private boolean b3 = false;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int x1 = msg.getData().getInt("1");
                if (x1 != 0) {
                    tv1.setText(String.valueOf(x1));
                }
                int x2 = msg.getData().getInt("2");
                if (x2 != 0) {
                    tv2.setText(String.valueOf(x2));
                }
                int x3 = msg.getData().getInt("3");
                if (x3 != 0) {
                    tv3.setText(String.valueOf(x3));
                }
            }
        };

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                if (!b1) {
                    b1 = true;
                    play1();
                    btn1.setText("stop");
                } else {
                    b1 = false;
                    btn1.setText("start");
                }
                break;
            case R.id.btn2:
                if (!b2) {
                    b2 = true;
                    play2();
                    btn2.setText("stop");
                } else {
                    b2 = false;
                    btn2.setText("start");
                }
                break;
            case R.id.btn3:
                if (!b3) {
                    b3 = true;
                    play3();
                    btn3.setText("stop");
                } else {
                    b3 = false;
                    btn3.setText("start");
                }
                break;
        }
    }

    private void play1() {
        t1 = new Thread(new Runnable() {
            Random x = new Random();

            @Override
            public void run() {
                while (b1) {
                    try {
                        Message message = mHandler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putInt("1", x.nextInt(51) + 50);
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t1.start();
    }

    private void play2() {
        t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (b2) {
                    try {
                        Message message = mHandler.obtainMessage();
                        Bundle bundle = new Bundle();
                        if (start == 0) {
                            bundle.putInt("2", 1);
                            start = 1;
                        } else {
                            bundle.putInt("2", start += 2);
                        }
                        message.setData(bundle);
                        mHandler.sendMessage(message);

                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t2.start();
    }

    private void play3() {
        t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (b3) {
                    try {
                        Message message = mHandler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putInt("3", start2++);
                        message.setData(bundle);
                        mHandler.sendMessage(message);

                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t3.start();
    }
}