package com.tiansirk.popularmovies.data;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.tiansirk.popularmovies.MainActivity;
import com.tiansirk.popularmovies.Movie;

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
    private ArrayList<Movie> mLoadedMovies;

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
    public ArrayList<Movie> getMovies(String moviesType){
        /*
        if(moviesType == Contract.FAVORITE) getFavoriteMovies();
        else*/ new FetchMovieTask().execute(moviesType);
        return  mLoadedMovies;
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {

            String receivedQueryParam = strings[0];

            URL url = MoviesUtils.buildUrl(receivedQueryParam, mContext);

            try {
                String jsonResponse = MoviesUtils.getResponseFromWeb(url);
                //Log.v(TAG, "jsonResponse: " + jsonResponse);

                ArrayList<Movie> moviesFetchedFromJson = MoviesUtils.getMoviesListFromJson(jsonResponse);
                Log.v(TAG, "Number of fetched movies: " + moviesFetchedFromJson.size() +
                        "\nFirst movie in the list: " + moviesFetchedFromJson.get(0).toString());

                return moviesFetchedFromJson;
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Problem with either reading from Internet connection or parsing JSON: ", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
                mLoadedMovies.addAll(movies);
        }
    }
}
