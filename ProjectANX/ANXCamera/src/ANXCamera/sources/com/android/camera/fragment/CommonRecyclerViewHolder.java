package com.android.camera.fragment;

import android.util.SparseArray;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class CommonRecyclerViewHolder extends ViewHolder {
    private final SparseArray mViews = new SparseArray();

    public CommonRecyclerViewHolder(View view) {
        super(view);
    }

    public View getView(int i) {
        View view = (View) this.mViews.get(i);
        if (view != null) {
            return view;
        }
        View findViewById = this.itemView.findViewById(i);
        this.mViews.put(i, findViewById);
        return findViewById;
    }
}
