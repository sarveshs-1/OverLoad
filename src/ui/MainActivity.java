package com.example.gymprogressiontracker;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * MainActivity manages the navigation between different fragments (Today's list and Add Workout).
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Set the "Today" screen as the default view when the app first launches
        if (savedInstanceState == null) {
            loadFragment(new TodayFragment());
        }

        // Handle tab clicks to switch between fragments
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_today) {
                selectedFragment = new TodayFragment();
            } else if (item.getItemId() == R.id.nav_add) {
                selectedFragment = new AddWorkoutFragment();
            }

            return loadFragment(selectedFragment);
        });
    }

    /**
     * Replaces the current fragment in the container with the new one.
     */
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
