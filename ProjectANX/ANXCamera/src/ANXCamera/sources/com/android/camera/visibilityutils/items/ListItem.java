package com.android.camera.visibilityutils.items;

import android.view.View;

public interface ListItem {
    void deactivate(View view, int i);

    int getVisibilityPercents(View view);

    void setActive(View view, int i);
}
