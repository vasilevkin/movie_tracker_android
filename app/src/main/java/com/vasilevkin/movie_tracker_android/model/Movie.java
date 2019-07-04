package com.vasilevkin.movie_tracker_android.model;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("budget")
    private int budget;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("overview")
    private String overview;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("poster_path")
    private String posterImageUrl;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("status")
    private String status;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("vote_count")
    private int voteCount;

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (null == object || getClass() != object.getClass())
            return false;

        Movie movie = (Movie) object;
        return id == movie.id
                && Objects.equals(title, movie.title)
                && Boolean.compare(adult, movie.adult) == 0
                && budget == movie.budget
                && Objects.equals(backdropPath, movie.backdropPath)
                && Objects.equals(overview, movie.overview)
                && Double.compare(popularity, movie.popularity) == 0
                && Objects.equals(posterImageUrl, movie.posterImageUrl)
                && Objects.equals(releaseDate, movie.releaseDate)
                && Objects.equals(status, movie.status)
                && Double.compare(voteAverage, movie.voteAverage) == 0
                && Objects.equals(voteCount, movie.voteCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                title,
                adult,
                budget,
                backdropPath,
                overview,
                popularity,
                posterImageUrl,
                releaseDate,
                status,
                voteAverage,
                voteCount
        );
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    public void setPosterImageUrl(String posterImageUrl) {
        this.posterImageUrl = posterImageUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
