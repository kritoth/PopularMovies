package com.tiansirk.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.tiansirk.popularmovies.data.AppDatabase;
import com.tiansirk.popularmovies.data.Movie;
import com.tiansirk.popularmovies.data.Review;
import com.tiansirk.popularmovies.data.VideoKey;
import com.tiansirk.popularmovies.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private static final String TAG = FavoriteActivity.class.getSimpleName();

    private AppDatabase mDbase;
    private List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mMovies = new ArrayList<>();

        mDbase = AppDatabase.getsInstance(this);
        final List<Movie> favoriteMovies = new ArrayList<>();
        favoriteMovies.addAll(mDbase.movieDAO().loadAllFavMoviesByRating());
        final List<String> reviews = new ArrayList<>();
        final List<String> trailers = new ArrayList<>();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for(Movie f : favoriteMovies){
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
                    mMovies.add(new Movie(f.getPosterImgUrl(), f.getPlotSynopsis(), f.getReleaseDate(), f.getTitle(),
                            f.getUserRating(), trailers, reviews, f.getOnlineId()));
                }
                Log.d(TAG, "No. of Reviews in DB: " + mDbase.reviewDAO().getReviewsCount());
                Log.d(TAG, "No. of Trailers in DB: " + mDbase.trailerDAO().getTrailersCount());
            }
        });


    }


}
