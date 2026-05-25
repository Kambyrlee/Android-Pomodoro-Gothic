package com.example.pomodorogothic.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodorogothic.R;
import com.example.pomodorogothic.databinding.FragmentHomeBinding;
import com.example.pomodorogothic.ui.TimerViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TimerViewModel timerViewModel;

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
        });

        timerViewModel.getCurrentState().observe(getViewLifecycleOwner(), state -> {
            updateUiForState(state);
        });

        timerViewModel.getRoundCount().observe(getViewLifecycleOwner(), count -> {
            if (count == 0) {
                binding.roundsText.setText("");
            } else {
                binding.roundsText.setText(getString(R.string.round_display_format, count));
            }
        });
        binding.startStop.setOnClickListener(v ->  {
            timerViewModel.toggleTimer();
        });
    }

    private void updateUiForState(TimerViewModel.PomodoroState state) {
        switch (state) {
            case WORK:
            case SHORT_BREAK:
                binding.currentStateLabel.setText(R.string.state_short_break);
                break;
            case LONG_BREAK:
                binding.currentStateLabel.setText(R.string.state_long_break);
                break;
            default:
                binding.currentStateLabel.setText(R.string.state_work);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}