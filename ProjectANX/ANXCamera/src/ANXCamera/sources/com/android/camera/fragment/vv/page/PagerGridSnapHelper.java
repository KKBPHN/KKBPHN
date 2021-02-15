package com.android.camera.fragment.vv.page;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.SmoothScroller.ScrollVectorProvider;
import androidx.recyclerview.widget.SnapHelper;

public class PagerGridSnapHelper extends SnapHelper {
    private RecyclerView mRecyclerView;

    private boolean snapFromFling(@NonNull LayoutManager layoutManager, int i, int i2) {
        if (!(layoutManager instanceof ScrollVectorProvider)) {
            return false;
        }
        LinearSmoothScroller createSnapScroller = createSnapScroller(layoutManager);
        if (createSnapScroller == null) {
            return false;
        }
        int findTargetSnapPosition = findTargetSnapPosition(layoutManager, i, i2);
        if (findTargetSnapPosition == -1) {
            return false;
        }
        createSnapScroller.setTargetPosition(findTargetSnapPosition);
        layoutManager.startSmoothScroll(createSnapScroller);
        return true;
    }

    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        super.attachToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @Nullable
    public int[] calculateDistanceToFinalSnap(@NonNull LayoutManager layoutManager, @NonNull View view) {
        return layoutManager instanceof PagerGridLayoutManager ? ((PagerGridLayoutManager) layoutManager).getSnapOffset(layoutManager.getPosition(view)) : new int[2];
    }

    /* access modifiers changed from: protected */
    public LinearSmoothScroller createSnapScroller(LayoutManager layoutManager) {
        if (!(layoutManager instanceof ScrollVectorProvider)) {
            return null;
        }
        return new PagerGridSmoothScroller(this.mRecyclerView);
    }

    @Nullable
    public View findSnapView(LayoutManager layoutManager) {
        if (layoutManager instanceof PagerGridLayoutManager) {
            return ((PagerGridLayoutManager) layoutManager).findSnapView();
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0037, code lost:
        if (r3 < (-com.android.camera.fragment.vv.page.PagerConfig.getFlingThreshold())) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return r1.findPrePageFirstPos();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001e, code lost:
        if (r2 < (-com.android.camera.fragment.vv.page.PagerConfig.getFlingThreshold())) goto L_0x0020;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int findTargetSnapPosition(LayoutManager layoutManager, int i, int i2) {
        if (layoutManager != null && (layoutManager instanceof PagerGridLayoutManager)) {
            PagerGridLayoutManager pagerGridLayoutManager = (PagerGridLayoutManager) layoutManager;
            if (pagerGridLayoutManager.canScrollHorizontally()) {
                if (i <= PagerConfig.getFlingThreshold()) {
                }
            } else if (pagerGridLayoutManager.canScrollVertically()) {
                if (i2 <= PagerConfig.getFlingThreshold()) {
                }
            }
            return pagerGridLayoutManager.findNextPageFirstPos();
        }
        return -1;
    }

    public boolean onFling(int i, int i2) {
        LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        boolean z = false;
        if (layoutManager == null || this.mRecyclerView.getAdapter() == null) {
            return false;
        }
        int flingThreshold = PagerConfig.getFlingThreshold();
        if ((Math.abs(i2) > flingThreshold || Math.abs(i) > flingThreshold) && snapFromFling(layoutManager, i, i2)) {
            z = true;
        }
        return z;
    }

    public void setFlingThreshold(int i) {
        PagerConfig.setFlingThreshold(i);
    }
}
