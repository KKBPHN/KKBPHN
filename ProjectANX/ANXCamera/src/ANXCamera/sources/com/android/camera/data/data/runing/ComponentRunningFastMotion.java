package com.android.camera.data.data.runing;

import androidx.annotation.NonNull;
import com.android.camera.data.data.ComponentData;
import java.util.ArrayList;
import java.util.List;

public class ComponentRunningFastMotion extends ComponentData {
    private String mCurrentType;
    private boolean mIsClosed;
    private ArrayList mList;

    public ComponentRunningFastMotion(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
        this.mList = new ArrayList();
        this.mIsClosed = true;
        this.mCurrentType = String.valueOf(1);
    }

    public ComponentRunningFastMotion(DataItemRunning dataItemRunning, ArrayList arrayList) {
        this(dataItemRunning);
        this.mList = arrayList;
    }

    public String getCurrentType() {
        return this.mCurrentType;
    }

    @NonNull
    public String getDefaultValue(int i) {
        return "off";
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List getItems() {
        return this.mList;
    }

    public String getKey(int i) {
        return "pref_fast_motion_key";
    }

    public boolean isClosed() {
        return this.mIsClosed;
    }

    public void reInit() {
        this.mCurrentType = String.valueOf(1);
    }

    public void setClosed(boolean z) {
        this.mIsClosed = z;
    }

    public void setCurrentType(String str) {
        this.mCurrentType = str;
    }
}
