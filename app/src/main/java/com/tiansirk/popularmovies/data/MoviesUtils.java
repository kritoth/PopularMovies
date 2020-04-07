package com.tiansirk.popularmovies.data;

import com.tiansirk.popularmovies.Movie;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class is for providing movies data
 */
public final class MoviesUtils {

    private static final int NUMBER_OF_MOVIES = 40;
    private static String posterImgUrl = "http://i.imgur.com/DvpvklR.png";
    private static String plotSynopsis = "This is about ";
    private static String releaseDate = "2020-4-";
    private static String title = "The Match ";
    private static double userRating = 0;


    public static ArrayList<Movie> getMoviesListFromJson(String moviesJson){

        ArrayList<Movie> movies = new ArrayList<>();

        return movies;
    }

    /**
     * This method generates fake movies to fill in the list of movies
     */
    public static ArrayList<Movie> getDummyMoviesList(){
        ArrayList<Movie> fakeMovies = new ArrayList<>(NUMBER_OF_MOVIES);
        for(int i=0;i<fakeMovies.size(); i++){
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
