package com.tiansirk.popularmovies.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiansirk.popularmovies.data.Movie;
import com.tiansirk.popularmovies.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private List<String> mTrailerData;
    private final TrailerAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface TrailerAdapterOnClickHandler {
        void onClick(String clickedTrailerUrl);
    }

    public TrailerAdapter(Movie movie, TrailerAdapterOnClickHandler onClickHandler) {
        mTrailerData = new ArrayList<>();
        mTrailerData.addAll(movie.getVideoKeys());
        mClickHandler = onClickHandler;
        Log.d(TAG, "Trailers loaded into Adapter: " + mTrailerData.size());
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView mPlayButtonView;
        public final TextView mTrailerTitle;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            mPlayButtonView = itemView.findViewById(R.id.trailer_item_iv_play_button);
            mTrailerTitle = itemView.findViewById(R.id.trailer_item_tv_title);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            String trailerUrlKey = mTrailerData.get(clickedPosition);
            mClickHandler.onClick(trailerUrlKey);
        }
    }

    @NonNull
    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutId = R.layout.trailer_item;
        View view = inflater.inflate(layoutId, parent, false);
        TrailerAdapter.TrailerViewHolder holder = new TrailerAdapter.TrailerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerViewHolder holder, int position) {
        String titleForThisItem = "Trailer " + position;
        holder.mTrailerTitle.setText(titleForThisItem);
    }

    @Override
    public int getItemCount() {
        if(mTrailerData == null) return 0;
        else if(mTrailerData.isEmpty()) return 0;
        return mTrailerData.size();
    }

    public void setTrailerData (List<String> trailerData){
        mTrailerData = trailerData;
        notifyDataSetChanged();
    }
}
