package com.android.camera.data.data.config;

import android.util.SparseBooleanArray;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.effect.FilterInfo;
import com.android.camera.log.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ComponentConfigFilter extends ComponentData {
    private SparseBooleanArray mIsClosed;

    public ComponentConfigFilter(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
    }

    public String getComponentValue(int i) {
        return isClosed(i) ? String.valueOf(FilterInfo.FILTER_ID_NONE) : i == 183 ? DataRepository.dataItemLive().getMiLiveFilterId() : super.getComponentValue(i);
    }

    public String getDefaultValue(int i) {
        int i2 = (i == 162 || i == 169 || i == 180) ? 0 : FilterInfo.FILTER_ID_NONE;
        return String.valueOf(i2);
    }

    public int getDisplayTitleString() {
        return R.string.pref_camera_coloreffect_title;
    }

    public List getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        return "pref_camera_shader_coloreffect_key";
    }

    public boolean isClosed(int i) {
        if (this.mIsClosed == null) {
            this.mIsClosed = new SparseBooleanArray();
        }
        if (i == 165) {
            i = 163;
        }
        return this.mIsClosed.get(i);
    }

    public void mapToItems(ArrayList arrayList, int i) {
        ComponentDataItem componentDataItem;
        this.mItems = new ArrayList(arrayList.size());
        boolean supportColorRentention = DataRepository.dataItemRunning().getComponentRunningShine().supportColorRentention();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            FilterInfo filterInfo = (FilterInfo) it.next();
            if (162 != i && 180 != i && 169 != i) {
                componentDataItem = new ComponentDataItem(filterInfo.getIconResId(), filterInfo.getIconResId(), filterInfo.getNameResId(), String.valueOf(filterInfo.getId()));
            } else if (filterInfo.getTagUniqueFilterId() != 200 || supportColorRentention) {
                componentDataItem = new ComponentDataItem(filterInfo.getIconResId(), filterInfo.getIconResId(), filterInfo.getNameResId(), String.valueOf(filterInfo.getTagUniqueFilterId()));
            }
            this.mItems.add(componentDataItem);
        }
    }

    public void setClosed(boolean z, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("setClosed: mode = ");
        sb.append(i);
        sb.append(", close = ");
        sb.append(z);
        Log.d("ComponentConfigFilter", sb.toString());
        if (this.mIsClosed == null) {
            this.mIsClosed = new SparseBooleanArray();
        }
        if (i == 165) {
            i = 163;
        }
        this.mIsClosed.put(i, z);
    }

    public void setComponentValue(int i, String str) {
        if (i == 183) {
            DataRepository.dataItemLive().setMiLiveFilterId(str);
        } else {
            super.setComponentValue(i, str);
        }
    }
}
