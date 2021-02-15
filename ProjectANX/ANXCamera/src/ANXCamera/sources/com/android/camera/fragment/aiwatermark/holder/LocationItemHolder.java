package com.android.camera.fragment.aiwatermark.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.android.camera.R;
import com.android.camera.fragment.CommonRecyclerViewHolder;

public class LocationItemHolder extends CommonRecyclerViewHolder {
    private int mIndex = -1;
    private ImageView mSelectedIndicator = null;
    private TextView mTextView = null;

    public LocationItemHolder(@NonNull View view) {
        super(view);
        this.mTextView = (TextView) view.findViewById(R.id.watermark_location_text);
        this.mSelectedIndicator = (ImageView) view.findViewById(R.id.watermark_location_image);
    }

    public void bindHolder(int i, String str) {
        this.mIndex = i;
        this.mTextView.setText(str);
    }

    public int getIndex() {
        return this.mIndex;
    }

    public View getSelectedIndicator() {
        return this.mSelectedIndicator;
    }

    public TextView getTextView() {
        return this.mTextView;
    }

    public void updateSelectItem(int i) {
        this.mSelectedIndicator.setVisibility(i);
    }
}
