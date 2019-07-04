package com.vasilevkin.movie_tracker_android.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vasilevkin.movie_tracker_android.dataRepository.MovieRepository;
import com.vasilevkin.movie_tracker_android.ui.discoverList.MoviesViewModel;
import com.vasilevkin.movie_tracker_android.ui.discoverList.movieDetails.MovieDetailsViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final MovieRepository repository;

    public static ViewModelFactory getInstance(MovieRepository repository) {
        return new ViewModelFactory(repository);
    }

    private ViewModelFactory(MovieRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MoviesViewModel.class)) {
            return (T) new MoviesViewModel(repository);
        } else if (modelClass.isAssignableFrom(MovieDetailsViewModel.class)) {
            return (T) new MovieDetailsViewModel(repository);
        }
        throw new IllegalArgumentException("Error: Unknown ViewModel class: " + modelClass.getName());
    }
}
