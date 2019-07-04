package com.vasilevkin.movie_tracker_android.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.vasilevkin.movie_tracker_android.network.NetworkState;
import com.vasilevkin.movie_tracker_android.paging.MoviePageKeyedDataSource;

public class RepositoryMoviesResult {
    public LiveData<PagedList<Movie>> data;
    public LiveData<NetworkState> networkState;
    public MutableLiveData<MoviePageKeyedDataSource> sourceLiveData;

    public RepositoryMoviesResult(LiveData<PagedList<Movie>> data,
                                  LiveData<NetworkState> networkState,
                                  MutableLiveData<MoviePageKeyedDataSource> sourceLiveData) {
        this.data = data;
        this.networkState = networkState;
        this.sourceLiveData = sourceLiveData;
    }
}
