package com.vasilevkin.movie_tracker_android.paging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.vasilevkin.movie_tracker_android.model.Movie;
import com.vasilevkin.movie_tracker_android.network.ApiThemoviedb;
import com.vasilevkin.movie_tracker_android.network.MoviesResponse;
import com.vasilevkin.movie_tracker_android.network.NetworkState;
import com.vasilevkin.movie_tracker_android.ui.discoverList.MoviesFilterType;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviePageKeyedDataSource extends PageKeyedDataSource<Integer, Movie> {

    private static final int FIRST_PAGE = 1;

    public MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
    public MutableLiveData<NetworkState> initialLoad = new MutableLiveData<>();

    private final ApiThemoviedb apiThemoviedb;

    private final Executor networkExecutor;

    private final MoviesFilterType sortBy;

    public RetryCallback retryCallback = null;

    public MoviePageKeyedDataSource(ApiThemoviedb apiThemoviedb,
                                    Executor networkExecutor, MoviesFilterType sortBy) {
        this.apiThemoviedb = apiThemoviedb;
        this.networkExecutor = networkExecutor;
        this.sortBy = sortBy;
    }

    @Override
    public void loadInitial(@NonNull final PageKeyedDataSource.LoadInitialParams<Integer> params,
                            @NonNull final PageKeyedDataSource.LoadInitialCallback<Integer, Movie> callback) {
        networkState.postValue(NetworkState.LOADING);
        initialLoad.postValue(NetworkState.LOADING);

        Call<MoviesResponse> request;
        if (sortBy == MoviesFilterType.POPULAR) {
            request = apiThemoviedb.fetchPopularMoviesFor(FIRST_PAGE);
        } else {
            request = apiThemoviedb.fetchTopRatedMoviesFor(FIRST_PAGE);
        }

        try {
            Response<MoviesResponse> response = request.execute();
            MoviesResponse data = response.body();
            List<Movie> movieList =
                    null != data ? data.getMovies() : Collections.<Movie>emptyList();

            retryCallback = null;
            networkState.postValue(NetworkState.LOADED);
            initialLoad.postValue(NetworkState.LOADED);
            callback.onResult(movieList, null, FIRST_PAGE + 1);
        } catch (IOException exception) {
            retryCallback = new RetryCallback() {
                @Override
                public void invoke() {
                    networkExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            loadInitial(params, callback);
                        }
                    });

                }
            };
            NetworkState error = NetworkState.error(exception.getMessage());
            networkState.postValue(error);
            initialLoad.postValue(error);
        }
    }

    @Override
    public void loadBefore(@NonNull PageKeyedDataSource.LoadParams<Integer> params,
                           @NonNull PageKeyedDataSource.LoadCallback<Integer, Movie> callback) {
    }

    @Override
    public void loadAfter(@NonNull final PageKeyedDataSource.LoadParams<Integer> params,
                          @NonNull final PageKeyedDataSource.LoadCallback<Integer, Movie> callback) {
        networkState.postValue(NetworkState.LOADING);

        Call<MoviesResponse> request;
        if (sortBy == MoviesFilterType.POPULAR) {
            request = apiThemoviedb.fetchPopularMoviesFor(params.key);
        } else {
            request = apiThemoviedb.fetchTopRatedMoviesFor(params.key);
        }

        request.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    MoviesResponse data = response.body();
                    List<Movie> movieList =
                            null != data ? data.getMovies() : Collections.<Movie>emptyList();

                    retryCallback = null;
                    callback.onResult(movieList, params.key + 1);
                    networkState.postValue(NetworkState.LOADED);
                } else {
                    retryCallback = new RetryCallback() {
                        @Override
                        public void invoke() {
                            loadAfter(params, callback);
                        }
                    };
                    networkState.postValue(
                            NetworkState.error("error code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable throwable) {
                retryCallback = new RetryCallback() {
                    @Override
                    public void invoke() {
                        networkExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                loadAfter(params, callback);
                            }
                        });
                    }
                };
                networkState.postValue(
                        NetworkState.error(null != throwable ? throwable.getMessage() : "Unexpected error"));
            }
        });
    }

    public interface RetryCallback {
        void invoke();
    }
}
