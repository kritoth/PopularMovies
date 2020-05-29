package com.tiansirk.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

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
    private List<Movie> mMovies;

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
            if(mDbase.reviewDAO().loadReviewsByMovie(f.getOnlineId()) != null) {
                List<Review> currMovieReviews = new ArrayList<Review>();
                currMovieReviews.addAll(mDbase.reviewDAO().loadReviewsByMovie(f.getOnlineId()));
                for(Review r : currMovieReviews) {
                    reviews.add(r.getReview());
                }
            }
            if(mDbase.trailerDAO().loadTrailersByMovie(f.getOnlineId()) != null) {
                List<VideoKey> currMovieTrailers = new ArrayList<VideoKey>();
                currMovieTrailers.addAll(mDbase.trailerDAO().loadTrailersByMovie(f.getOnlineId()));
                for(VideoKey v : currMovieTrailers) {
                    trailers.add(v.getVideoKey());
                }
            }

            mMovies.add(new Movie(f.getPosterImgUrl(), f.getPlotSynopsis(), f.getReleaseDate(), f.getTitle(), f.getUserRating(),
                    trailers, reviews, f.getOnlineId()));
        }
        Log.d(TAG, "No. of Reviews in DB: " + mDbase.reviewDAO().getReviewsCount());
        Log.d(TAG, "No. of Trailers in DB: " + mDbase.trailerDAO().getTrailersCount());


    }


}
