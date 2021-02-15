package com.android.camera.fragment.vv.page;

import android.util.DisplayMetrics;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.SmoothScroller.Action;
import androidx.recyclerview.widget.RecyclerView.State;

public class PagerGridSmoothScroller extends LinearSmoothScroller {
    private RecyclerView mRecyclerView;

    public PagerGridSmoothScroller(@NonNull RecyclerView recyclerView) {
        super(recyclerView.getContext());
        this.mRecyclerView = recyclerView;
    }

    /* access modifiers changed from: protected */
    public float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return PagerConfig.getMillisecondsPreInch() / ((float) displayMetrics.densityDpi);
    }

    /* access modifiers changed from: protected */
    public void onTargetFound(View view, State state, Action action) {
        LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (layoutManager != null && (layoutManager instanceof PagerGridLayoutManager)) {
            int[] snapOffset = ((PagerGridLayoutManager) layoutManager).getSnapOffset(this.mRecyclerView.getChildAdapterPosition(view));
            int i = snapOffset[0];
            int i2 = snapOffset[1];
            int calculateTimeForScrolling = calculateTimeForScrolling(Math.max(Math.abs(i), Math.abs(i2))) * 2;
            if (calculateTimeForScrolling > 0) {
                action.update(i, i2, calculateTimeForScrolling, this.mDecelerateInterpolator);
            }
        }
    }
}
