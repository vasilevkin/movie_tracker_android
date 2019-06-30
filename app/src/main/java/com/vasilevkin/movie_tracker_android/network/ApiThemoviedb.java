package com.vasilevkin.movie_tracker_android.network;

import com.vasilevkin.movie_tracker_android.model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiThemoviedb {
    @GET("movie/popular")
    Call<MoviesResponse> fetchPopularMoviesFor(@Query("page") int page);

    @GET("movie/top_rated")
    Call<MoviesResponse> fetchTopRatedMoviesFor(@Query("page") int page);

    @GET("movie/{id}")
    Call<Movie> fetchMovieDetails(@Path("id") long id);
}
