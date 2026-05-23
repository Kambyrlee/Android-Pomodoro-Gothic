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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
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
        binding.startStop.setOnClickListener(v ->  {
            timerViewModel.toggleTimer();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}