package com.example.android.habittracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.habittracker.data.HabitContract.HabitEntry;

/**
 * Created by Kezia on 18/07/2017.
 */

/**
 * {@link ContentProvider} for Pets app.
 */

public class HabitProvider extends ContentProvider {

    /**
     * URI matcher code for the content URI for the habits table
     */
    private static final int HABIT = 100;

    /**
     * URI matcher code for the content URI for a single pet in the habits table
     */
    private static final int HABIT_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.habittracker/habits" will map to the
        // integer code {@link #PETS}. This URI is used to provide access to MULTIPLE rows
        // of the pets table.
        sUriMatcher.addURI(HabitContract.CONTENT_AUTHORITY, HabitContract.PATH_HABITS, HABIT);

        // The content URI of the form "content://com.example.android.pets/pets/#" will map to the
        // integer code {@link #PET_ID}. This URI is used to provide access to ONE single row
        // of the pets table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.pets/pets/3" matches, but
        // "content://com.example.android.pets/pets" (without a number at the end) doesn't match.
        sUriMatcher.addURI(HabitContract.CONTENT_AUTHORITY, HabitContract.PATH_HABITS + "/#", HABIT_ID);
    }


    //Database helper object
    private HabitDbHelper mDbHelper;
    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = HabitProvider.class.getSimpleName();

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        mDbHelper = new HabitDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case HABIT:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                // TODO: Perform database query on pets table

                cursor = database.query(HabitEntry.TABLE_NAME, projection, selection, selectionArgs, null, null,
                        sortOrder);


                break;
            case HABIT_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = HabitEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(HabitEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case HABIT:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertPet(Uri uri, ContentValues values) {

//        // Check that the gym activity is not null
//        String name = values.getAsString(HabitEntry.COLUMN_GYM);
//        if (name == null) {
//            throw new IllegalArgumentException("Gym requires a name");
//        }
//
        Integer run = values.getAsInteger(HabitEntry.COLUMN_RUNNING);
        if (run != null && run < 0) {
            throw new IllegalArgumentException("Pet requires valid gender");
        }
        // If the walk is provided, check that it's greater than or equal to 0 kg
        Integer walk = values.getAsInteger(HabitEntry.COLUMN_WALKING);
        if (walk != null && walk < 0) {
            throw new IllegalArgumentException("Pet requires valid walk");
        }

        // TODO: Insert a new pet into the pets database table with the given ContentValues
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(HabitEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */


    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case HABIT:
                return updateHabit(uri, contentValues, selection, selectionArgs);
            case HABIT_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = HabitEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateHabit(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updateHabit(Uri uri, ContentValues values, String selection, String[] selectionArgs) {


        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(HabitEntry.COLUMN_GYM)) {
            String name = values.getAsString(HabitEntry.COLUMN_GYM);
            if (name == null) {
                throw new IllegalArgumentException("Gym requires a name");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(HabitEntry.COLUMN_RUNNING)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer weight = values.getAsInteger(HabitEntry.COLUMN_RUNNING);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Run is required");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(HabitEntry.COLUMN_WALKING)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer weight = values.getAsInteger(HabitEntry.COLUMN_WALKING);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Walk is required");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return database.update(HabitEntry.TABLE_NAME, values, selection, selectionArgs);
    }


    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case HABIT:
                // Delete all rows that match the selection and selection args
                return database.delete(HabitEntry.TABLE_NAME, selection, selectionArgs);
            case HABIT_ID:
                // Delete a single row given by the ID in the URI
                selection = HabitEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(HabitEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case HABIT:
                return HabitEntry.CONTENT_LIST_TYPE;
            case HABIT_ID:
                return HabitEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }


}
