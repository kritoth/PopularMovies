package com.tiansirk.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiansirk.popularmovies.util.MoviesUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private List<String> mReviewData;

    public ReviewAdapter(Movie movie) {
        mReviewData = new ArrayList<>();
        mReviewData.addAll(movie.getReviews());
        Log.d(TAG, "Reviews loaded into Adapter: " + mReviewData.size());
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        public final TextView mReviewAuthorTextView;
        public final TextView mReviewContentTextView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            mReviewAuthorTextView = itemView.findViewById(R.id.review_item_author);
            mReviewContentTextView = itemView.findViewById(R.id.review_item_content);
        }
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutId = R.layout.review_item;
        View view  = inflater.inflate(layoutId, parent, false);
        ReviewAdapter.ReviewViewHolder holder = new ReviewAdapter.ReviewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        String s [] = mReviewData.get(position).split(MoviesUtils.DELIMITER);
        Log.d(TAG, "Author and content: " + s[0] + s[1]);
        holder.mReviewAuthorTextView.setText(s[0]);
        holder.mReviewContentTextView.setText(s[1]);
    }

    @Override
    public int getItemCount() {
        if(mReviewData == null) return 0;
        else if(mReviewData.isEmpty()) return 0;
        else return mReviewData.size();
    }

    public void setReviewData (List<String> reviewData){

        mReviewData = reviewData;
        notifyDataSetChanged();
    }
}
