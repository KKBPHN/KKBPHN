package com.android.camera.visibilityutils.scroll_utils;

import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewItemPositionGetter implements ItemsPositionGetter {
    private static final boolean SHOW_LOGS = false;
    private static final String TAG = "RecyclerViewItemPositionGetter";
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    public RecyclerViewItemPositionGetter(LinearLayoutManager linearLayoutManager, RecyclerView recyclerView) {
        this.mLayoutManager = linearLayoutManager;
        this.mRecyclerView = recyclerView;
    }

    public View getChildAt(int i) {
        return this.mLayoutManager.getChildAt(i);
    }

    public int getChildCount() {
        return this.mRecyclerView.getChildCount();
    }

    public int getFirstVisiblePosition() {
        return this.mLayoutManager.findFirstVisibleItemPosition();
    }

    public int getLastVisiblePosition() {
        return this.mLayoutManager.findLastVisibleItemPosition();
    }

    public int indexOfChild(View view) {
        return this.mRecyclerView.indexOfChild(view);
    }
}
