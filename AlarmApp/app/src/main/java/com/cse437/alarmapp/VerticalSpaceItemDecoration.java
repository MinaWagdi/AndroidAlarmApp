package com.cse437.alarmapp;

import android.support.v7.widget.RecyclerView;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration{
    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }
}
