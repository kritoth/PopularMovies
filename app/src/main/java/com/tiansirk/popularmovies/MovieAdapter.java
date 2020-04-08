package com.tiansirk.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tiansirk.popularmovies.data.MoviesUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private ArrayList<Movie> mMovieData;

    public MovieAdapter(){
        mMovieData = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.grid_item;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);

        MovieViewHolder holder = new MovieViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movieForThisItem = mMovieData.get(position);
        String imgPath = MoviesUtils.imageUrlBuilder(movieForThisItem.getPosterImgUrl());
        Picasso.get()
                .load(imgPath)
                .into(holder.mPosterImageView);

        Log.v(TAG, "Poster img in Movie: " + imgPath);
    }

    @Override
    public int getItemCount() {
        if(mMovieData.isEmpty()) return 0;
        return mMovieData.size();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder{
        ImageView mPosterImageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mPosterImageView = itemView.findViewById(R.id.iv_grid_item_poster_view);
        }
    }

    /**
     * This method is to set the data of the Movies on a MovieAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new MovieAdapter to display it.
     * @param movies The list of Movies to set to the Adapter
     */
    public void setMovieData(ArrayList movies) {
        mMovieData.addAll(movies);
        notifyDataSetChanged();
    }
}
