package com.tiansirk.popularmovies;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_INTENT = "CHOSEN_MOVIE";

    private ImageView mPoster;
    private TextView mTitleView;
    private TextView mReleaseDateView;
    private TextView mRatingView;
    private TextView mPlotView;

    private ImageView mFavoriteButton;
    private TextView mFavoriteText;

    private ImageView mTrailerPlay;
    private TextView mTrailerTitle;

    private Movie mMovie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPoster = findViewById(R.id.detail_iv_poster_view);
        mTitleView = findViewById(R.id.detail_tv_title);
        mReleaseDateView = findViewById(R.id.detail_tv_release_date);
        mRatingView = findViewById(R.id.detail_tv_rating);
        mPlotView = findViewById(R.id.detail_tv_overview);

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

        mFavoriteText = findViewById(R.id.detail_tv_favorite);
        //Todo: Check ROOM and reset its text according to its state (see strings.xml)

        mFavoriteButton = findViewById(R.id.detail_iv_favorite);
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add/Delete to/from ROOM and reset its drawable according to its state (bordered/filled star)
                Toast.makeText(DetailActivity.this, "Favorite Star is clicked", Toast.LENGTH_LONG).show();
            }
        });


        mTrailerTitle = findViewById(R.id.trailer_item_tv_title);
        // Todo: Set its text if there is a title for the trailer. Check the youtube API and set/create Movie field


        mTrailerPlay = findViewById(R.id.trailer_item_iv_play_button);
        mTrailerPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Add Implicit Intent to open up video player to the weblink
                Toast.makeText(DetailActivity.this, "Play button is clicked", Toast.LENGTH_LONG).show();
            }
        });

    }
}
