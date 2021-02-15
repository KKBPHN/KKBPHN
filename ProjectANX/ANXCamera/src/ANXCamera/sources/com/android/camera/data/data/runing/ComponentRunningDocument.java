package com.android.camera.data.data.runing;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import androidx.annotation.NonNull;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentRunningDocument extends ComponentData {
    public static final String DOCUMENT_BLACK_WHITE = "bin";
    public static final String DOCUMENT_ORIGIN = "raw";
    public static final String DOCUMENT_STRENGTHEN = "color";
    private DataItemRunning mDataItemRunning;
    private boolean mSupported;

    public ComponentRunningDocument(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
        this.mDataItemRunning = dataItemRunning;
    }

    @NonNull
    public String getDefaultValue(int i) {
        return "raw";
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List getItems() {
        ArrayList arrayList = new ArrayList();
        ComponentDataItem componentDataItem = new ComponentDataItem(-1, -1, -1, R.string.document_origin, R.string.accessibility_document_origin_button, "raw");
        arrayList.add(componentDataItem);
        ComponentDataItem componentDataItem2 = new ComponentDataItem(-1, -1, -1, R.string.document_blackwhite, R.string.accessibility_document_blackwhite_button, DOCUMENT_BLACK_WHITE);
        arrayList.add(componentDataItem2);
        ComponentDataItem componentDataItem3 = new ComponentDataItem(-1, -1, -1, R.string.document_strengthen, R.string.accessibility_document_strengthen_button, DOCUMENT_STRENGTHEN);
        arrayList.add(componentDataItem3);
        return Collections.unmodifiableList(arrayList);
    }

    public String getKey(int i) {
        return "pref_document_mode_value_key";
    }

    public boolean isSwitchOn(int i) {
        if (!this.mSupported) {
            return false;
        }
        return this.mDataItemRunning.isSwitchOn("pref_document_mode_key");
    }

    public void reInit(int i, int i2, boolean z) {
        this.mSupported = false;
        if (!z) {
            return;
        }
        if ((C0122O00000o.instance().OO0ooo0() || C0122O00000o.instance().OO0ooo()) && i == 186 && i2 == 0) {
            this.mSupported = true;
        }
    }

    public void setEnabled(boolean z) {
        String str = "pref_document_mode_key";
        DataItemRunning dataItemRunning = this.mDataItemRunning;
        if (z) {
            dataItemRunning.switchOn(str);
        } else {
            dataItemRunning.switchOff(str);
        }
    }
}
