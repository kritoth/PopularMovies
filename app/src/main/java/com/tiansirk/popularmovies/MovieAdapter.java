package com.tiansirk.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public MovieAdapter(){

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

        //Dummy data for testing
        String imgPath = "http://i.imgur.com/DvpvklR.png";
        String overview = "This film would be great if it did exist.";
        String releaseDate = "2020-04-07";
        String originalTitle = "The Dummy Movie";
        double voteAvg = 9.87;
        Movie dummyMovie = new Movie(imgPath, overview, releaseDate, originalTitle, voteAvg);

        Picasso.get()
                .load(dummyMovie.getPosterImgUrl())
                .into(holder.mPosterImageView);

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder{
        ImageView mPosterImageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            mPosterImageView = itemView.findViewById(R.id.iv_grid_item_poster_view);

        }
    }
}
