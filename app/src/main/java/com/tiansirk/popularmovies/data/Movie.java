package com.tiansirk.popularmovies.data;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.tiansirk.popularmovies.util.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Class for Movie objects to be presented to the user.
 */
@Entity(tableName = "movies_table")
public class Movie implements Parcelable {

    private String posterImgUrl;
    private String plotSynopsis;
    private String releaseDate;
    private String title;
    private double userRating;
    @Ignore
    private List<String> videoKeys;
    @Ignore
    private List<String> reviews;
    @PrimaryKey
    @NonNull
    private int onlineId;
    private long dateAddedToFav;
    boolean isFavorite;

    public Movie() {
    }

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
        parcel.writeLong(dateAddedToFav);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            parcel.writeBoolean(isFavorite);
        }
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

    // Parcelling part of constructor. Reads from the Parcel that was written into above. Must maintain the same order of the fields
    @Ignore
    public Movie(Parcel in){
        this.posterImgUrl = in.readString();
        this.plotSynopsis = in.readString();
        this.releaseDate = in.readString();
        this.title = in.readString();
        this.userRating = in.readDouble();
        videoKeys = new ArrayList<String>();
        in.readList(videoKeys, Movie.class.getClassLoader());
        reviews = new ArrayList<String>();
        in.readList(reviews, Movie.class.getClassLoader());
        this.onlineId = in.readInt();
        this.dateAddedToFav = in.readLong();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.isFavorite = in.readBoolean();
        }
    }

    /**
     * Constructor for Movie item. If any of the required params are not existing then an empty object of that type needed to be inserted.
     * At creating the object its field of isFavorite is going to be false and its field of dateAdded are going to be NOT initialized.
     * @param posterPath Complete URL path for an image resource as String
     * @param overview String with any length
     * @param releaseDate String with any length
     * @param originalTitle String with any length
     * @param voteAverage a double
     * @param videoKeys A List of Strings with any length or empty List
     * @param reviews A List of Strings with any length or empty List
     * @param onlineId The id of the Movie in TMDB
     */
    @Ignore
    public Movie(String posterPath, String overview, String releaseDate, String originalTitle, double voteAverage,
                 List<String> videoKeys, List<String> reviews, int onlineId){
        this.posterImgUrl = posterPath;
        this.plotSynopsis = overview;
        this.releaseDate = releaseDate;
        this.title = originalTitle;
        this.userRating = voteAverage;
        this.videoKeys = new ArrayList<>();
        this.videoKeys.addAll(videoKeys);
        this.reviews = new ArrayList<>();
        this.reviews.addAll(reviews);
        this.onlineId = onlineId;
        this.isFavorite = false;
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
     * youtube link of "https://www.youtube.com/watch?v={_key_}" to get the video played with/on youtube (app/web).
     * @return List of Strings representing youtube video _key_
     */
    public List<String> getVideoKeys() {
        return videoKeys;
    }

    /**
     * @return List of reviews as Strings as author+DELIMITER+content
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

    public long getDateAddedToFav() {
        return dateAddedToFav;
    }

    public void setDateAddedToFav(long dateAddedToFav) {
        this.dateAddedToFav = dateAddedToFav;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setPosterImgUrl(String posterImgUrl) {
        this.posterImgUrl = posterImgUrl;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public void setOnlineId(int onlineId) {
        this.onlineId = onlineId;
    }

    @NonNull
    @Override
    public String toString() {
        return "\n Title: " + getTitle() +
                "\n Has a poster: " + hasImage() +
                "\n Plot synopsis: " + getPlotSynopsis().substring(0,9) + "..." +
                "\n Release date: " + getReleaseDate() +
                "\n User rating: " + getUserRating() +
                "\n No. of Videos: " + getVideoKeys().size() +
                "\n No. of reviews: " + getReviews().size() +
                "\n TMDB id: " + getOnlineId() +
                "\n It is a favorite: " + isFavorite();
    }

}
