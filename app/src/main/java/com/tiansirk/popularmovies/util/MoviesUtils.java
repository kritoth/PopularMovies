package com.tiansirk.popularmovies.util;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.tiansirk.popularmovies.data.Movie;
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
    private static final String APPEND_TO_RESPONSE = "append_to_response";
    private static final String[] SUB_REQUESTS = {"videos", "reviews"};
    public static final String DELIMITER = ",";

    private static final String IMG_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    private static final String YOUTUBE_WATCH_URL = "https://www.youtube.com/watch";
    private static final String YOUTUBE_QUERY_PARAM_KEY = "v";

    /**
     * Build the URL for to request a list of movies from themoviedb.org API
     * It builds as follows: https://api.themoviedb.org/3/movie/{sortCriteria}?api_key={api_key}"
     * You can insert your API key into the api_keys.xml resource file
     * @param sortCriteria: depending on the user's setting can be: "top_rated", "popular", "now_playing" or "upcoming"
     * @return the built URL
     */
    public static URL buildListUrl(String sortCriteria, Context context){

        Uri builtUri = Uri.parse(TMDB_BASE_URL + sortCriteria).buildUpon()
                .appendQueryParameter(QUERY_PARAM_KEY, context.getString(R.string.THE_MOVIE_DB_API_TOKEN))
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Built Movie list URI: " + url);

        return url;
    }

    /**
     * Build the URL for to request some details of a single Movie from themoviedb.org API
     * It builds as follows:  https://api.themoviedb.org/3/movie/{movieID}?api_key={api_key}&append_to_response={videos},{reviews},{etc}
     * You can insert your API key into the api_keys.xml resource file
     * @param movieID: The ID of the Movie of which details are to be requested
     * @param context: Application's context to reach for the API key available in the Application's resources
     * @return the built URL
     */
    public static URL buildDetailURL(int movieID, Context context){
        Uri builtUri = Uri.parse(TMDB_BASE_URL + movieID).buildUpon()
                .appendQueryParameter(QUERY_PARAM_KEY, context.getString(R.string.THE_MOVIE_DB_API_TOKEN))
                .appendQueryParameter(APPEND_TO_RESPONSE, TextUtils.join(DELIMITER, SUB_REQUESTS)) //TODO: vessző helyett %2C -t csinál
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Built Movie detail URI: " + builtUri);

        return url;
    }

    /**
     * Build the URL for a youtube video of a single Movie
     * It builds as follows:  https://www.youtube.com/watch?v={videoKey}
     * @param videoKey: The key of the video on youtube
     * @return the built Uri
     */
    public static Uri buildVideoURL(String videoKey){
        Uri builtUri = Uri.parse(YOUTUBE_WATCH_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_QUERY_PARAM_KEY, videoKey)
                .build();
        Log.d(TAG, "Built Youtube URI: " + builtUri);

        return builtUri;
    }

    /**
     * This method creates an {@link HttpURLConnection} to parse the data from it. It uses {@link InputStream}
     * and {@link Scanner} to read the content from the {@param path} and return it as a String
     * @param path A {@URL} object to create the internet connection
     * @return A String containing the content of the URL location the {@param path} points to
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
     * @param context
     * @return an ArrayList of {@link Movie} objects
     * @throws JSONException
     */
    public static ArrayList<Movie> getMoviesListFromJson(String moviesJson, Context context) throws JSONException, IOException {

        ArrayList<Movie> movies = new ArrayList<>();

        JSONObject response = new JSONObject(moviesJson);
        JSONArray results = response.getJSONArray("results");
        for(int i=0; i<results.length(); i++){
            JSONObject object = results.getJSONObject(i);
            String posterpath = object.getString("poster_path");
            String completePosterPath = imageUrlBuilder(posterpath);

            String overview = object.getString("overview");
            String releaseDate = object.getString("release_date");
            String originalTitle = object.getString("original_title");
            double voteAverage = object.getDouble("vote_average");

            int id = object.getInt("id");
            //Todo: 1. get this new network request out
            // 2. insert empty List<>s here and make the other second request when starting DetailActivity
            // 3. for this the "id" of the Movie need to be saved into Movie
            String movieDetails = getResponseFromWeb(buildDetailURL(id, context));
            JSONObject responseDetail = new JSONObject(movieDetails);

            JSONObject objectVideos = responseDetail.getJSONObject("videos");
            JSONArray resultsVideos = objectVideos.getJSONArray("results");
            ArrayList<String> videoKeys = new ArrayList<>();
            for(int j=0; j<resultsVideos.length(); j++){
                JSONObject video = resultsVideos.getJSONObject(j);
                String key = video.getString("key");
                videoKeys.add(key);
            }
            JSONObject objectReviews = responseDetail.getJSONObject("reviews");
            JSONArray resultsReviews = objectReviews.getJSONArray("results");
            ArrayList<String> reviews = new ArrayList<>();
            for(int k=0; k<resultsReviews.length(); k++){
                JSONObject review = resultsReviews.getJSONObject(k);
                String author = review.getString("author");
                String content = review.getString("content");
                reviews.add(author + DELIMITER + content);
            }

            movies.add(new Movie(completePosterPath, overview, releaseDate, originalTitle, voteAverage, videoKeys, reviews, id));
        }
        return movies;
    }

    /**
     * This method builds the complete url needed to fetch the image using Picasso.
     * http://image.tmdb.org/t/p/w185/{@param relativePath}
     * @param relativePath Returned from the JSON parsing
     * @return The complete url encapsulated in String
     */
    private static String imageUrlBuilder(String relativePath){
        return IMG_BASE_URL + relativePath;
    }

    /**
     * This method generates fake movies to fill in the list of movies for testing purposes
     * @return dummy data
     */
    public static ArrayList<Movie> getDummyMoviesList(String json, Context context) throws JSONException{
        ArrayList<Movie> fakeMovies = new ArrayList<>();
        for(int i=0; i<NUMBER_OF_MOVIES; i++){
            fakeMovies.add(new Movie(
                    posterImgUrl,
                    plotSynopsis + i + " foxes chasing around.",
                    releaseDate + i,
                    title + i,
                    userRating + randomModifier(10),
                    dummyTrailers(),
                    dummyReviews(),
                    (int)randomModifier(999)
            ));
        }
        return fakeMovies;
    }
    /**
     * Helper method for generating dummy video location data
     * @return a ArrayList of dummy trailers
     */
    private static ArrayList<String> dummyTrailers(){
        ArrayList<String> dummyTrailers = new ArrayList<>();
        for(int i=0; i<randomModifier(4); i++){
            dummyTrailers.add("http:\\www.video-is-here\\" + i);
        }
        return dummyTrailers;
    }
    /**
     * Helper method for generating dummy review data
     * @return a ArrayList of dummy reviews
     */
    private static ArrayList<String> dummyReviews(){
        ArrayList<String> dummyReviews = new ArrayList<>();
        for(int i=0; i<randomModifier(10); i++){
            dummyReviews.add("Review:\nThis Movie is great!\n" + i + "/10 star!");
        }
        return dummyReviews;
    }
    /**
     * Helper method for generating dummy data
     * @return a random double bween 0 and 10
     */
    private static double randomModifier(int bound) {
        Random r = new Random();
        return r.nextInt(bound) + r.nextDouble();
    }

}
