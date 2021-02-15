package com.android.camera.data.data.global;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.text.TextUtils;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ComponentModuleList extends ComponentData {
    private boolean isChanged;
    private List mFavoriteItems = new CopyOnWriteArrayList();
    private List mInHideItems = new CopyOnWriteArrayList();
    private List mInMoreItems = new CopyOnWriteArrayList();
    private int mIntentType;
    private int mLastCameraId;

    public ComponentModuleList(DataItemGlobal dataItemGlobal) {
        super(dataItemGlobal);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.data.data.ComponentDataItem>, for r3v0, types: [java.util.List, java.util.List<com.android.camera.data.data.ComponentDataItem>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private String getItemStringName(String str, List<ComponentDataItem> list) {
        for (ComponentDataItem componentDataItem : list) {
            if (str.equals(componentDataItem.mValue)) {
                return componentDataItem.mDisplayNameRes > 0 ? CameraAppImpl.getAndroidContext().getResources().getString(componentDataItem.mDisplayNameRes) : componentDataItem.mDisplayNameStr;
            }
        }
        return "";
    }

    private int getMoreItemIndex(List list) {
        if (this.mIntentType != 0 || list.isEmpty()) {
            return -1;
        }
        int i = 0;
        while (list.size() > i && Integer.parseInt(((ComponentDataItem) list.get(i)).mValue) != 254) {
            i++;
        }
        if (i == list.size()) {
            return -1;
        }
        return i;
    }

    public static final int getTransferredMode(int i) {
        if (i == 165) {
            return 163;
        }
        if (i == 176) {
            return 166;
        }
        if (i == 185) {
            return 210;
        }
        if (i == 179) {
            return 209;
        }
        if (i != 180) {
            return i;
        }
        return 167;
    }

    private void initHideItems() {
        C0122O00000o instance = C0122O00000o.instance();
        this.mInHideItems.clear();
        if ((instance.OOOo0oO() || instance.OOoO0OO()) && DataRepository.dataItemGlobal().getDisplayMode() == 1) {
            this.mInHideItems.add(ModeDataFactory.getInstance().createModeData(214));
        }
    }

    private void initImageItems(List list) {
        list.add(ModeDataFactory.getInstance().createModeData(163));
    }

    private synchronized List initItems() {
        ArrayList arrayList;
        List list;
        List subList;
        if (this.mIntentType != -1) {
            arrayList = new ArrayList();
            int i = this.mIntentType;
            if (i == 0) {
                initNormalItems(arrayList);
                initHideItems();
            } else if (i == 1) {
                initImageItems(arrayList);
            } else if (i == 2) {
                initVideoItems(arrayList);
            } else if (i == 3) {
                initQRItems(arrayList);
            }
            this.mFavoriteItems.clear();
            this.mInMoreItems.clear();
            int moreItemIndex = getMoreItemIndex(arrayList);
            if (moreItemIndex == -1) {
                this.mFavoriteItems.addAll(arrayList);
            } else {
                if (CameraSettings.isPopupMoreStyle()) {
                    list = this.mFavoriteItems;
                    subList = arrayList.subList(0, moreItemIndex);
                } else {
                    if (CameraSettings.getMoreModeStyle() == 0) {
                        list = this.mFavoriteItems;
                        subList = arrayList.subList(0, moreItemIndex + 1);
                    }
                    this.mInMoreItems.addAll(arrayList.subList(moreItemIndex + 1, arrayList.size()));
                }
                list.addAll(subList);
                this.mInMoreItems.addAll(arrayList.subList(moreItemIndex + 1, arrayList.size()));
            }
        } else {
            throw new RuntimeException("parse intent first!");
        }
        return arrayList;
    }

    private void initNormalItems(List list) {
        C0122O00000o instance = C0122O00000o.instance();
        if (instance.OOOOoo()) {
            if (C0122O00000o.instance().OOO0Oo0()) {
                list.add(ModeDataFactory.getInstance().createModeData(177));
            }
            if (C0122O00000o.instance().OOO0ooo()) {
                list.add(ModeDataFactory.getInstance().createModeData(184));
            }
            return;
        }
        HashMap hashMap = new HashMap();
        if (DataRepository.dataItemGlobal().getDisplayMode() == 1) {
            hashMap.put(Integer.valueOf(167), ModeDataFactory.getInstance().createModeData(167));
        }
        if (instance.OOOo00o()) {
            hashMap.put(Integer.valueOf(171), ModeDataFactory.getInstance().createModeData(171));
        }
        hashMap.put(Integer.valueOf(163), ModeDataFactory.getInstance().createModeData(163));
        if (C0122O00000o.instance().OO000oO() > 0 && DataRepository.dataItemGlobal().getDisplayMode() == 1) {
            hashMap.put(Integer.valueOf(175), ModeDataFactory.getInstance().createModeData(175));
        }
        hashMap.put(Integer.valueOf(162), ModeDataFactory.getInstance().createModeData(162));
        hashMap.put(Integer.valueOf(254), ModeDataFactory.getInstance().createModeData(254));
        if ((instance.OOOo0oO() || instance.OOoO0OO()) && DataRepository.dataItemGlobal().getDisplayMode() == 1) {
            hashMap.put(Integer.valueOf(173), ModeDataFactory.getInstance().createModeData(173));
        }
        if (instance.OOO0ooO() && !instance.OOO0o0O()) {
            hashMap.put(Integer.valueOf(183), ModeDataFactory.getInstance().createModeData(183));
        }
        if (instance.OOO0o0O() && !instance.OOO0ooO()) {
            hashMap.put(Integer.valueOf(174), ModeDataFactory.getInstance().createModeData(174));
        }
        if (!instance.OOO0o0O() && !instance.OOO0ooO()) {
            hashMap.put(Integer.valueOf(161), ModeDataFactory.getInstance().createModeData(161));
        }
        if (C0122O00000o.instance().OOO0Oo0()) {
            hashMap.put(Integer.valueOf(177), ModeDataFactory.getInstance().createModeData(177));
        }
        if (C0122O00000o.instance().OOO0ooo()) {
            hashMap.put(Integer.valueOf(184), ModeDataFactory.getInstance().createModeData(184));
        }
        if (instance.OOOo0o() && DataRepository.dataItemGlobal().getDisplayMode() == 1) {
            hashMap.put(Integer.valueOf(166), ModeDataFactory.getInstance().createModeData(166));
        }
        if ((instance.OO0ooo0() || instance.OO0ooo()) && DataRepository.dataItemGlobal().getDisplayMode() == 1) {
            hashMap.put(Integer.valueOf(186), ModeDataFactory.getInstance().createModeData(186));
        }
        if (instance.OOOOo0o() && DataRepository.dataItemGlobal().getDisplayMode() == 1) {
            hashMap.put(Integer.valueOf(188), ModeDataFactory.getInstance().createModeData(188));
        }
        if (instance.OOO00o() && DataRepository.dataItemGlobal().getDisplayMode() == 1) {
            hashMap.put(Integer.valueOf(209), ModeDataFactory.getInstance().createModeData(209));
        }
        if (instance.OOoOoo0() && DataRepository.dataItemGlobal().getDisplayMode() == 1) {
            hashMap.put(Integer.valueOf(172), ModeDataFactory.getInstance().createModeData(172));
        }
        hashMap.put(Integer.valueOf(169), ModeDataFactory.getInstance().createModeData(169));
        if (instance.OO0oo00()) {
            hashMap.put(Integer.valueOf(210), ModeDataFactory.getInstance().createModeData(210));
        }
        if (instance.OO0oooo()) {
            hashMap.put(Integer.valueOf(204), ModeDataFactory.getInstance().createModeData(204));
        }
        if (instance.OOOoooo()) {
            hashMap.put(Integer.valueOf(205), ModeDataFactory.getInstance().createModeData(205));
        }
        if (instance.oO0OO0()) {
            hashMap.put(Integer.valueOf(187), ModeDataFactory.getInstance().createModeData(187));
        }
        if (instance.OOO0oOO() || instance.OOOO0o0() || instance.OOOOoO0() || instance.OOooOo() || instance.OO0oooO()) {
            hashMap.put(Integer.valueOf(211), ModeDataFactory.getInstance().createModeData(211));
        }
        int[] sortModes = DataRepository.dataItemGlobal().getSortModes();
        int[] iArr = new int[hashMap.size()];
        int i = 0;
        for (int i2 = 0; i2 < sortModes.length; i2++) {
            ComponentDataItem componentDataItem = (ComponentDataItem) hashMap.get(Integer.valueOf(sortModes[i2]));
            if (componentDataItem != null) {
                list.add(componentDataItem);
                hashMap.remove(Integer.valueOf(sortModes[i2]));
                iArr[i] = sortModes[i2];
                i++;
            }
        }
        if (!hashMap.isEmpty()) {
            for (Integer num : hashMap.keySet()) {
                list.add((ComponentDataItem) hashMap.get(num));
                iArr[i] = num.intValue();
                i++;
            }
        }
        list.add(ModeDataFactory.getInstance().createModeData(255));
        DataRepository.dataItemGlobal().setSortModes(iArr);
    }

    private void initQRItems(List list) {
        list.add(ModeDataFactory.getInstance().createModeData(163));
    }

    private void initVideoItems(List list) {
        list.add(ModeDataFactory.getInstance().createModeData(162));
    }

    public void clear() {
        this.mItems = null;
    }

    public String geItemStringName(int i, boolean z) {
        String valueOf = String.valueOf(getTransferredMode(i));
        String itemStringName = getItemStringName(valueOf, z ? getMoreItems() : getCommonItems());
        return TextUtils.isEmpty(itemStringName) ? getItemStringName(valueOf, getHideItems()) : itemStringName;
    }

    public List getCommonItems() {
        if (this.mIntentType != 0) {
            return getItems();
        }
        if (this.mItems == null) {
            this.mItems = initItems();
        }
        return this.mFavoriteItems;
    }

    public String getDefaultValue(int i) {
        return null;
    }

    public int getDisplayNameRes(int i) {
        return ((ComponentDataItem) getItems().get(i)).mDisplayNameRes;
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List getHideItems() {
        return this.mIntentType != 0 ? getItems() : this.mInHideItems;
    }

    public List getItems() {
        int currentCameraId = DataRepository.dataItemGlobal().getCurrentCameraId();
        if (C0122O00000o.instance().OOOOOOo() && this.mLastCameraId != currentCameraId) {
            this.mLastCameraId = currentCameraId;
            this.mItems = initItems();
        }
        if (this.mItems == null) {
            this.mItems = initItems();
        }
        return this.mItems;
    }

    public String getKey(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(DataItemGlobal.DATA_COMMON_CURRENT_MODE);
        sb.append(this.mIntentType);
        return sb.toString();
    }

    public int getMode(int i) {
        return Integer.parseInt(((ComponentDataItem) getItems().get(i)).mValue);
    }

    public List getMoreItems() {
        if (this.mIntentType != 0) {
            return new ArrayList();
        }
        if (this.mItems == null) {
            this.mItems = initItems();
        }
        return this.mInMoreItems;
    }

    public boolean isCommonMode(int i) {
        int transferredMode = getTransferredMode(i);
        if (this.mFavoriteItems.isEmpty()) {
            this.mItems = initItems();
        }
        for (ComponentDataItem componentDataItem : this.mFavoriteItems) {
            if (componentDataItem != null && transferredMode == Integer.parseInt(componentDataItem.mValue)) {
                return true;
            }
        }
        return false;
    }

    public boolean needShowLiveRedDot() {
        return !CameraSettings.isLiveModuleClicked();
    }

    public void reInit(boolean z) {
        if (z || DataRepository.dataItemGlobal().hasModesChanged()) {
            this.mItems = initItems();
            this.isChanged = true;
        }
    }

    public void runChangeResetCb(Runnable runnable) {
        if (this.isChanged && runnable != null) {
            this.isChanged = false;
            runnable.run();
        }
    }

    public void setIntentType(int i) {
        this.mIntentType = i;
        if (this.mItems != null) {
            this.mItems.clear();
        }
        this.mItems = initItems();
    }
}
