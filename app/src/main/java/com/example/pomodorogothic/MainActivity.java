package com.example.pomodorogothic;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pomodorogothic.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private TextView timerText;
    private Button  start_stop;

    private CountDownTimer timer;
    private boolean timerIsActive = false;
    private static final long TIMER_SET_MS = 1500000;
    private long timeRemaining = TIMER_SET_MS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        timerText = findViewById(R.id.timerText);
        start_stop = findViewById(R.id.start_stop);

        start_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerIsActive){
                    stopTimer();
                }
                else {
                    startTimer();
                }
            }
        });

        updateTimerText();
    }

    private void startTimer() {
        timer = new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onFinish() {
                timerIsActive = false;
                start_stop.setText(getString(R.string.start_button_text));
                timeRemaining = TIMER_SET_MS;
                updateTimerText();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                updateTimerText();
            }
        }.start();

        timerIsActive = true;
        start_stop.setText(getString(R.string.stop_button_text));
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timerIsActive = false;
        start_stop.setText(getString(R.string.start_button_text));
    }

    private void updateTimerText() {
        int minutes = (int) (timeRemaining / 1000) / 60;
        int seconds = (int) (timeRemaining / 1000) % 60;

        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerText.setText(formattedTime);

    }

}