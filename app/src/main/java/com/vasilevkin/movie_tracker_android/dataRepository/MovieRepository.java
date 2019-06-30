package com.vasilevkin.movie_tracker_android.dataRepository;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.vasilevkin.movie_tracker_android.model.Movie;
import com.vasilevkin.movie_tracker_android.model.RepositoryMovieDetailsResult;
import com.vasilevkin.movie_tracker_android.model.RepositoryMoviesResult;
import com.vasilevkin.movie_tracker_android.network.ApiThemoviedb;
import com.vasilevkin.movie_tracker_android.network.NetworkState;
import com.vasilevkin.movie_tracker_android.paging.MovieDataSourceFactory;
import com.vasilevkin.movie_tracker_android.paging.MoviePageKeyedDataSource;
import com.vasilevkin.movie_tracker_android.ui.discoverList.MoviesFilterType;
import com.vasilevkin.movie_tracker_android.utils.AppExecutors;

import java.io.IOException;

import retrofit2.Response;

import static com.vasilevkin.movie_tracker_android.utils.Constants.PAGE_SIZE;

public class MovieRepository implements DataSource {
    private final ApiThemoviedb movieApiThemoviedb;
    private final AppExecutors movieExecutors;

    public MovieRepository(ApiThemoviedb apiThemoviedb,
                           AppExecutors executors) {
        movieApiThemoviedb = apiThemoviedb;
        movieExecutors = executors;
    }

    @Override
    public RepositoryMovieDetailsResult getMovieBy(final long movieId) {
        final MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
        final MutableLiveData<Movie> movieLiveData = new MutableLiveData<>();

        networkState.setValue(NetworkState.LOADING);

        movieExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<Movie> response = movieApiThemoviedb
                            .fetchMovieDetails(movieId).execute();
                    networkState.postValue(NetworkState.LOADED);
                    movieLiveData.postValue(response.body());
                } catch (IOException e) {
                    NetworkState error = NetworkState.error(e.getMessage());
                    networkState.postValue(error);
                }
            }
        });

        return new RepositoryMovieDetailsResult(movieLiveData, networkState);
    }

    @Override
    public RepositoryMoviesResult getMoviesFilteredBy(MoviesFilterType sortBy) {
        MovieDataSourceFactory sourceFactory =
                new MovieDataSourceFactory(movieApiThemoviedb, movieExecutors.networkIO(), sortBy);

        // paging configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .build();

        // Get the paged list
        LiveData<PagedList<Movie>> moviesPagedList =
                new LivePagedListBuilder<>(sourceFactory, config)
                        .setFetchExecutor(movieExecutors.networkIO())
                        .build();

        LiveData<NetworkState> networkState = Transformations.switchMap(
                sourceFactory.sourceLiveData,
                new Function<MoviePageKeyedDataSource, LiveData<NetworkState>>() {
                    @Override
                    public LiveData<NetworkState> apply(MoviePageKeyedDataSource input) {
                        return input.networkState;
                    }
                });

        return new RepositoryMoviesResult(
                moviesPagedList,
                networkState,
                sourceFactory.sourceLiveData
        );
    }
}