package com.tiansirk.popularmovies.model;

import android.app.Application;

import com.tiansirk.popularmovies.data.AppDatabase;
import com.tiansirk.popularmovies.data.Movie;
import com.tiansirk.popularmovies.data.Review;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FavoriteViewModel extends AndroidViewModel {

    private AppDatabase mDb;

    private LiveData<List<Movie>> moviesById; //used in FavoriteActivity to sort by TMDB_ID
    private LiveData<List<Movie>> moviesByRate; //used in FavoriteActivity to sort by avg_rating
    private LiveData<List<Movie>> moviesByDate; //used in FavoriteActivity to sort by date

    private LiveData<List<Review>> reviewsForMovie;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        mDb = AppDatabase.getsInstance(application);
    }

    public LiveData<List<Movie>> getMoviesByDate() {
        moviesByDate = mDb.movieDAO().loadAllFavMoviesByDateAdded();
        return moviesByDate;
    }

    public LiveData<List<Movie>> getMoviesById() {
        moviesById = mDb.movieDAO().loadAllFavMoviesById();
        return moviesById;
    }

    public LiveData<List<Movie>> getMoviesByRate() {
        moviesByRate = mDb.movieDAO().loadAllFavMoviesByRating();
        return moviesByRate;
    }

    public LiveData<List<Review>> getReviewsForMovie(int id){
        reviewsForMovie = mDb.reviewDAO().loadReviewsByMovie(id);
        return reviewsForMovie;
    }

    public void deleteAllFavorites(){
        mDb.movieDAO().deleteAllFavMovie();
    }
}
