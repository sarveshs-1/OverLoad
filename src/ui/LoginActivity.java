package com.example.gymprogressiontracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * LoginActivity is the initial screen that users see when opening the app.
 * It will eventually handle user authentication.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if name is already saved
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedName = preferences.getString("userName", null);

        if (savedName != null) {
            startMainActivity();
            return;
        }

        setContentView(R.layout.activity_login);

        EditText etName = findViewById(R.id.etName);
        Button btnLogin = findViewById(R.id.btnLogin);

        // Simple bypass for now: clicking "Login" takes you to the main screen
        btnLogin.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save name to SharedPreferences
            preferences.edit().putString("userName", name).apply();

            startMainActivity();
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close LoginActivity so user can't go back to it with the back button
    }
}
