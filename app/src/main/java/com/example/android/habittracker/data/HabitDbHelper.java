package com.example.android.habittracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kezia on 15/07/2017.
 */

public class HabitDbHelper  extends SQLiteOpenHelper{


        public static final String LOG_TAG = HabitDbHelper.class.getSimpleName();

        /** Name of the database file */
        private static final String DATABASE_NAME = "habits.db";

        /**
         * Database version. If you change the database schema, you must increment the database version.
         */
        private static final int DATABASE_VERSION = 1;

        /**
         * Constructs a new instance of {@link HabitDbHelper}.
         *
         * @param context of the app
         */
        public HabitDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * This is called when the database is created for the first time.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create a String that contains the SQL statement to create the habits table
            String SQL_CREATE_HABITS_TABLE =  "CREATE TABLE " + HabitContract.HabitEntry.TABLE_NAME + " ("
                    + HabitContract.HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + HabitContract.HabitEntry.COLUMN_RUNNING + " TEXT NOT NULL, "
                    + HabitContract.HabitEntry.COLUMN_GYM + " TEXT, "
                    + HabitContract.HabitEntry.COLUMN_WALKING + " INTEGER NOT NULL,);";

            // Execute the SQL statement
            db.execSQL(SQL_CREATE_HABITS_TABLE);
        }

        /**
         * This is called when the database needs to be upgraded.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // The database is still at version 1, so there's nothing to do be done here.
        }
}
