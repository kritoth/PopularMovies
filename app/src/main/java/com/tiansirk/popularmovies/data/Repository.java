package com.tiansirk.popularmovies.data;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.tiansirk.popularmovies.MainActivity;
import com.tiansirk.popularmovies.Movie;
import com.tiansirk.popularmovies.MovieAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Repository {

    private static final String TAG = Repository.class.getSimpleName();
    private Context mContext;
/*
    private MoviesDao moviesDao;
    private ArrayList<Movie> mFavoriteMovies;
*/


    public Repository(Application application) {
        mContext = application.getApplicationContext();

/*        // Get the database and store inside a member var here
        AppDatabase db = AppDatabase.getDatabase(application);
        this.moviesDao = db.moviesDao();
        // Get all the favorite movies from the db
        this.mFavoriteMovies = mFavoriteMovies.getFavoriteMovies();
 */
    }
/*
    public ArrayList<Movie> getFavoriteMovies(){
        return mFavoriteMovies;
    }
*/

}
