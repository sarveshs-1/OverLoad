package com.example.gymprogressiontracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddWorkoutFragment extends Fragment {

    private Spinner spinnerDays;
    private EditText etExercise, etWeight, etReps, etSets;
    private TextView tvGreeting;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_workout, container, false);

        spinnerDays = view.findViewById(R.id.spinnerDays);
        etExercise = view.findViewById(R.id.etExerciseName);
        etWeight = view.findViewById(R.id.etWeight);
        etReps = view.findViewById(R.id.etReps);
        etSets = view.findViewById(R.id.etSets);
        tvGreeting = view.findViewById(R.id.tvGreeting);
        Button btnSave = view.findViewById(R.id.btnSave);

        db = AppDatabase.getInstance(getContext());

        displayGreeting();

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDays.setAdapter(adapter);

        btnSave.setOnClickListener(v -> saveToDatabase());

        return view;
    }

    private void displayGreeting() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String name = preferences.getString("userName", "User");
        tvGreeting.setText("Hello, " + name + "!");
    }

    private void saveToDatabase() {
        String day = spinnerDays.getSelectedItem().toString();
        String name = etExercise.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();
        String repsStr = etReps.getText().toString().trim();
        String setsStr = etSets.getText().toString().trim();

        if (name.isEmpty() || weightStr.isEmpty() || repsStr.isEmpty() || setsStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all boxes!", Toast.LENGTH_SHORT).show();
            return;
        }

        WorkoutSet newSet = new WorkoutSet(day, name, Integer.parseInt(weightStr), Integer.parseInt(repsStr), Integer.parseInt(setsStr), null);
        db.workoutDao().insertWorkout(newSet);
        
        Toast.makeText(getContext(), "Logged!", Toast.LENGTH_SHORT).show();

        etExercise.setText("");
        etWeight.setText("");
        etReps.setText("");
        etSets.setText("");
    }
}
