package com.tiansirk.popularmovies.data;

import android.content.Context;
import android.util.Log;

import com.tiansirk.popularmovies.util.DateConverter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {FavoriteMovie.class, Review.class, VideoKey.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favoritemovies";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context) {
        if(sInstance == null) {
            synchronized (LOCK) {
                if(sInstance == null) {
                    Log.d(TAG, "Instantiating a new database");
                    sInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            AppDatabase.DATABASE_NAME)
                            .build();
                }
            }
        }
        Log.d(TAG, "Returning the database instance already available");
        return sInstance;
    }

    public abstract MovieDAO movieDAO();

}
