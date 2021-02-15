package com.android.camera.fragment.aiwatermark.holder;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.android.camera.R;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.fragment.CommonRecyclerViewHolder;

public class WatermarkHolder extends CommonRecyclerViewHolder {
    private static final String TAG = "WatermarkHolder";
    private ImageView mImageView = null;
    private int mIndex = -1;
    private ImageView mSelectedIndicator = null;

    public WatermarkHolder(@NonNull View view) {
        super(view);
        this.mImageView = (ImageView) view.findViewById(R.id.mimoji_item_image);
        this.mSelectedIndicator = (ImageView) view.findViewById(R.id.mimoji_item_selected_indicator);
        FolmeUtils.touchItemScale(view);
    }

    public void bindHolder(int i, WatermarkItem watermarkItem) {
        this.mIndex = i;
        this.mImageView.setImageResource(watermarkItem.getResRvItem());
    }

    public View getImageView() {
        return this.mImageView;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public View getSelectedIndicator() {
        return this.mSelectedIndicator;
    }

    public void updateSelectItem(int i) {
        this.mSelectedIndicator.setVisibility(i);
    }
}
