package com.tiansirk.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private ArrayList<Movie> mMovieData;

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie clickedMovie);
    }

    private final MovieAdapterOnClickHandler mClickHandler;

    /**
     * Constructor
     * @param onClickHandler registers the click handler
     */
    public MovieAdapter(MovieAdapterOnClickHandler onClickHandler){
        mClickHandler = onClickHandler;
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
        String imgPath = movieForThisItem.getPosterImgUrl();
        Picasso.get()
                .load(imgPath)
                .into(holder.mPosterImageView);

        //Log.v(TAG, "Poster img in Movie: " + imgPath);
    }

    @Override
    public int getItemCount() {
        if(mMovieData.isEmpty()) return 0;
        return mMovieData.size();
    }

    /**
     * Custom ViewHolder for recycling the itemviews
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mPosterImageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mPosterImageView = itemView.findViewById(R.id.iv_grid_item_poster_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Movie movie = mMovieData.get(clickedPosition);
            mClickHandler.onClick(movie);
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
