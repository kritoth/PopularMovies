package com.tiansirk.popularmovies.model;

import android.app.Application;

import com.tiansirk.popularmovies.data.AppDatabase;
import com.tiansirk.popularmovies.data.Movie;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * ViewModel to provide lifecycle aware communication with the App's database. More precisely to
 * manage a query to the database and update the UI according to the query result.
 */
public class DetailViewModel extends AndroidViewModel {

    private AppDatabase mDb;
    private LiveData<Integer> movieIsFound; //used in DetailActivity to check if it's a favorite

    public DetailViewModel(Application application, int id) {
        super(application);
        mDb = AppDatabase.getsInstance(application);
        movieIsFound = mDb.movieDAO().searchMovie(id);
    }

    public LiveData<Integer> getMovieIsFound() {
        return movieIsFound;
    }
}
