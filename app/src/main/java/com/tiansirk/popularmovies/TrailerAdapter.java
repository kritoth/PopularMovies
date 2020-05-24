package com.tiansirk.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private List<String> mTrailerData;

    public TrailerAdapter() {
        mTrailerData = new ArrayList<>();
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
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerViewHolder holder, int position) {
        String titleForThisItem = mTrailerData.get(position);
        holder.mTrailerTitle.setText(titleForThisItem);
    }

    @Override
    public int getItemCount() {
        if(mTrailerData.isEmpty()) return 0;
        return mTrailerData.size();
    }

    public void setTrailerData (List<String> trailerData){
        mTrailerData = trailerData;
        notifyDataSetChanged();
    }
}
