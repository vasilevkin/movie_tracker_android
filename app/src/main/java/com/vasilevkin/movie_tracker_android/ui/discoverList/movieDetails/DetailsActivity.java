package com.vasilevkin.movie_tracker_android.ui.discoverList.movieDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.appbar.AppBarLayout;
import com.vasilevkin.movie_tracker_android.R;
import com.vasilevkin.movie_tracker_android.model.Movie;
import com.vasilevkin.movie_tracker_android.network.NetworkState;
import com.vasilevkin.movie_tracker_android.utils.Constants;
import com.vasilevkin.movie_tracker_android.utils.GlideApp;
import com.vasilevkin.movie_tracker_android.utils.Injection;
import com.vasilevkin.movie_tracker_android.utils.ViewModelFactory;
import com.vasilevkin.movie_tracker_android.databinding.ActivityDetailsBinding;

import static com.vasilevkin.movie_tracker_android.network.Status.FAILED;
import static com.vasilevkin.movie_tracker_android.network.Status.INPROGRESS;
import static com.vasilevkin.movie_tracker_android.utils.Constants.DEFAULT_ID;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding movieDetailsBinding;
    private MovieDetailsViewModel movieDetailsViewModel;

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        Intent intent = getIntent();
        if (null == intent) {
            closeOnError();
        }
        final long movieId = intent.getLongExtra(EXTRA_MOVIE_ID, DEFAULT_ID);
        if (movieId == DEFAULT_ID) {
            closeOnError();
        }
        setupToolbar();

        movieDetailsViewModel = obtainViewModel();
        if (null == savedInstanceState) {
            movieDetailsViewModel.setMovieId(movieId);
        }
        movieDetailsViewModel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                handleNetworkState(networkState);
            }
        });
        movieDetailsViewModel.getMovieLiveData().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                updateDetailsUi(movie);
            }
        });
        movieDetailsBinding.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieDetailsViewModel.retry(movieId);
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = movieDetailsBinding.toolbar;
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            handleCollapsedToolbarTitle();
        }
    }

    private void updateDetailsUi(Movie movie) {
        GlideApp.with(this)
                .load(Constants.BACKDROP_URL + movie.getBackdropPath())
                .into(movieDetailsBinding.imageMovieBackdrop);

        GlideApp.with(this)
                .load(Constants.IMAGE_URL + movie.getPosterImageUrl())
                .into(movieDetailsBinding.imagePoster);

        movieDetailsBinding.textTitle.setText(movie.getTitle());
        movieDetailsBinding.textReleaseDate.setText(movie.getReleaseDate());
        movieDetailsBinding.textVote.setText(String.valueOf(movie.getVoteAverage()));
        movieDetailsBinding.textOverview.setText(movie.getOverview());
        movieDetailsBinding.textStatus.setText(movie.getStatus());
        movieDetailsBinding.textVoteCount.setText(String.valueOf(movie.getVoteCount()));
        movieDetailsBinding.executePendingBindings();
    }

    private MovieDetailsViewModel obtainViewModel() {
        ViewModelFactory factory = ViewModelFactory.getInstance(Injection.provideMovieRepository());
        return ViewModelProviders.of(this, factory).get(MovieDetailsViewModel.class);
    }

    private void closeOnError() {
        throw new RuntimeException("Runtime error: Access denied");
    }

    private void handleCollapsedToolbarTitle() {
        movieDetailsBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    movieDetailsBinding.collapsingToolbar.setTitle(
                            movieDetailsViewModel
                                    .getMovieLiveData()
                                    .getValue()
                                    .getTitle()
                    );
                    isShow = true;
                } else if (isShow) {
                    movieDetailsBinding.collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void handleNetworkState(NetworkState networkState) {
        boolean isLoaded = networkState == NetworkState.LOADED;
        movieDetailsBinding.appbar.setVisibility(isVisible(isLoaded));
        movieDetailsBinding.movieDetails.setVisibility(isVisible(isLoaded));
        movieDetailsBinding.progressBar.setVisibility(
                isVisible(networkState.getStatus() == INPROGRESS));
        movieDetailsBinding.retryButton.setVisibility(
                isVisible(networkState.getStatus() == FAILED));
        movieDetailsBinding.errorMessage.setVisibility(
                isVisible(networkState.getNetworkMessage() != null));
        movieDetailsBinding.errorMessage.setText(networkState.getNetworkMessage());
    }

    private int isVisible(boolean condition) {
        if (condition)
            return View.VISIBLE;
        else
            return View.GONE;
    }
}
