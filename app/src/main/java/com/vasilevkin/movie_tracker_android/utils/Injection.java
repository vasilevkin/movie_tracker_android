package com.vasilevkin.movie_tracker_android.utils;

import com.vasilevkin.movie_tracker_android.dataRepository.MovieRepository;
import com.vasilevkin.movie_tracker_android.network.HTTPClient;

public class Injection {
    public static MovieRepository provideMovieRepository() {
        return new MovieRepository(
                HTTPClient.getApiServiceInstance(),
                AppExecutors.getInstance()
        );
    }
}
