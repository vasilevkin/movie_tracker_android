package com.vasilevkin.movie_tracker_android.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    private int movieItemOffset;

    public ItemOffsetDecoration(int itemOffset) {
        movieItemOffset = itemOffset;
    }

    public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context
                .getResources()
                .getDimensionPixelSize(itemOffsetId)
        );
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(
                outRect,
                view,
                parent,
                state
        );

        outRect.set(
                movieItemOffset,
                movieItemOffset,
                movieItemOffset,
                movieItemOffset
        );
    }
}
