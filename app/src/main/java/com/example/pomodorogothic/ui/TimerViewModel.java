package com.example.pomodorogothic.ui;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;

public class TimerViewModel extends AndroidViewModel {
    private final MutableLiveData<PomodoroState> currentState = new MutableLiveData<>(PomodoroState.WORK);
    private final MutableLiveData<Integer> roundCount = new MutableLiveData<>(0);
    public enum PomodoroState {WORK, SHORT_BREAK, LONG_BREAK}
    private final MutableLiveData<String> timeRemaining = new MutableLiveData<>();
    private final MutableLiveData<Boolean> timerIsActive = new MutableLiveData<>(false);
    private CountDownTimer timer;
    private long currentMillisLeft = 0;
    private final SharedPreferences sharedPreferences;

    public TimerViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
        resetTimerToDefault();
    }

    public LiveData<String> getTimeRemaining() { return timeRemaining; }
    public LiveData<Boolean> getTimerIsActive() { return timerIsActive; }
    public LiveData<PomodoroState> getCurrentState() { return currentState; }
    public LiveData<Integer> getRoundCount() { return roundCount; }

    public void startTimer(long duration) {
        if (timer != null) timer.cancel();

        timerIsActive.setValue(true);

        timer = new CountDownTimer(duration, 1000) {
            @Override
            public void onFinish() {
                timerIsActive.setValue(false);
                currentMillisLeft = 0;
                handleStateTransition();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                currentMillisLeft = millisUntilFinished;
                updateTimerText(millisUntilFinished);

            }
        }.start();

    }
    private void pauseTimer() {
        if (timer != null) timer.cancel();
        timerIsActive.setValue(false);
    }

    public void resetTimer() {
        pauseTimer();
        currentMillisLeft = 0;
        roundCount.setValue(0);
        currentState.setValue(PomodoroState.WORK);
        resetTimerToDefault();
    }

    private void handleStateTransition() {
        PomodoroState current = currentState.getValue();
        int roundsBeforeLong = sharedPreferences.getInt("wipr", 4);
        int currentRounds = (roundCount.getValue() != null) ? roundCount.getValue() : 0;

        if (current == PomodoroState.WORK) {
            currentRounds++;
            roundCount.setValue(currentRounds);

            if (currentRounds % roundsBeforeLong == 0) {
                currentState.setValue(PomodoroState.LONG_BREAK);
            } else {
                currentState.setValue(PomodoroState.SHORT_BREAK);
            }
        } else {
            currentState.setValue(PomodoroState.WORK);
        }
        currentMillisLeft = getDurationForCurrentState();
        updateTimerText(currentMillisLeft);

        startTimer(currentMillisLeft);
    }

    private long getDurationForCurrentState() {
        int minutes = 0;
        switch (currentState.getValue()) {
            case WORK:
                minutes = sharedPreferences.getInt("work_duration", 25);
                break;
            case SHORT_BREAK:
                minutes = sharedPreferences.getInt("rest_duration", 5);
                break;
            case LONG_BREAK:
                minutes = sharedPreferences.getInt("long_rest_duration", 15);
                break;
        }
        return (long) minutes * 60 * 1000;
    }

    private void resetTimerToDefault() {
        currentMillisLeft = getDurationForCurrentState();
        updateTimerText(currentMillisLeft);
    }

    private void updateTimerText(long millis) {
        long minutes = (millis / 1000) / 60;
        long seconds = (millis / 1000) % 60;
        timeRemaining.setValue(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }

    public void toggleTimer() {
        if (Boolean.TRUE.equals(timerIsActive.getValue())) {
            pauseTimer();
        } else {
            if (currentMillisLeft <= 0) {
                currentMillisLeft = getDurationForCurrentState();
            }
            startTimer(currentMillisLeft);
        }
    }

    public void refreshSettings(){
        if (Boolean.FALSE.equals(timerIsActive.getValue())) {
            currentMillisLeft = getDurationForCurrentState();
            updateTimerText(currentMillisLeft);
        }
    }
    public boolean timerIsUnstarted() {
        boolean isNotRunning = !Boolean.TRUE.equals(timerIsActive.getValue());
        boolean isAtStart = (currentMillisLeft == getDurationForCurrentState());

        return isNotRunning && isAtStart;
    }
}
