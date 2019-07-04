package com.vasilevkin.movie_tracker_android.ui.discoverList;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.vasilevkin.movie_tracker_android.R;
import com.vasilevkin.movie_tracker_android.dataRepository.MovieRepository;
import com.vasilevkin.movie_tracker_android.model.Movie;
import com.vasilevkin.movie_tracker_android.model.RepositoryMoviesResult;
import com.vasilevkin.movie_tracker_android.network.NetworkState;

public class MoviesViewModel extends ViewModel {
    private final MovieRepository movieRepository;
    private LiveData<RepositoryMoviesResult> repoMoviesResult;
    private LiveData<PagedList<Movie>> pagedList;
    private LiveData<NetworkState> networkState;
    private MutableLiveData<Integer> currentTitle = new MutableLiveData<>();
    private MutableLiveData<MoviesFilterType> sortBy = new MutableLiveData<>();

    public MoviesViewModel(final MovieRepository movieRepository) {
        this.movieRepository = movieRepository;

        sortBy.setValue(MoviesFilterType.POPULAR);
        currentTitle.setValue(R.string.action_popular);

        repoMoviesResult = Transformations.map(sortBy, new Function<MoviesFilterType, RepositoryMoviesResult>() {
            @Override
            public RepositoryMoviesResult apply(MoviesFilterType sort) {
                return movieRepository.getMoviesFilteredBy(sort);
            }
        });
        pagedList = Transformations.switchMap(repoMoviesResult,
                new Function<RepositoryMoviesResult, LiveData<PagedList<Movie>>>() {
                    @Override
                    public LiveData<PagedList<Movie>> apply(RepositoryMoviesResult input) {
                        return input.data;
                    }
                });
        networkState = Transformations.switchMap(repoMoviesResult,
                new Function<RepositoryMoviesResult, LiveData<NetworkState>>() {
                    @Override
                    public LiveData<NetworkState> apply(RepositoryMoviesResult input) {
                        return input.networkState;
                    }
                });
    }

    LiveData<PagedList<Movie>> getPagedList() {
        return pagedList;
    }

    LiveData<NetworkState> getNetWorkState() {
        return networkState;
    }

    MoviesFilterType getCurrentSorting() {
        return sortBy.getValue();
    }

    public LiveData<Integer> getCurrentTitle() {
        return currentTitle;
    }

    void setSortMoviesBy(int id) {
        MoviesFilterType sort = null;
        Integer title = null;
        switch (id) {
            case R.id.action_popular_movies: {
                if (sortBy.getValue() == MoviesFilterType.POPULAR)
                    return;
                sort = MoviesFilterType.POPULAR;
                title = R.string.action_popular;
                break;
            }
            case R.id.action_top_rated: {
                if (sortBy.getValue() == MoviesFilterType.TOP_RATED)
                    return;
                sort = MoviesFilterType.TOP_RATED;
                title = R.string.action_top_rated;
                break;
            }
        }
        sortBy.setValue(sort);
        currentTitle.setValue(title);
    }

    void retry() {
        repoMoviesResult.getValue()
                .sourceLiveData.getValue()
                .retryCallback.invoke();
    }
}
