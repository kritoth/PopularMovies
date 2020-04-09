package com.tiansirk.popularmovies;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_INTENT = "CHOSEN_MOVIE";

    private TextView mTitleView;
    private TextView mReleaseDateView;
    private TextView mRatingView;
    private TextView mPlotView;

    private Movie mMovie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleView = findViewById(R.id.tv_title);
        mReleaseDateView = findViewById(R.id.tv_release_date);
        mRatingView = findViewById(R.id.tv_rating);
        mPlotView = findViewById(R.id.tv_overview);

        Intent receivedIntent = getIntent();
        if(receivedIntent != null) {
            if (receivedIntent.hasExtra(KEY_ACTIVITY_INTENT)) {
                mMovie = receivedIntent.getParcelableExtra(KEY_ACTIVITY_INTENT);

                mTitleView.setText(mMovie.getTitle());
                mReleaseDateView.setText(mMovie.getReleaseDate());
                mRatingView.setText(Double.toString(mMovie.getUserRating()));
                mPlotView.setText(mMovie.getPlotSynopsis());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mPlotView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
                }
            }
        }
    }
}
