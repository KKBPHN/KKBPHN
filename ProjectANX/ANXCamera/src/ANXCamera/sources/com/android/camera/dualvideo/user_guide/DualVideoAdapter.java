package com.android.camera.dualvideo.user_guide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.R;
import com.android.camera.fragment.CommonRecyclerViewHolder;

public class DualVideoAdapter extends Adapter {
    public int getItemCount() {
        return 3;
    }

    public int getItemViewType(int i) {
        return i;
    }

    public void onBindViewHolder(@NonNull CommonRecyclerViewHolder commonRecyclerViewHolder, int i) {
    }

    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater layoutInflater;
        int i2;
        if (i == 0) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
            i2 = R.layout.dual_video_user_guide_main;
        } else if (i == 1) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
            i2 = R.layout.dual_video_user_guide_merge;
        } else if (i != 2) {
            view = null;
            return new CommonRecyclerViewHolder(view);
        } else {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
            i2 = R.layout.dual_video_user_guide_dual;
        }
        view = layoutInflater.inflate(i2, viewGroup, false);
        return new CommonRecyclerViewHolder(view);
    }
}
