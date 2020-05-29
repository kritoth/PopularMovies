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
import com.tiansirk.popularmovies.data.FavoriteMovie;
import com.tiansirk.popularmovies.data.Movie;
import com.tiansirk.popularmovies.data.Review;
import com.tiansirk.popularmovies.data.VideoKey;
import com.tiansirk.popularmovies.ui.ReviewAdapter;
import com.tiansirk.popularmovies.ui.TrailerAdapter;
import com.tiansirk.popularmovies.util.MoviesUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String KEY_ACTIVITY_INTENT = "CHOSEN_MOVIE";

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

        mDbase = AppDatabase.getsInstance(getApplicationContext());

        mPoster = findViewById(R.id.detail_iv_poster_view);
        mTitleView = findViewById(R.id.detail_tv_title);
        mReleaseDateView = findViewById(R.id.detail_tv_release_date);
        mRatingView = findViewById(R.id.detail_tv_rating);
        mFavoriteButton = findViewById(R.id.detail_iv_favorite);
        mFavoriteText = findViewById(R.id.detail_tv_favorite);
        mPlotView = findViewById(R.id.detail_tv_overview);

        mReviewRV = findViewById(R.id.detail_rv_review_holder);
        mTrailerRV = findViewById(R.id.detail_rv_trailer_holder);

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

        mFavoriteText = findViewById(R.id.detail_tv_favorite);
        //Todo: Check ROOM and reset its text and the star according to its state (see strings.xml)

        mFavoriteButton = findViewById(R.id.detail_iv_favorite);
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteMovie favoriteMovie = new FavoriteMovie(
                        mMovie.getPosterImgUrl(),
                        mMovie.getPlotSynopsis(),
                        mMovie.getReleaseDate(),
                        mMovie.getTitle(),
                        mMovie.getUserRating(),
                        today(),
                        mMovie.getOnlineId());
                long insertedFavMovieId = mDbase.movieDAO().insertFavMovie(favoriteMovie);
                Log.d(TAG,
                        "\nNo. of Reviews in Movie: " + mMovie.getReviews().size()
                                + "\nNo. of Trailers in Movie: " + mMovie.getVideoKeys().size());
                for(int i=0; i<mMovie.getReviews().size(); i++){
                    Review currReview = new Review(mMovie.getOnlineId(), mMovie.getReviews().get(i));
                    mDbase.reviewDAO().insertReview(currReview);
                    Log.d(TAG, "Inserted review:\n" + i + ": " + currReview.toString());
                }

                for(int j=0; j<mMovie.getVideoKeys().size(); j++){
                    VideoKey currVideoKey = new VideoKey(mMovie.getOnlineId(), mMovie.getVideoKeys().get(j));
                    mDbase.trailerDAO().insertTrailer(currVideoKey);
                    Log.d(TAG, "Inserted trailer:\n" + j + ": " + currVideoKey.toString());
                }
                if(insertedFavMovieId > -1){
                    Toast.makeText(getApplicationContext(), mMovie.getTitle() + " is saved as favorite!", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "FavoriteMovie INSERT successful");
                }
            }
        });


    }

    @Override
    public void onClick(String clickedTrailerUrl) {
        Log.d(TAG, "Trailer item clicked: " + clickedTrailerUrl);

        Intent webIntent = new Intent(Intent.ACTION_VIEW, MoviesUtils.buildVideoURL(clickedTrailerUrl));
        if (webIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(webIntent);
        }
    }

    private Date today(){
        return Calendar.getInstance().getTime();
    }

}
