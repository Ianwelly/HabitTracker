package com.example.android.habittracker.data;

import android.provider.BaseColumns;

/**
 * Created by Kezia on 15/07/2017.
 */

public final class HabitContract {

    private HabitContract(){}

    /**
     * Inner class that defines constant values for the habit database table.
     * Each entry in the table represents a single habit.
     */
    public static final class HabitEntry implements BaseColumns {

        /** Name of database table for habits*/
        public final static String TABLE_NAME = "habits";

        /**
         * Unique ID number for the habit (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Type of habit.
         *
         * Type: TEXT
         */
        public final static String COLUMN_RUNNING = "running";

        /**
         * Type of habit.
         *
         * Type: TEXT
         */
        public final static String COLUMN_GYM = "gym";

        /**
         * Type of habit.
         *
         * Type:TEXT
         */
        public final static String COLUMN_WALKING = "walking";

    }
}


