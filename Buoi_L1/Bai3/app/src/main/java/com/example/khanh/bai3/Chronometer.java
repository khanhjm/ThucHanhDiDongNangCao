package com.example.khanh.bai3;

import android.content.Context;

public class Chronometer implements Runnable {

    public static final long MILLIS_TO_MINUTES = 60000;
    public static final long MILLIS_TO_HOURS = 3600000;

    private Context mContext;
    private long mStartTime, mStopTime;
    private boolean mIsRunning;
    private long mSpace = 0;

    public Chronometer(Context mContext) {
        this.mContext = mContext;
    }

    public void start() {
        mStartTime = System.currentTimeMillis();
        mIsRunning = true;
    }

    public void stop() {
        mStopTime = System.currentTimeMillis();
        mIsRunning = false;
    }

    public void resume() {
        mSpace += System.currentTimeMillis() - mStopTime;
        mIsRunning = true;
    }

    @Override
    public void run() {
        while (mIsRunning) {
            long since = System.currentTimeMillis() - mStartTime - mSpace;
            int seconds = (int) ((since / 1000) % 60);
            int minutes = (int) ((since / MILLIS_TO_MINUTES) % 60);
            int hours = (int) ((since / MILLIS_TO_HOURS) % 24);
            int millis = (int) since % 1000;
            ((MainActivity) mContext).updateTimerText(String.format(
                    "%02d:%02d:%02d:%03d", hours, minutes, seconds, millis
            ));
        }
    }
}
