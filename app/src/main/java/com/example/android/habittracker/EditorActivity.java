package com.example.android.habittracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.habittracker.data.HabitContract;
import com.example.android.habittracker.data.HabitDbHelper;

public class EditorActivity extends AppCompatActivity {

    /** EditText field to enter the run distance in miles */
    private EditText mRunningEditText;

    /** EditText field to enter the type of exercise done in gym, wights, push-ups etc */
    private EditText mGymEditText;

    /** EditText field to enter the walk distance in miles */
    private EditText mWalkingEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mRunningEditText = (EditText) findViewById(R.id.edit_run);
        mGymEditText = (EditText) findViewById(R.id.edit_gym);
        mWalkingEditText = (EditText) findViewById(R.id.edit_walk);

    }

    /**
     * Get user input from editor and save new habit into database.
     */
    private void insertHabit() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space

        //Input length of run in miles
        String runString = mRunningEditText.getText().toString().trim();
        int runLength = Integer.parseInt(runString);

         //Input type of exercise done e.g. weights, pull-ups, press ups etc
        String gymString = mGymEditText.getText().toString().trim();

        //Input length of walk
        String walkString = mWalkingEditText.getText().toString().trim();
        int walkLength = Integer.parseInt(walkString);

        // Create database helper
        HabitDbHelper mDbHelper = new HabitDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_RUNNING, runLength);
        values.put(HabitContract.HabitEntry.COLUMN_GYM, gymString);
        values.put(HabitContract.HabitEntry.COLUMN_WALKING, walkLength);


        // Insert a new row for habit in the database, returning the ID of that new row.
        long newRowId = db.insert(HabitContract.HabitEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving habir", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                insertHabit();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
