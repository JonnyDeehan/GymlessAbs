package com.jdblogs.gymlessabs.utilities;

import android.os.CountDownTimer;

/**
 * Created by jonathandeehan on 24/06/2017.
 */

public class WorkoutCountDownTimer extends CountDownTimer {

    public WorkoutCountDownTimer(long millisInFuture, long countDownInterval){
        super(millisInFuture,countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int currentTimeLeft = (int) (millisUntilFinished/1000);

    }

    @Override
    public void onFinish() {

    }
}
