package com.example.habitotracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddHabitActivity extends AppCompatActivity {
    private static final int MAX_HABITS = 3;
    private ListView habitListView;
    private ArrayList<String> selectedHabits = new ArrayList<>();
    private final String[] habits = {
            "Exercise", "Meditation", "Reading", "Journaling", "Healthy Eating",
            "Drinking 2L of Water", "Sleeping by 10pm", "Learning to Code"
    };
    private HabitDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit); // Only call once

        habitListView = findViewById(R.id.habitListView);
        Button enterFocusModeButton = findViewById(R.id.enterFocusModeButton);
        dbHelper = new HabitDatabaseHelper(this);

        // Debugging log
        ArrayList<String> savedHabits = dbHelper.getAllHabits();
        Log.d("HABITS", "Saved habits: " + savedHabits.toString());

        // Setup ListView adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, habits);
        habitListView.setAdapter(adapter);
        habitListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Restore previously saved habits
        for (int i = 0; i < habits.length; i++) {
            if (savedHabits.contains(habits[i])) {
                habitListView.setItemChecked(i, true);
                selectedHabits.add(habits[i]);
            }
        }

        // Handle ListView item selection
        habitListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedHabit = habits[position];
            if (selectedHabits.contains(selectedHabit)) {
                selectedHabits.remove(selectedHabit);
            } else if (selectedHabits.size() < MAX_HABITS) {
                selectedHabits.add(selectedHabit);
            } else {
                Toast.makeText(this, "You can only select up to " + MAX_HABITS + " habits.", Toast.LENGTH_SHORT).show();
            }

            if (selectedHabits.size() >= MAX_HABITS) {
                disableUnselectedItems();
            } else {
                enableAllItems();
            }
        });

        // Add Habit Button
        findViewById(R.id.addHabitButton).setOnClickListener(v -> {
            if (selectedHabits.isEmpty()) {
                Toast.makeText(this, "Please select at least one habit.", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.clearHabits();
            for (String habit : selectedHabits) {
                dbHelper.addHabit(habit);
            }

            Toast.makeText(this, "Habits saved successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AddHabitActivity.this, ViewHabitActivity.class);
            startActivity(intent);
            finish();
        });

        // Enter Focus Mode Button
        enterFocusModeButton.setOnClickListener(v -> {
            Toast.makeText(this, "Entering Focus Mode...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddHabitActivity.this, FocusModeActivity.class);
            startActivity(intent);
        });
    }

    private void disableUnselectedItems() {
        for (int i = 0; i < habitListView.getCount(); i++) {
            if (!selectedHabits.contains(habits[i])) {
                habitListView.setItemChecked(i, false);
                if (habitListView.getChildAt(i) != null) {
                    habitListView.getChildAt(i).setEnabled(false);
                }
            }
        }
    }

    private void enableAllItems() {
        for (int i = 0; i < habitListView.getCount(); i++) {
            if (habitListView.getChildAt(i) != null) {
                habitListView.getChildAt(i).setEnabled(true);
            }
        }
    }
}
