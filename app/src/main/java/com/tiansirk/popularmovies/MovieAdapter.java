package com.tiansirk.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

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

        holder.mTitleTextView.setText(movieForThisItem.getTitle());
        //TODO: Build the full poster URL using the API documentation and getPosterImgUrl
        // then fetch it with Picasso and set it to the mPosterImageView
        Picasso.get()
                .load(movieForThisItem.getPosterImgUrl())
                .into(holder.mPosterImageView);

    }

    @Override
    public int getItemCount() {
        if(mMovieData.isEmpty()) return 0;
        return mMovieData.size();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder{
        ImageView mPosterImageView;
        TextView mTitleTextView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            mPosterImageView = itemView.findViewById(R.id.iv_grid_item_poster_view);
            mTitleTextView = itemView.findViewById(R.id.tv_title);
        }
    }

    /**
     * This method is to set the data of the Movieon a ForecastAdapter if we've already
     *      * created one. This is handy when we get new data from the web but don't want to create a
     *      * new ForecastAdapter to display it.
     * @param movies
     */
    public void setMovieData(ArrayList movies) {
        mMovieData.addAll(movies);
        notifyDataSetChanged();
    }
}
