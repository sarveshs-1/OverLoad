package com.example.gymprogressiontracker;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TodayFragment extends Fragment {

    private RecyclerView rvWorkoutList;
    private TextView tvGreeting;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        rvWorkoutList = view.findViewById(R.id.rvWorkoutList);
        tvGreeting = view.findViewById(R.id.tvGreeting);
        rvWorkoutList.setLayoutManager(new LinearLayoutManager(getContext()));
        
        db = AppDatabase.getInstance(getContext());
        
        displayGreeting();
        refreshHistory();

        return view;
    }

    private void displayGreeting() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String name = preferences.getString("userName", "User");
        tvGreeting.setText("Hello, " + name + "!");
    }

    private void refreshHistory() {
        // Get today's day of the week
        Calendar calendar = Calendar.getInstance();
        String today = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        
        List<WorkoutSet> sets = db.workoutDao().getWorkoutsForDay(today);
        WorkoutAdapter adapter = new WorkoutAdapter(sets);
        rvWorkoutList.setAdapter(adapter);
    }
}
