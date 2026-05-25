package com.example.pomodorogothic.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.pomodorogothic.databinding.FragmentSettingsBinding;
import com.example.pomodorogothic.ui.TimerViewModel;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String[] attributions = getResources().getStringArray(R.array.icon_attributions);
        StringBuilder builder = new StringBuilder();

        for (String detail : attributions) {
            builder.append(detail).append("\n\n");
        }

        binding.iconAttributions.setText(builder.toString());

        final TextView textView = binding.textSettings;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
        binding.workDurationInput.setText(String.valueOf(sharedPreferences.getInt("work_duration", 25)));
        binding.restDurationInput.setText(String.valueOf(sharedPreferences.getInt("rest_duration", 5)));
        binding.longRestDurationInput.setText(String.valueOf(sharedPreferences.getInt("long_rest_duration", 15)));
        binding.wiprInput.setText(String.valueOf(sharedPreferences.getInt("wipr", 4)));

        TimerViewModel timerViewModel = new ViewModelProvider(requireActivity()).get(TimerViewModel.class);

        binding.saveSettings.setOnClickListener(v -> {
            saveSettings();
            timerViewModel.refreshSettings();
            com.google.android.material.snackbar.Snackbar.make(view, R.string.save_confirmation_msg, com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        saveSettings();
    }

    private void saveSettings(){
        if (binding == null) return;

        SharedPreferences.Editor editor = sharedPreferences.edit();

        String workStr = binding.workDurationInput.getText().toString();
        String restStr = binding.restDurationInput.getText().toString();
        String longRestStr = binding.longRestDurationInput.getText().toString();
        String wiprStr = binding.wiprInput.getText().toString();

        int workMin = workStr.isEmpty() ? 25 : Integer.parseInt(workStr);
        int restMin = restStr.isEmpty() ? 5 : Integer.parseInt(restStr);
        int longRestMin = longRestStr.isEmpty() ? 15 : Integer.parseInt(longRestStr);
        int wiprCount = wiprStr.isEmpty() ? 4 : Integer.parseInt(wiprStr);

        editor.putInt("work_duration", workMin);
        editor.putInt("rest_duration", restMin);
        editor.putInt("long_rest_duration", longRestMin);
        editor.putInt("wipr", wiprCount);

        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}