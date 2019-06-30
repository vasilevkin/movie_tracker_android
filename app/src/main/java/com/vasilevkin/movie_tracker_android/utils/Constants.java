package com.vasilevkin.movie_tracker_android.utils;

public class Constants {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w342";
    private static final String BACKDROP_SIZE = "w780";
    public static final String IMAGE_URL = IMAGE_BASE_URL + IMAGE_SIZE;
    public static final String BACKDROP_URL = IMAGE_BASE_URL + BACKDROP_SIZE;

    static final int THREAD_COUNT = 5;
    public static final int PAGE_SIZE = 20;
}
