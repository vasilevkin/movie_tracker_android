package com.vasilevkin.movie_tracker_android.ui.discoverList.movieDetails;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.vasilevkin.movie_tracker_android.dataRepository.MovieRepository;
import com.vasilevkin.movie_tracker_android.model.Movie;
import com.vasilevkin.movie_tracker_android.model.RepositoryMovieDetailsResult;
import com.vasilevkin.movie_tracker_android.network.NetworkState;

public class MovieDetailsViewModel extends ViewModel {
    private final MovieRepository repository;
    private LiveData<RepositoryMovieDetailsResult> resultLiveData;
    private MutableLiveData<Long> movieId = new MutableLiveData<>();
    private LiveData<Movie> movieLiveData;
    private LiveData<NetworkState> networkState;

    public MovieDetailsViewModel(final MovieRepository repository) {
        this.repository = repository;
        resultLiveData = Transformations.map(movieId, new Function<Long, RepositoryMovieDetailsResult>() {
            @Override
            public RepositoryMovieDetailsResult apply(Long input) {
                return repository.getMovieBy(input);
            }
        });
        movieLiveData = Transformations.switchMap(resultLiveData,
                new Function<RepositoryMovieDetailsResult, LiveData<Movie>>() {
                    @Override
                    public LiveData<Movie> apply(RepositoryMovieDetailsResult input) {
                        return input.data;
                    }
                });
        networkState = Transformations.switchMap(resultLiveData,
                new Function<RepositoryMovieDetailsResult, LiveData<NetworkState>>() {
                    @Override
                    public LiveData<NetworkState> apply(RepositoryMovieDetailsResult input) {
                        return input.networkState;
                    }
                });
    }

    LiveData<Movie> getMovieLiveData() {
        return movieLiveData;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    void setMovieId(long movieId) {
        this.movieId.setValue(movieId);
    }

    void retry(long movieId) {
        setMovieId(movieId);
    }
}
