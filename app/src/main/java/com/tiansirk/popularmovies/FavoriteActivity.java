package com.tiansirk.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.tiansirk.popularmovies.data.AppDatabase;
import com.tiansirk.popularmovies.data.FavoriteMovie;
import com.tiansirk.popularmovies.data.Movie;
import com.tiansirk.popularmovies.data.Review;
import com.tiansirk.popularmovies.data.VideoKey;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private static final String TAG = FavoriteActivity.class.getSimpleName();

    private AppDatabase mDbase;
    List<FavoriteMovie> mFavoriteMovies;
    List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mMovies = new ArrayList<>();

        mDbase = AppDatabase.getsInstance(this);
        mFavoriteMovies = new ArrayList<>();
        mFavoriteMovies.addAll(mDbase.movieDAO().loadAllFavMoviesByRating());
        List<String> reviews = new ArrayList<>();
        List<String> trailers = new ArrayList<>();

        for(FavoriteMovie f : mFavoriteMovies){
            if(mDbase.reviewDAO().loadReview(f.getOnlineId()) != null) {
                reviews.add(mDbase.reviewDAO().loadReview(f.getOnlineId()).getReview());
            }
            if(mDbase.trailerDAO().loadTrailer(f.getOnlineId()) != null) {
                trailers.add(mDbase.trailerDAO().loadTrailer(f.getOnlineId()).getVideoKey());
            }
            mMovies.add(new Movie(f.getPosterImgUrl(), f.getPlotSynopsis(), f.getReleaseDate(), f.getTitle(), f.getUserRating(),
                    trailers, reviews, f.getOnlineId()));
        }

        Log.d(TAG, "Request from Database: " + printMovies(reviews, trailers));
    }

    private String printMovies(List<String> reviews, List<String> videoKeys) {
        String s = "\nNo. of Movies = " + mFavoriteMovies.size()
                + "\nNo. of FavoriteMovies = " + mFavoriteMovies.size()
                + "\nFavoriteMovies: ";
        String s1 = "";
        for(int i=0; i<mFavoriteMovies.size(); i++){
            s1 += TextUtils.concat(
                    "\n",
                    Integer.toString(i),
                    ": ",
                    mFavoriteMovies.get(i).getTitle(),
                    "\nReviews: ",
                    Integer.toString(reviews.size()),
                    "\nTrailers: ",
                    Integer.toString(videoKeys.size())).toString();
        }
        String s2 = "\nMovies: " + mMovies.toString();

        return s+s1+s2;
    }
}
