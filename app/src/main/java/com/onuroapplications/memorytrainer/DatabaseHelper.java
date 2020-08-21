package com.onuroapplications.memorytrainer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MemoryTrainer.db";
    public static final String TABLE_NAME = "Points_Table";
    public static final String COLUMN_1 = "SESSION";
    public static final String COLUMN_2 = "N_BACK";
    public static final String COLUMN_3 = "SEQUENCE";
    public static final String COLUMN_4 = "SEQUENCE_BACKWARDS";
    public static final String COLUMN_5 = "RUNNING_MEMORY_SPAN";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase(); //uses onCreate and creates table
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (COLUMN_1 INTEGER PRIMARY KEY AUTOINCREMENT, COLUMN_2 TEXT, COLUMN_3 TEXT, COLUMN_4 TEXT, COLUMN_5 TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
