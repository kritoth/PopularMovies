package com.tiansirk.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private static final String KEY_ACTIVITY_INTENT = "CHOSEN_MOVIE";
    private static final String DEFAULT_SORT_PREFERENCE = "id";

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;

    private MovieAdapter mAdapter;
    private String mSortingPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator_favorites);
        mRecyclerView = findViewById(R.id.recyclerview_favorites);
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
        ViewModelProvider provider = new ViewModelProvider(this, factory);
        final FavoriteViewModel viewModel = provider.get(FavoriteViewModel.class);
        // Querying the Database according to user's preference
        // Reviews and Trailer links are not queried here, only MovieDAO
        if(mSortingPreference == null || mSortingPreference.isEmpty()){
            mSortingPreference = DEFAULT_SORT_PREFERENCE;
        }
        switch (mSortingPreference) {
            case "id":
                viewModel.getMoviesById().observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        showDataView();
                        mAdapter.setMovieData(movies);
                    }
                });
                break;
            case "rating":
                viewModel.getMoviesByRate().observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        showDataView();
                        mAdapter.setMovieData(movies);
                    }
                });
                break;
            case "date":
                viewModel.getMoviesByDate().observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        showDataView();
                        mAdapter.setMovieData(movies);
                    }
                });
                break;
            case "delete":
                confirmAndNuke(viewModel);
                break;
        }
    }

    private void confirmAndNuke(final FavoriteViewModel viewModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm to delete ALL favorites!");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        viewModel.deleteAllFavorites();
                    }
                });
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                showDataView();
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Clicking on an item of the RecyclerView will open up the DetailActivity by sending the details of the clicked Movie
     * @param clickedMovie {@class Movie} object
     */
    @Override
    public void onClick(Movie clickedMovie) {
        Intent activityIntent = new Intent(this, DetailActivity.class);
        activityIntent.putExtra(KEY_ACTIVITY_INTENT, clickedMovie);
        startActivity(activityIntent);
    }

    /**
     * This method will make the RecyclerView visible and hide the error message
     */
    private void showDataView() {
        /* Hide loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorite_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int selectedItem = item.getItemId();

        switch (selectedItem) {
            case R.id.sort_id:
                Log.d(TAG, "Sort ID menu item selected");
                mSortingPreference = "id";
                mAdapter = new MovieAdapter(this);
                mRecyclerView.setAdapter(mAdapter);
                loadMovieDataFromDb();
                return true;
            case R.id.sort_rate:
                Log.d(TAG, "Sort rating avg menu item selected");
                mSortingPreference = "rating";
                mAdapter = new MovieAdapter(this);
                mRecyclerView.setAdapter(mAdapter);
                loadMovieDataFromDb();
                return true;
            case R.id.sort_date:
                Log.d(TAG, "Sort date added menu item selected");
                mSortingPreference = "date";
                mAdapter = new MovieAdapter(this);
                mRecyclerView.setAdapter(mAdapter);
                loadMovieDataFromDb();
                return true;
            case R.id.delete_all:
                Log.d(TAG, "Sort DELETE ALL menu item selected");
                mSortingPreference = "delete";
                mAdapter = new MovieAdapter(this);
                mRecyclerView.setAdapter(mAdapter);
                loadMovieDataFromDb();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
