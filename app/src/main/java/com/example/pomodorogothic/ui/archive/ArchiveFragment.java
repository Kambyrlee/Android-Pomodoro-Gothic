package com.example.pomodorogothic.ui.archive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodorogothic.databinding.FragmentArchiveBinding;
import com.example.pomodorogothic.ui.database.Repository;

public class ArchiveFragment extends Fragment {
    private Repository repository;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new Repository(requireActivity().getApplication());

        repository.getAllSessions().observe(getViewLifecycleOwner(), sessions -> {
            if (sessions != null) {
                //update UI
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}