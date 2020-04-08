package com.tiansirk.popularmovies.data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.tiansirk.popularmovies.BuildConfig;
import com.tiansirk.popularmovies.Movie;
import com.tiansirk.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

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

    private static final String IMG_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    /**
     * Build the URL for requesting form themoviedb.org API
     * It builds as follows: Base + sortCriteria + "?api_key=..." You can insert your API key into the api_keys.xml resource file
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

    /**
     * This method creates an {@link HttpURLConnection} to parse the data from it. It uses {@link InputStream}
     * and {@link Scanner} to read the content from the {@param path} and return it as a String
     * @param path A {@URL} object to create the internet connection
     * @return A String containing the content of the location the {@param path} points to
     * @throws IOException
     */
    public static String getResponseFromWeb(URL path) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) path.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * This method fetches the JSON responded from the API encapsulated in a String and returns the
     * fetched data as a list of {@link Movie} objects
     * @param moviesJson The JSON repsonse encapsulated in a String
     * @return an ArrayList of {@link Movie} objects
     * @throws JSONException
     */
    public static ArrayList<Movie> getMoviesListFromJson(String moviesJson) throws JSONException {

        ArrayList<Movie> movies = new ArrayList<>();

        JSONObject response = new JSONObject(moviesJson);
        JSONArray results = response.getJSONArray("results");
        for(int i=0; i<results.length(); i++){
            JSONObject object = results.getJSONObject(i);
            String posterpath = object.getString("poster_path");
            String overview = object.getString("overview");
            String releaseDate = object.getString("release_date");
            String originalTitle = object.getString("original_title");
            double voteAverage = object.getDouble("vote_average");
            movies.add(new Movie(posterpath, overview, releaseDate, originalTitle, voteAverage));
        }
        return movies;
    }

    /**
     * This method builds the complete url needed to fetch the image using Picasso.
     * http://image.tmdb.org/t/p/w185/{@param relativePath}
     * @param relativePath Returned from the JSON parsing
     * @return The complete url encapsulated in String
     */
    public static String imageUrlBuilder(String relativePath){
        return IMG_BASE_URL + relativePath;
    }

    /**
     * This method generates fake movies to fill in the list of movies for testing purposes
     * @return dummy data
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

    /**
     * Helper method for generating dummy data
     * @return a random double
     */
    private static double randomModifier() {
        Random r = new Random();
        return r.nextInt(10) + r.nextDouble();
    }

}
