package com.vasilevkin.movie_tracker_android.dataRepository;

import com.vasilevkin.movie_tracker_android.model.RepositoryMovieDetailsResult;
import com.vasilevkin.movie_tracker_android.model.RepositoryMoviesResult;
import com.vasilevkin.movie_tracker_android.ui.discoverList.MoviesFilterType;

public interface DataSource {

    RepositoryMovieDetailsResult getMovieBy(long movieId);

    RepositoryMoviesResult getMoviesFilteredBy(MoviesFilterType sortBy);

}
