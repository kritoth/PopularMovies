package com.tiansirk.popularmovies;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tiansirk.popularmovies.data.AppDatabase;
import com.tiansirk.popularmovies.data.Movie;
import com.tiansirk.popularmovies.data.Review;
import com.tiansirk.popularmovies.data.VideoKey;
import com.tiansirk.popularmovies.ui.ReviewAdapter;
import com.tiansirk.popularmovies.ui.TrailerAdapter;
import com.tiansirk.popularmovies.util.AppExecutors;
import com.tiansirk.popularmovies.util.DateConverter;
import com.tiansirk.popularmovies.util.MoviesUtils;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String KEY_ACTIVITY_INTENT = "CHOSEN_MOVIE";
    private static final String INSTANCE_FAVORITE_STATUS = "favorite_status_state";

    private ImageView mPoster;
    private TextView mTitleView;
    private TextView mReleaseDateView;
    private TextView mRatingView;
    private TextView mPlotView;

    private ImageView mFavoriteButton;
    private TextView mFavoriteText;

    private RecyclerView mTrailerRV;
    private RecyclerView mReviewRV;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private Movie mMovie;
    private AppDatabase mDbase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initViews();

        Intent receivedIntent = getIntent();
        if(receivedIntent != null) {
            if (receivedIntent.hasExtra(KEY_ACTIVITY_INTENT)) {
                mMovie = receivedIntent.getParcelableExtra(KEY_ACTIVITY_INTENT);

                Picasso.get()
                        .load(mMovie.getPosterImgUrl())
                        .into(mPoster);
                mTitleView.setText(mMovie.getTitle());
                mReleaseDateView.setText(mMovie.getReleaseDate());
                mRatingView.setText(Double.toString(mMovie.getUserRating()));
                mPlotView.setText(mMovie.getPlotSynopsis());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mPlotView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
                }
            }
        }
        //Log.d(TAG, "Content of mMovie field is: " + mMovie.toString());

        RecyclerView.LayoutManager reviewLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mReviewRV.setLayoutManager(reviewLayoutManager);
        mReviewRV.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter(mMovie);
        mReviewRV.setAdapter(mReviewAdapter);

        RecyclerView.LayoutManager trailerLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mTrailerRV.setLayoutManager(trailerLayoutManager);
        mTrailerRV.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(mMovie, this);
        mTrailerRV.setAdapter(mTrailerAdapter);

        mDbase = AppDatabase.getsInstance(getApplicationContext());

        setFavoriteStatus();

        // OnClickListener for the FavoriteButton in the favorite section
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Saves if it is not a favorite yet
                if(!mMovie.isFavorite()) {
                    saveMovieAsFavorite();
                }
                //Removes if it is a favorite already
                else if(mMovie.isFavorite()) {
                    removeFromFavorites();
                }
            }
        });
    }

    /**
     * Checks App's Database whether the displayed Movie exists in it as a favorite and according to
     * this state, sets how to display the Movie to the user: either as favorite or not by setting the text and image of the
     * favorite section in the UI.
     */
    private void setFavoriteStatus() {
        LiveData<Integer> isFound= mDbase.movieDAO().searchMovie(mMovie.getOnlineId());
        isFound.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer == 1) {
                    mMovie.setFavorite(true);
                    showAsFavorite();
                }
                else if (integer == 0){
                    mMovie.setFavorite(false);
                    showAsNotFavoriteYet();
                }
            }
        });
    }

    /**
     * Saves a Movie into the App's Database as Movie
     */
    private void saveMovieAsFavorite() {
        mMovie.setDateAddedToFav(DateConverter.toTimestamp(today()));
        long insertedFavMovieId = insertFavoriteMovie(mMovie);

        Log.d(TAG, "\nNo. of Reviews in Movie: " + mMovie.getReviews().size() + "\nNo. of Trailers in Movie: " + mMovie.getVideoKeys().size());

        for(int i=0; i<mMovie.getReviews().size(); i++){
            Review currReview = new Review(mMovie.getOnlineId(), mMovie.getReviews().get(i));
            insertReview(currReview);
            Log.d(TAG, "Inserted review:\n" + i + ": " + currReview.toString());
        }
        for(int j=0; j<mMovie.getVideoKeys().size(); j++){
            VideoKey currVideoKey = new VideoKey(mMovie.getOnlineId(), mMovie.getVideoKeys().get(j));
            insertTrailer(currVideoKey);
            Log.d(TAG, "Inserted trailer:\n" + j + ": " + currVideoKey.toString());
        }
        if(insertedFavMovieId > -1){
            Toast.makeText(getApplicationContext(), mMovie.getTitle() + " is saved as favorite!", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Movie INSERT successful");
        }
        else{
            Toast.makeText(getApplicationContext(), "Saving " + mMovie.getTitle() + " as favorite was unsuccessful.", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Movie INSERT successful");
        }
        finish();
        startActivity(getIntent());
    }

    /**
     * Removes a Movie from the App's Database as Movie
     */
    private void removeFromFavorites() {
        deleteReviewsFromFavorites();
        deleteTrailersFromFavorites();
        deleteMovieFromFavorites();
        Toast.makeText(this, mMovie.getTitle() + " is removed from favorites.", Toast.LENGTH_LONG).show();
    }

    //TODO: change to recyclerview clicklistener
    @Override
    public void onClick(String clickedTrailerUrl) {
        Log.d(TAG, "Trailer item clicked: " + clickedTrailerUrl);

        Intent webIntent = new Intent(Intent.ACTION_VIEW, MoviesUtils.buildVideoURL(clickedTrailerUrl));
        if (webIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(webIntent);
        }
    }

    /**
     * Inserts a Movie object into the app's Database
     * @return the iD of the new row from the app's Database
     */
    private long insertFavoriteMovie(final Movie favoriteMovie){
        final long[] iD = new long[1];
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                iD[0] = mDbase.movieDAO().insertFavMovie(favoriteMovie);
            }
        });
        return iD[0];
    }

    /**
     * Inserts a Review object into the app's Database
     * @return the iD of the new row from the app's Database
     */
    private long insertReview(final Review review){
        final long[] iD = new long[1];
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                iD[0] = mDbase.reviewDAO().insertReview(review);
            }
        });
        return iD[0];
    }

    /**
     * Inserts a Trailer object into the app's Database
     * @return the iD of the new row from the app's Database
     */
    private long insertTrailer(final VideoKey trailer){
        final long[] iD = new long[1];
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                iD[0] = mDbase.trailerDAO().insertTrailer(trailer);
            }
        });
        return iD[0];
    }

    /**
     * Deletes a Movie object from the app's Database
     * @return the iD of the deleted row from the app's Database
     */
    private void deleteMovieFromFavorites(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDbase.movieDAO().removeFavMovie(mMovie);
            }
        });
    }

    /**
     * Deletes Review objects related to a Movie object from the app's Database
     */
    private void deleteReviewsFromFavorites(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDbase.reviewDAO().removeReviewsOfMovie(mMovie.getOnlineId());
            }
        });
    }

    /**
     * Deletes Trailer objects related to a Movie object from the app's Database
     */
    private void deleteTrailersFromFavorites(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDbase.trailerDAO().removeTrailersOfMovie(mMovie.getOnlineId());
            }
        });
    }

    private void initViews() {
        mPoster = findViewById(R.id.detail_iv_poster_view);
        mTitleView = findViewById(R.id.detail_tv_title);
        mReleaseDateView = findViewById(R.id.detail_tv_release_date);
        mRatingView = findViewById(R.id.detail_tv_rating);
        mFavoriteButton = findViewById(R.id.detail_iv_favorite);
        mFavoriteText = findViewById(R.id.detail_tv_favorite);
        mPlotView = findViewById(R.id.detail_tv_overview);

        mReviewRV = findViewById(R.id.detail_rv_review_holder);
        mTrailerRV = findViewById(R.id.detail_rv_trailer_holder);

    }

    /**
     * Show the Star button filled and the Text indicating it is a favorite
     */
    private void showAsFavorite() {
        mFavoriteButton.setImageResource(R.drawable.ic_star_filled_gold_24dp);
        mFavoriteText.setText(R.string.marked_as_favorite);
    }
    /**
     * Show the Star button empty and the Text indicating it is not a favorite
     */
    private void showAsNotFavoriteYet(){
        mFavoriteButton.setImageResource(R.drawable.ic_star_border_gold_24dp);
        mFavoriteText.setText(R.string.mark_as_favorite);
    }

    /**
     * Helper method to return the date of the day it's been called
     * @return Date object of the same day
     */
    private Date today(){
        return Calendar.getInstance().getTime();
    }

}
