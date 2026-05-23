package com.example.pomodorogothic.ui.archive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodorogothic.databinding.FragmentArchiveBinding;

public class ArchiveFragment extends Fragment {

    private FragmentArchiveBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ArchiveViewModel archiveViewModel =
                new ViewModelProvider(this).get(ArchiveViewModel.class);

        binding = FragmentArchiveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textArchive;
        archiveViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}