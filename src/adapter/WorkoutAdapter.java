package com.example.gymprogressiontracker;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

/**
 * WorkoutAdapter handles displaying a list of WorkoutSet objects in a RecyclerView.
 */
public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private List<WorkoutSet> workoutList;
    private OnItemChangedListener listener;

    public interface OnItemChangedListener {
        void onItemChanged();
    }

    // Constructor to pass the data list to the adapter
    public WorkoutAdapter(List<WorkoutSet> workoutList, OnItemChangedListener listener) {
        this.workoutList = workoutList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate (create) our custom row layout (item_workout_row.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_row, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        // Get the specific workout set for this position in the list
        WorkoutSet currentSet = workoutList.get(position);

        // Bind (set) the data to the UI elements in the row
        holder.tvExerciseName.setText(currentSet.exerciseName);
        holder.tvDetails.setText(currentSet.sets + " sets × " + currentSet.reps + " reps @ " + currentSet.weight + " lbs");

        // Set the checkbox state based on the data
        holder.cbComplete.setChecked(currentSet.isCompleted());

        // Display timestamp
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy - hh:mm a", java.util.Locale.getDefault());
        java.util.Date date = new java.util.Date(currentSet.getTimestamp());
        holder.tvDateStamp.setText(sdf.format(date));

        // Handle item click for editing
        holder.itemView.setOnClickListener(v -> showEditDialog(v, currentSet));

        // Handle when a user taps the checkbox
        holder.cbComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = holder.cbComplete.isChecked();
                currentSet.setCompleted(isChecked);

                if (isChecked) {
                    // Logic for progression: if target reps (e.g. 10) are reached, suggest a weight increase
                    if (currentSet.reps >= 10) {
                        int newWeight = currentSet.weight + 5;
                        String message = "Target reached! Weight increased to " + newWeight + " lbs for your next session.";
                        Toast.makeText(v.getContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
                
                // Update in database (background thread)
                new Thread(() -> {
                    AppDatabase.getInstance(v.getContext()).workoutDao().updateWorkout(currentSet);
                }).start();
            }
        });

        // Handle delete button
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                .setTitle("Delete Workout")
                .setMessage("Are you sure you want to remove this workout?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    new Thread(() -> {
                        AppDatabase.getInstance(v.getContext()).workoutDao().deleteWorkout(currentSet);
                        if (listener != null) {
                            v.post(() -> listener.onItemChanged());
                        }
                    }).start();
                })
                .setNegativeButton("Cancel", null)
                .show();
        });
    }

    private void showEditDialog(View v, WorkoutSet set) {
        BottomSheetDialog dialog = new BottomSheetDialog(v.getContext());
        View popupView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_exercise_details, null);
        dialog.setContentView(popupView);

        TextView tvName = popupView.findViewById(R.id.popupExerciseName);
        TextView tvDesc = popupView.findViewById(R.id.popupDescription);
        Spinner spinnerDay = popupView.findViewById(R.id.popupSpinnerDay);
        TextInputEditText etWeight = popupView.findViewById(R.id.popupEtWeight);
        TextInputEditText etReps = popupView.findViewById(R.id.popupEtReps);
        TextInputEditText etSets = popupView.findViewById(R.id.popupEtSets);
        android.widget.Button btnSave = popupView.findViewById(R.id.btnPopupLog);
        
        btnSave.setText("Update Workout");

        tvName.setText(set.exerciseName);
        tvDesc.setText("Modify your session details below.");
        etWeight.setText(String.valueOf(set.weight));
        etReps.setText(String.valueOf(set.reps));
        etSets.setText(String.valueOf(set.sets));

        // Setup Spinner for Days
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(dayAdapter);
        
        // Set current day
        for (int i = 0; i < days.length; i++) {
            if (days[i].equalsIgnoreCase(set.dayOfWeek)) {
                spinnerDay.setSelection(i);
                break;
            }
        }

        btnSave.setOnClickListener(view -> {
            set.dayOfWeek = spinnerDay.getSelectedItem().toString();
            set.weight = Integer.parseInt(etWeight.getText().toString());
            set.reps = Integer.parseInt(etReps.getText().toString());
            set.sets = Integer.parseInt(etSets.getText().toString());

            new Thread(() -> {
                AppDatabase.getInstance(v.getContext()).workoutDao().updateWorkout(set);
                if (listener != null) {
                    v.post(() -> {
                        listener.onItemChanged();
                        dialog.dismiss();
                    });
                }
            }).start();
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return workoutList.size(); // Total number of items to show
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView tvExerciseName;
        TextView tvDetails;
        TextView tvDateStamp;
        CheckBox cbComplete;
        ImageButton btnDelete;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvDetails = itemView.findViewById(R.id.tvWorkoutDetails);
            tvDateStamp = itemView.findViewById(R.id.tvDateStamp);
            cbComplete = itemView.findViewById(R.id.cbComplete);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
