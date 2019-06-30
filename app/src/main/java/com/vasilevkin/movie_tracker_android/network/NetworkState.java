package com.vasilevkin.movie_tracker_android.network;

public class NetworkState {
    private Status status;
    private String networkMessage;

    public static final NetworkState LOADED = new NetworkState(Status.SUCCESS, null);
    public static final NetworkState LOADING = new NetworkState(Status.INPROGRESS, null);

    private NetworkState(Status status, String networkMessage) {
        this.status = status;
        this.networkMessage = networkMessage;
    }

    public static NetworkState error(String msg) {
        return new NetworkState(Status.FAILED, msg);
    }

    public Status getStatus() {
        return status;
    }

    public String getNetworkMessage() {
        return networkMessage;
    }
}


