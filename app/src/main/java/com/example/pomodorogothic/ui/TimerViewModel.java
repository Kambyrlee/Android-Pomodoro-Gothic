package com.example.pomodorogothic.ui;

import android.os.CountDownTimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;

public class TimerViewModel extends ViewModel {
    private final MutableLiveData<String> timeRemaining = new MutableLiveData<>();
    private final MutableLiveData<Boolean> timerIsActive = new MutableLiveData<>(false);
    private CountDownTimer timer;

    public LiveData<String> getTimeRemaining(){
        return timeRemaining;
    }

    public LiveData<Boolean> getTimerIsActive(){
        return timerIsActive;
    }

    public void startTimer(long duration) {
        if (timer != null) timer.cancel();

        timer = new CountDownTimer(duration, 1000) {
            @Override
            public void onFinish() {
                timerIsActive.setValue(false);
                timeRemaining.setValue("00:00");
            }

            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                timeRemaining.setValue(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));

            }
        }.start();

    }

    public void toggleTimer() {
        if (Boolean.TRUE.equals(timerIsActive.getValue())) {
            if (timer != null) timer.cancel();
            timerIsActive.setValue(false);
        } else {
            timerIsActive.setValue(true);
            startTimer(1500000);
        }
    }
}
