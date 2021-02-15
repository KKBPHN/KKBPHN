package com.android.camera.videoplayer.meta;

import android.view.View;

public class CurrentItemMetaData implements MetaData {
    public final View currentItemView;
    public final int positionOfCurrentItem;

    public CurrentItemMetaData(int i, View view) {
        this.positionOfCurrentItem = i;
        this.currentItemView = view;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CurrentItemMetaData{positionOfCurrentItem=");
        sb.append(this.positionOfCurrentItem);
        sb.append(", currentItemView=");
        sb.append(this.currentItemView);
        sb.append('}');
        return sb.toString();
    }
}
