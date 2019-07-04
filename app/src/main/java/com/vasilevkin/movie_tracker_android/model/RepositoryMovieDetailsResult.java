package com.vasilevkin.movie_tracker_android.model;

import androidx.lifecycle.LiveData;

import com.vasilevkin.movie_tracker_android.network.NetworkState;

public class RepositoryMovieDetailsResult {
    public LiveData<Movie> data;
    public LiveData<NetworkState> networkState;

    public RepositoryMovieDetailsResult(LiveData<Movie> data,
                                        LiveData<NetworkState> networkState) {
        this.data = data;
        this.networkState = networkState;
    }
}
