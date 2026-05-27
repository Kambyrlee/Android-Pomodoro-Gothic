package com.example.pomodorogothic.ui.home;

import static android.icu.text.ListFormatter.Type.OR;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodorogothic.R;
import com.example.pomodorogothic.databinding.FragmentHomeBinding;
import com.example.pomodorogothic.ui.TimerViewModel;
import com.example.pomodorogothic.ui.database.Repository;
import com.example.pomodorogothic.ui.database.Session;
import com.example.pomodorogothic.ui.database.SessionDAO;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TimerViewModel timerViewModel;
    private SharedPreferences sharedPreferences;

    private Repository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences("appPrefs", Context.MODE_PRIVATE);

        repository = new Repository(requireActivity().getApplication());

        timerViewModel = new ViewModelProvider(requireActivity()).get(TimerViewModel.class);

        timerViewModel.getTimeRemaining().observe(getViewLifecycleOwner(), time -> {
            binding.timerText.setText(time);
        });

        timerViewModel.getTimerIsActive().observe(getViewLifecycleOwner(), running -> {
            if (running) {
                binding.startStop.setText(R.string.stop_button_text);
            } else {
                binding.startStop.setText(R.string.start_button_text);
            }
            updateRoundsText();
        });

        timerViewModel.getCurrentState().observe(getViewLifecycleOwner(), state -> {
            updateUiForState(state);
            updateRoundsText();
        });

        timerViewModel.getRoundCount().observe(getViewLifecycleOwner(), count -> {
            binding.roundsText.setText(getString(R.string.round_display_format, count + 1));
        });
        binding.startStop.setOnClickListener(v ->  {
            if (timerViewModel.timerIsUnstarted()) {
                AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.DialogTheme)
                        .setTitle(R.string.session_prompt_title)
                        .setMessage(R.string.session_prompt_msg)
                        .setPositiveButton(R.string.session_prompt_yes, (d, which) -> {
                            LocalDateTime timestamp = LocalDateTime.now();
                            int roundsCompleted = 0;
                            int workDuration = sharedPreferences.getInt("work_duration", 25);
                            int shortRestDuration = sharedPreferences.getInt("rest_duration", 5);
                            int longRestDuration = sharedPreferences.getInt("long_rest_duration", 15);
                            String sessionNotes = "";

                            Session session = new Session(timestamp, roundsCompleted, workDuration, shortRestDuration,longRestDuration, sessionNotes);
                            repository.insertSession(session, id -> {

                            });
                        })
                        .setNegativeButton(R.string.session_prompt_no, null)
                        .create();
                        dialog.show();
            }
            timerViewModel.toggleTimer();
        });
        binding.reset.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.DialogTheme)
                    .setTitle(R.string.reset_confirm_title)
                    .setMessage(R.string.reset_confirm_msg)
                    .setPositiveButton(R.string.reset_confirm_yes, (d, which) -> {
                        timerViewModel.resetTimer();
                        updateRoundsText();
                    })
                    .setNegativeButton(R.string.reset_confirm_no, null)
                    .create();

            dialog.show();

            // Forcibly bypass themes because they aren't working as expected.

            TextView messageView = dialog.findViewById(android.R.id.message);
            if (messageView != null) {
                messageView.setTextColor(getResources().getColor(R.color.lt_accent_1, null));
                messageView.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.body_font));
            }
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            if (positiveButton != null) {
                positiveButton.setTextColor(getResources().getColor(R.color.lt_accent_2, null));
                positiveButton.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.body_font));
            }
            if (negativeButton != null) {
                negativeButton.setTextColor(getResources().getColor(R.color.lt_accent_2, null));
                negativeButton.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.body_font));
            }
        });
    }

    private void updateUiForState(TimerViewModel.PomodoroState state) {
        switch (state) {
            case SHORT_BREAK:
                binding.currentStateLabel.setText(R.string.state_short_break);
                break;
            case LONG_BREAK:
                binding.currentStateLabel.setText(R.string.state_long_break);
                break;
            case WORK:
            default:
                binding.currentStateLabel.setText(R.string.state_work);
                break;
        }
    }
    private void updateRoundsText(){
        Boolean isActive = timerViewModel.getTimerIsActive().getValue();
        TimerViewModel.PomodoroState state = timerViewModel.getCurrentState().getValue();

        if (Boolean.TRUE.equals(isActive)) {
            if (state == TimerViewModel.PomodoroState.WORK) {
                binding.roundsText.setVisibility(View.VISIBLE);
            } else {
                binding.roundsText.setVisibility(View.INVISIBLE);
            }
        } else {
            if (timerViewModel.timerIsUnstarted()) {
                binding.roundsText.setVisibility(View.INVISIBLE);
            } else {
                binding.roundsText.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}