package com.arodi.surveyapp.database;

import android.content.Context;

import androidx.room.Room;

import com.arodi.surveyapp.database.AppDatabase;

public class DatabaseClient {
    private static DatabaseClient mInstance;

    private AppDatabase database;

    private DatabaseClient(Context mCtx) {
        database = Room
                .databaseBuilder(mCtx, AppDatabase.class, "questions")
                .allowMainThreadQueries()
                .build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
