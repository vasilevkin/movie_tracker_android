package com.vasilevkin.movie_tracker_android.paging;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.vasilevkin.movie_tracker_android.model.Movie;
import com.vasilevkin.movie_tracker_android.network.ApiThemoviedb;
import com.vasilevkin.movie_tracker_android.ui.discoverList.MoviesFilterType;

import java.util.concurrent.Executor;

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {
    private final ApiThemoviedb apiThemoviedb;
    private final Executor networkExecutor;
    private final MoviesFilterType sortBy;

    public MutableLiveData<MoviePageKeyedDataSource> sourceLiveData = new MutableLiveData<>();

    public MovieDataSourceFactory(ApiThemoviedb apiThemoviedb,
                                  Executor networkExecutor, MoviesFilterType sortBy) {
        this.apiThemoviedb = apiThemoviedb;
        this.sortBy = sortBy;
        this.networkExecutor = networkExecutor;
    }

    @Override
    public DataSource<Integer, Movie> create() {
        MoviePageKeyedDataSource movieDataSource =
                new MoviePageKeyedDataSource(apiThemoviedb, networkExecutor, sortBy);
        sourceLiveData.postValue(movieDataSource);
        return movieDataSource;
    }
}
