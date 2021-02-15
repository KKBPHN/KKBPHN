package com.android.camera.fragment.mode;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

public interface IMoreMode {
    public static final int MAX_ICON_COUNT_PER_LINES_NEW = 2;
    public static final int TYPE_EDIT = 2;
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_NORMAL_NEW = 3;
    public static final int TYPE_POPUP = 1;

    public @interface Type {
    }

    LayoutManager createLayoutManager(Context context);

    ModeItemDecoration createModeItemDecoration(Context context, IMoreMode iMoreMode);

    int getCountPerLine();

    RecyclerView getModeList(View view);

    @Type
    int getType();

    boolean modeShouldDownload(int i);
}
