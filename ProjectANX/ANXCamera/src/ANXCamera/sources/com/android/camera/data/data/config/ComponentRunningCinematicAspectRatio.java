package com.android.camera.data.data.config;

import androidx.annotation.NonNull;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.runing.DataItemRunning;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentRunningCinematicAspectRatio extends ComponentData {
    public ComponentRunningCinematicAspectRatio(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
    }

    @NonNull
    public String getDefaultValue(int i) {
        return null;
    }

    public int getDisplayTitleString() {
        return R.string.moive_frame;
    }

    public List getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        if (i != 162) {
            if (i != 163) {
                if (i != 169) {
                    if (!(i == 171 || i == 173 || i == 180 || i == 189)) {
                        if (i != 204) {
                            if (!(i == 207 || i == 208)) {
                                switch (i) {
                                    case 212:
                                    case 213:
                                    case 214:
                                        break;
                                    default:
                                        return "is_cinematic_unsupported";
                                }
                            }
                        }
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("is_cinematic_");
            sb.append(i);
            return sb.toString();
        }
        return "is_cinematic_162";
    }

    public boolean isSwitchOn(int i) {
        if (i == 171 || i == 163 || i == 173 || i == 180 || i == 162 || i == 169 || i == 189 || i == 207 || i == 213 || i == 208 || i == 212 || i == 214) {
            return this.mParentDataItem.getBoolean(getKey(i), false);
        }
        return false;
    }

    public void reInit(int i) {
        ArrayList arrayList = new ArrayList();
        if (!(i == 162 || i == 163 || i == 169 || i == 171 || i == 173 || i == 180 || i == 189 || i == 207 || i == 208)) {
            switch (i) {
                case 212:
                case 213:
                case 214:
                    break;
            }
        }
        arrayList.add(new ComponentDataItem((int) R.drawable.ic_cinematic_aspect_ratio_off, (int) R.drawable.ic_cinematic_aspect_ratio_on, (int) R.string.accessibility_mimovie_on, "on"));
        this.mItems = Collections.unmodifiableList(arrayList);
    }

    public void setEnabled(int i, boolean z) {
        this.mParentDataItem.putBoolean(getKey(i), z);
    }
}
