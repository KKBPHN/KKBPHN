package com.android.camera.features.mimoji2.widget.baseview;

import android.content.Context;
import android.util.AttributeSet;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView.Recycler;
import androidx.recyclerview.widget.RecyclerView.State;
import com.android.camera.log.Log;

public class BaseNoScrollGridLayoutManager extends GridLayoutManager {
    public BaseNoScrollGridLayoutManager(Context context, int i) {
        super(context, i);
    }

    public BaseNoScrollGridLayoutManager(Context context, int i, int i2, boolean z) {
        super(context, i, i2, z);
    }

    public BaseNoScrollGridLayoutManager(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public boolean canScrollHorizontally() {
        return false;
    }

    public boolean canScrollVertically() {
        return false;
    }

    public void onLayoutChildren(Recycler recycler, State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException unused) {
            Log.i("BaseNoScrollGridLayoutManager", "IndexOutOfBoundsException ");
        }
    }
}
