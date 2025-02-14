package com.example.habitotracker;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewHabitActivity extends AppCompatActivity {
    private HabitDatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> habitList;
    private String selectedHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        dbHelper = new HabitDatabaseHelper(this);
        ListView habitListView = findViewById(R.id.habitListView);
        Button viewHabitDetailsButton = findViewById(R.id.viewHabitDetailsButton);
        Button deleteHabitButton = findViewById(R.id.deleteHabitButton);


        habitList = dbHelper.getAllHabits();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, habitList);
        habitListView.setAdapter(adapter);


        habitListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            selectedHabit = habitList.get(position);
            Toast.makeText(this, "Selected: " + selectedHabit, Toast.LENGTH_SHORT).show();
        });


        habitListView.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            String habitName = habitList.get(position);
            showDeleteConfirmation(habitName, position);
            return true;
        });

        viewHabitDetailsButton.setOnClickListener(v -> {
            if (selectedHabit != null) {
                String details = dbHelper.viewHabitDetails(selectedHabit);
                showHabitDetails(details);
            } else {
                Toast.makeText(this, "Please select a habit to view details.", Toast.LENGTH_SHORT).show();
            }
        });


        deleteHabitButton.setOnClickListener(v -> {
            if (selectedHabit != null) {
                int position = habitList.indexOf(selectedHabit);
                showDeleteConfirmation(selectedHabit, position);
            } else {
                Toast.makeText(this, "Please select a habit to delete.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmation(String habitName, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Habit")
                .setMessage("Are you sure you want to delete this habit?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dbHelper.deleteHabit(habitName);
                    habitList.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Habit deleted", Toast.LENGTH_SHORT).show();
                    selectedHabit = null;
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showHabitDetails(String details) {
        new AlertDialog.Builder(this)
                .setTitle("Habit Details")
                .setMessage(details)
                .setPositiveButton("OK", null)
                .show();
    }
}
