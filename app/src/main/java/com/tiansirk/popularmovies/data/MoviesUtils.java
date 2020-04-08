package com.tiansirk.popularmovies.data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.tiansirk.popularmovies.BuildConfig;
import com.tiansirk.popularmovies.Movie;
import com.tiansirk.popularmovies.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class is for providing movies data
 */
public final class MoviesUtils {

    private static final String TAG = MoviesUtils.class.getSimpleName();

    private static final int NUMBER_OF_MOVIES = 40;
    private static String posterImgUrl = "http://i.imgur.com/DvpvklR.png";
    private static String plotSynopsis = "This is about ";
    private static String releaseDate = "2020-4-";
    private static String title = "The Match ";
    private static double userRating = 0;

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String QUERY_PARAM_KEY = "api_key";

    /**
     * Build the URL for requesting form themoviedb.org API
     * It builds as follows: Base + sortCriteria + "?api_key=..."
     * @param sortCriteria: depending on the user's setting can be: "top_rated" OR "popular"
     * @return the built URL
     */
    public static URL buildUrl (String sortCriteria, Context context){

        Uri builtUri = Uri.parse(TMDB_BASE_URL + sortCriteria).buildUpon()
                .appendQueryParameter(QUERY_PARAM_KEY, context.getString(R.string.THE_MOVIE_DB_API_TOKEN))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI: " + url);

        return url;

    }

    public static String getResponseFromWeb(URL path){
        return null;
    }

    public static String getJsonFromWebResponse (String responseFromWeb){
        return null;
    }

    public static ArrayList<Movie> getMoviesListFromJson(String moviesJson){

        ArrayList<Movie> movies = new ArrayList<>();

        return movies;
    }

    /**
     * This method generates fake movies to fill in the list of movies
     */
    public static ArrayList<Movie> getDummyMoviesList(){
        ArrayList<Movie> fakeMovies = new ArrayList<>();
        for(int i=0; i<NUMBER_OF_MOVIES; i++){
            fakeMovies.add(new Movie(
                    posterImgUrl,
                    plotSynopsis + i + " foxes chasing around.",
                    releaseDate + i,
                    title + i,
                    userRating + randomModifier())
            );
        }
        return fakeMovies;
    }

    private static double randomModifier() {
        Random r = new Random();
        return r.nextInt(10) + r.nextDouble();
    }

}
