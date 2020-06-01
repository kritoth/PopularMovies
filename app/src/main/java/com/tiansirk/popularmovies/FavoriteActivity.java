package com.tiansirk.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tiansirk.popularmovies.data.AppDatabase;
import com.tiansirk.popularmovies.data.Movie;
import com.tiansirk.popularmovies.data.Review;
import com.tiansirk.popularmovies.data.VideoKey;
import com.tiansirk.popularmovies.model.FavoriteViewModel;
import com.tiansirk.popularmovies.model.FavoriteViewModelFactory;
import com.tiansirk.popularmovies.ui.MovieAdapter;
import com.tiansirk.popularmovies.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = FavoriteActivity.class.getSimpleName();
    private static final String KEY_ACTIVITY_INTENT = "key_movie_to_detail_activity";

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;

    private MovieAdapter mAdapter;
    private List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 4);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        loadMovieDataFromDb();
    }

    private void loadMovieDataFromDb() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        FavoriteViewModelFactory factory = new FavoriteViewModelFactory(getApplication());
        ViewModelProvider provider = new ViewModelProvider(this);
        FavoriteViewModel viewModel = provider.get(FavoriteViewModel.class);
        //TODO: switch according to user's preference
        viewModel.getMoviesByDate().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                mAdapter.setMovieData((ArrayList<Movie>) movies);
            }
        });
    }


    @Override
    public void onClick(Movie clickedMovie) {
        Intent activityIntent = new Intent(this, DetailActivity.class);
        activityIntent.putExtra(KEY_ACTIVITY_INTENT, clickedMovie);
        startActivity(activityIntent);
    }
}
