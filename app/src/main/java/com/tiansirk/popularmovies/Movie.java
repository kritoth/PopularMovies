package com.tiansirk.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

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
    private List<String> videoKeys;
    private List<String> reviews;
    private int onlineId;

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
        parcel.writeList(videoKeys);
        parcel.writeList(reviews);
        parcel.writeInt(onlineId);
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

    // Parcelling part of constructor. Reads from the Parcel that was written into above Must maintain the same order of the fields
    public Movie(Parcel in){
        this.posterImgUrl = in.readString();
        this.plotSynopsis = in.readString();
        this.releaseDate = in.readString();
        this.title = in.readString();
        this.userRating = in.readDouble();
        in.readArrayList(null);
        in.readArrayList(null);
        this.onlineId = in.readInt();
    }

    /**
     * Constructor for Movie item. If any of the required params are not existing then an empty object of that type needed to be inserted
     * @param posterPath Complete URL path for an image resource as String
     * @param overview String with any length
     * @param releaseDate String with any length
     * @param originalTitle String with any length
     * @param voteAverage a double
     * @param videoKeys A List of Strings with any length or empty List
     * @param reviews A List of Strings with any length or empty List
     */
    public Movie(String posterPath, String overview, String releaseDate, String originalTitle, double voteAverage,
                 List<String> videoKeys, List<String> reviews, int id){
        this.posterImgUrl = posterPath;
        this.plotSynopsis = overview;
        this.releaseDate = releaseDate;
        this.title = originalTitle;
        this.userRating = voteAverage;
        this.videoKeys = videoKeys;
        this.reviews = reviews;
        this.onlineId = id;
    }

    /**
     * @return a String representing a poster path URL from where the poster image can be fetched
     * the complete path consists of the basic path: "http://image.tmdb.org/t/p/w185/" and then the "relativePath"
     */
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

    /**
     * A List of _key_ Strings of video resources available on youtube. The _key_ can be added to the URL of the
     * youtube link of "https://www.youtube.com/watch?v={ID}" to get the video played with/on youtube (app/web).
     * @return List of Strings representing youtube video _key_
     */
    public List<String> getVideoKeys() {
        return videoKeys;
    }

    /**
     * @return List of reviews as Strings
     */
    public List<String> getReviews() {
        return reviews;
    }

    public int getOnlineId() {
        return onlineId;
    }

    private boolean hasImage(){
        return !posterImgUrl.isEmpty();
    }

    private boolean hasVideoKeys(){
        return !videoKeys.isEmpty();
    }

    private boolean hasReviews(){
        return !reviews.isEmpty();
    }

    @NonNull
    @Override
    public String toString() {
        return "\n Title: " + getTitle() +
                "\n Has a poster: " + hasImage() +
                "\n Plot synopsis: " + getPlotSynopsis().substring(0,21) + "..." +
                "\n Release date: " + getReleaseDate() +
                "\n User rating: " + getUserRating() +
                "\n No. of Videos: " + getVideoKeys().size() +
                "\n No. of reviews: " + getReviews().size() +
                "\n TMDB id: " + getOnlineId();
    }

}
