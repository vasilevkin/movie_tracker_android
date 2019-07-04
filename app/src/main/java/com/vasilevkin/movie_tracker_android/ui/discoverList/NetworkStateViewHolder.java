package com.vasilevkin.movie_tracker_android.ui.discoverList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vasilevkin.movie_tracker_android.databinding.ItemNetworkStateBinding;
import com.vasilevkin.movie_tracker_android.network.NetworkState;

import static com.vasilevkin.movie_tracker_android.network.Status.FAILED;
import static com.vasilevkin.movie_tracker_android.network.Status.INPROGRESS;

public class NetworkStateViewHolder extends RecyclerView.ViewHolder {

    private ItemNetworkStateBinding binding;

    public NetworkStateViewHolder(@NonNull ItemNetworkStateBinding binding,
                                  final MoviesViewModel viewModel) {
        super(binding.getRoot());
        this.binding = binding;

        binding.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.retry();
            }
        });
    }

    static NetworkStateViewHolder create(ViewGroup parent, MoviesViewModel viewModel) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ItemNetworkStateBinding binding =
                ItemNetworkStateBinding.inflate(layoutInflater, parent, false);
        return new NetworkStateViewHolder(binding, viewModel);
    }

    void bindTo(NetworkState networkState) {
        binding.progressBar.setVisibility(
                isVisible(networkState.getStatus() == INPROGRESS));
        binding.retryButton.setVisibility(
                isVisible(networkState.getStatus() == FAILED));
        binding.errorMessage.setVisibility(
                isVisible(networkState.getNetworkMessage() != null));
        binding.errorMessage.setText(networkState.getNetworkMessage());
    }

    private int isVisible(boolean condition) {
        if (condition)
            return View.VISIBLE;
        else
            return View.GONE;
    }
}