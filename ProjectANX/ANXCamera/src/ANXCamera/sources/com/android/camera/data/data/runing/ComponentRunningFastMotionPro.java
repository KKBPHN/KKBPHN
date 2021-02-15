package com.android.camera.data.data.runing;

import androidx.annotation.NonNull;
import com.android.camera.data.data.ComponentData;
import java.util.ArrayList;
import java.util.List;

public class ComponentRunningFastMotionPro extends ComponentData {
    private boolean mIsClosed;
    private ArrayList mList;

    public ComponentRunningFastMotionPro(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
        this.mList = new ArrayList();
        this.mIsClosed = true;
    }

    public ComponentRunningFastMotionPro(DataItemRunning dataItemRunning, ArrayList arrayList) {
        this(dataItemRunning);
        this.mList = arrayList;
    }

    @NonNull
    public String getDefaultValue(int i) {
        return "0";
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List getItems() {
        return this.mList;
    }

    public String getKey(int i) {
        return "pref_fast_motion_pro_key";
    }

    public boolean isClosed() {
        return this.mIsClosed;
    }

    public void setClosed(boolean z) {
        this.mIsClosed = z;
    }
}
