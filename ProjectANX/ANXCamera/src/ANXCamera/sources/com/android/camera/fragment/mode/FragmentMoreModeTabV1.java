package com.android.camera.fragment.mode;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.data.DataRepository;

public class FragmentMoreModeTabV1 implements IMoreMode {
    private static final String TAG = "MoreModeTabV1";

    public LayoutManager createLayoutManager(Context context) {
        return new GridLayoutManager(context, getCountPerLine(), 1, false);
    }

    public ModeItemDecoration createModeItemDecoration(Context context, IMoreMode iMoreMode) {
        return new ModeItemDecoration(context, iMoreMode, getType());
    }

    public int getCountPerLine() {
        return Display.getMoreModeTabCol(DataRepository.dataItemRunning().getUiStyle(), false);
    }

    public RecyclerView getModeList(View view) {
        return (RecyclerView) view.findViewById(R.id.modes_content);
    }

    public int getType() {
        return 0;
    }

    public boolean modeShouldDownload(int i) {
        return false;
    }
}
