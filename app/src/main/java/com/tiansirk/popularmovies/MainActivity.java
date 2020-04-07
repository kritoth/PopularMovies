package com.tiansirk.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_movies);
        mErrorMessage = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        //Dummy data for testing
        String imgPath = "http://i.imgur.com/DvpvklR.png";
        String overview = "This film would be great if it did exist.";
        String releaseDate = "2020-04-07";
        String originalTitle = "The Dummy Movie";
        double voteAvg = 9.87;
        Movie dummyMovie = new Movie(imgPath, overview, releaseDate, originalTitle, voteAvg);
        
    }
}
