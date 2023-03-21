package com.gomtechsolutions.attendance.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gomtechsolutions.attendance.R;
import com.gomtechsolutions.attendance.ReportsAdapter;
import com.gomtechsolutions.attendance.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {
    RecyclerView recyclerView;
    List<String> dates,numberOfStudents;
    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.rv);
        dates = new ArrayList<String>();
        numberOfStudents = new ArrayList<String>();
        dates.add("01-12-1010");
        dates.add("01-12-1010");
        numberOfStudents.add("20");
        numberOfStudents.add("50");
        setUpView(dates,numberOfStudents);
        return root;
    }
    private void setUpView(List<String> dates, List<String> numberOfStudents) {
        ReportsAdapter adapter = new ReportsAdapter(getContext(), dates, numberOfStudents);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}