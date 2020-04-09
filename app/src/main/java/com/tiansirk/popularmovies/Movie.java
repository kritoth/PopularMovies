package com.tiansirk.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Class for Movie objects to be presented to the user.
 */
public class Movie implements Parcelable {
    private String posterImgUrl;
    private String plotSynopsis;
    private String releaseDate;
    private String title;
    private double userRating;

    @Override
    public int describeContents() {
        return 0;
    }
    /**
     * Write object's data to the passed-in Parcel
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(posterImgUrl);
        parcel.writeString(plotSynopsis);
        parcel.writeString(releaseDate);
        parcel.writeString(title);
        parcel.writeDouble(userRating);
    }
    /**
     * This is used to regenerate Movie object. All Parcelables must have a CREATOR that implements these two methods
      */
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    // Parcelling part of constructor. Must maintain the same order of the fields
    public Movie(Parcel in){
        this.posterImgUrl = in.readString();
        this.plotSynopsis = in.readString();
        this.releaseDate = in.readString();
        this.title = in.readString();
        this.userRating = in.readDouble();
    }

    public Movie(String posterPath, String overview, String releaseDate, String originalTitle, double voteAverage){
        this.posterImgUrl = posterPath;
        this.plotSynopsis = overview;
        this.releaseDate = releaseDate;
        this.title = originalTitle;
        this.userRating = voteAverage;
    }

    public String getPosterImgUrl() {
        return posterImgUrl;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public double getUserRating() {
        return userRating;
    }

    private boolean hasImage(){
        if(posterImgUrl.isEmpty()) return false;
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return "\n Title: " + getTitle() +
                "\n Has a poster: " + hasImage() +
                "\n Plot synopsis: " + getPlotSynopsis().substring(0,100) + "..." +
                "\n Release date: " + getReleaseDate() +
                "\n User rating: " + getUserRating();
    }

}
