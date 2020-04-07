package com.tiansirk.popularmovies.data;

import com.tiansirk.popularmovies.Movie;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class is for providing movies data
 */
public final class MoviesUtils {

    private static final int NUMBER_OF_MOVIES = 40;
    private String posterImgUrl = "http://i.imgur.com/DvpvklR.png";
    private String plotSynopsis = "This is about ";
    private String releaseDate = "2020-4-";
    private String title = "The Match ";
    private double userRating = 0;

    private ArrayList<Movie> movies = new ArrayList<>();

    public void addMovie(Movie movie){
        movies.add(movie);
    }

    /**
     * This method generates fake movies to fill in the list of movies
     */
    public void addMovie(){
        for(int i=0;i<NUMBER_OF_MOVIES; i++){
            movies.add(new Movie(posterImgUrl, plotSynopsis + i + " foxes chasing around.", releaseDate + i, title + i,
                    userRating + randomModifier()));
        }
    }

    private double randomModifier() {
        Random r = new Random();
        return r.nextInt(10) + r.nextDouble();
    }

    public ArrayList<Movie> getMovies(){
        return movies;
    }

}
