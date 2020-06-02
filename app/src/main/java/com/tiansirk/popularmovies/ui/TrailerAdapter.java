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
    private OnTrailerItemClicked mOnClick;

    /**
     * The interface that receives onClick messages.
     */
    public interface OnTrailerItemClicked {
        void onItemClick(String clickedTrailerUrl);
    }

    public TrailerAdapter(Movie movie) {
        mTrailerData = new ArrayList<>();
        mTrailerData.addAll(movie.getVideoKeys());
        Log.d(TAG, "Trailers loaded into Adapter: " + mTrailerData.size());
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder{
        public final ImageView mPlayButtonView;
        public final TextView mTrailerTitle;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            mPlayButtonView = itemView.findViewById(R.id.trailer_item_iv_play_button);
            mTrailerTitle = itemView.findViewById(R.id.trailer_item_tv_title);
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
    public void onBindViewHolder(@NonNull final TrailerAdapter.TrailerViewHolder holder, int position) {
        String titleForThisItem = "Trailer " + position+1;
        holder.mTrailerTitle.setText(titleForThisItem);
        holder.mPlayButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                String trailerUrlKey = mTrailerData.get(clickedPosition);
                mOnClick.onItemClick(trailerUrlKey);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mTrailerData == null) return 0;
        else if(mTrailerData.isEmpty()) return 0;
        return mTrailerData.size();
    }

    public void setOnClick(OnTrailerItemClicked onClick){
        this.mOnClick = onClick;
    }

    public void setTrailerData (List<String> trailerData){
        mTrailerData = trailerData;
        notifyDataSetChanged();
    }
}
