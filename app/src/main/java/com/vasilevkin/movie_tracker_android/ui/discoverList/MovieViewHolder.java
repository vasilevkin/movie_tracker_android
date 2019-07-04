package com.vasilevkin.movie_tracker_android.ui.discoverList;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vasilevkin.movie_tracker_android.databinding.ItemMovieBinding;
import com.vasilevkin.movie_tracker_android.model.Movie;
import com.vasilevkin.movie_tracker_android.ui.discoverList.movieDetails.DetailsActivity;
import com.vasilevkin.movie_tracker_android.utils.GlideRequests;

import static com.vasilevkin.movie_tracker_android.utils.Constants.IMAGE_URL;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    private final ItemMovieBinding binding;
    private GlideRequests glide;

    public MovieViewHolder(@NonNull ItemMovieBinding binding, GlideRequests glide) {
        super(binding.getRoot());

        this.binding = binding;
        this.glide = glide;
    }

    void bindTo(final Movie movie) {
        glide.load(IMAGE_URL + movie.getPosterImageUrl())
                .placeholder(android.R.color.black)
                .into(binding.imageMoviePoster);

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.EXTRA_MOVIE_ID, movie.getId());
                view.getContext().startActivity(intent);
            }
        });
        binding.executePendingBindings();
    }

    static MovieViewHolder create(ViewGroup parent, GlideRequests glide) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMovieBinding binding =
                ItemMovieBinding.inflate(layoutInflater, parent, false);
        return new MovieViewHolder(binding, glide);
    }
}
