package com.android.camera.fragment.beauty;

import android.content.Context;
import android.util.AttributeSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.Recycler;
import androidx.recyclerview.widget.RecyclerView.State;
import com.android.camera.log.Log;

public class LinearLayoutManagerWrapper extends LinearLayoutManager {
    private static final String TAG = "LinearLayoutManagerWrapper";
    private String mRecycleViewFrom;

    public LinearLayoutManagerWrapper(Context context, int i, boolean z, String str) {
        super(context, i, z);
        this.mRecycleViewFrom = str;
    }

    public LinearLayoutManagerWrapper(Context context, AttributeSet attributeSet, int i, int i2, String str) {
        super(context, attributeSet, i, i2);
        this.mRecycleViewFrom = str;
    }

    public LinearLayoutManagerWrapper(Context context, String str) {
        super(context);
        this.mRecycleViewFrom = str;
    }

    public void onLayoutChildren(Recycler recycler, State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append("onLayoutChildren IndexOutOfBoundsException from bussiness: ");
            sb.append(this.mRecycleViewFrom);
            Log.e(TAG, sb.toString());
        }
    }

    public int scrollHorizontallyBy(int i, Recycler recycler, State state) {
        try {
            this = this;
            this = super.scrollHorizontallyBy(i, recycler, state);
            r0 = this;
            return this;
        } catch (IndexOutOfBoundsException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append("scrollHorizontallyBy IndexOutOfBoundsException from bussiness: ");
            sb.append(r0.mRecycleViewFrom);
            Log.e(TAG, sb.toString());
            return 0;
        }
    }
}
