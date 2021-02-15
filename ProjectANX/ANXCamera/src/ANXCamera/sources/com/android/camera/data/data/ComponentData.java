package com.android.camera.data.data;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.android.camera.Util;
import com.android.camera.log.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public abstract class ComponentData {
    public boolean mIsDisplayStringFromResourceId = false;
    public boolean mIsKeepValueWhenDisabled = false;
    protected volatile List mItems = new ArrayList();
    protected DataItemBase mParentDataItem;

    public ComponentData(DataItemBase dataItemBase) {
        this.mParentDataItem = dataItemBase;
    }

    /* access modifiers changed from: protected */
    public boolean checkValueValid(int i, String str) {
        return true;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.data.data.ComponentDataItem>, for r1v0, types: [java.util.List, java.util.List<com.android.camera.data.data.ComponentDataItem>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void copyList(List<ComponentDataItem> list, List list2) {
        if (list2 == null) {
            list2 = new ArrayList();
        }
        list2.clear();
        for (ComponentDataItem add : list) {
            list2.add(add);
        }
    }

    public boolean disableUpdate() {
        return false;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.data.data.ComponentDataItem>, for r5v0, types: [java.util.List, java.util.List<com.android.camera.data.data.ComponentDataItem>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public List filterSupprotedItems(List<ComponentDataItem> list, List list2) {
        ArrayList arrayList = new ArrayList();
        if (list == null || list.size() == 0) {
            return null;
        }
        for (ComponentDataItem componentDataItem : list) {
            componentDataItem.mIsDisabled = true;
            Iterator it = list2.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String str = (String) it.next();
                if (componentDataItem.mValue == str) {
                    arrayList.add(str);
                    componentDataItem.mIsDisabled = false;
                    break;
                }
            }
        }
        return arrayList;
    }

    public List filterSupprotedItems(List list, String... strArr) {
        ArrayList arrayList = new ArrayList();
        for (String add : strArr) {
            arrayList.add(add);
        }
        return filterSupprotedItems(list, (List) arrayList);
    }

    public int findIndexOfValue(String str) {
        List items = getItems();
        for (int i = 0; i < items.size(); i++) {
            if (str.equals(((ComponentDataItem) items.get(i)).mValue)) {
                return i;
            }
        }
        return -1;
    }

    public String getComponentValue(int i) {
        String defaultValue = getDefaultValue(i);
        String string = this.mParentDataItem.getString(getKey(i), defaultValue);
        if (string == null || string.equals(defaultValue) || checkValueValid(i, string)) {
            return string;
        }
        String simpleName = getClass().getSimpleName();
        StringBuilder sb = new StringBuilder();
        sb.append("reset invalid value ");
        sb.append(string);
        Log.e(simpleName, sb.toString());
        resetComponentValue(i);
        return this.mParentDataItem.getString(getKey(i), defaultValue);
    }

    @StringRes
    public int getContentDescriptionString() {
        return 0;
    }

    @NonNull
    public abstract String getDefaultValue(int i);

    @StringRes
    public int getDefaultValueDisplayString(int i) {
        return 0;
    }

    @StringRes
    public int getDisableReasonString() {
        return 0;
    }

    public String getDisplayComponentValue(int i) {
        return getComponentValue(i);
    }

    @StringRes
    public abstract int getDisplayTitleString();

    public abstract List getItems();

    public abstract String getKey(int i);

    public String getPersistValue(int i) {
        return getComponentValue(i);
    }

    @StringRes
    public int getValueDisplayString(int i) {
        String componentValue = getComponentValue(i);
        List<ComponentDataItem> items = getItems();
        if (items.isEmpty()) {
            Log.e(getClass().getSimpleName(), "List is empty!");
            return -1;
        }
        for (ComponentDataItem componentDataItem : items) {
            if (componentDataItem.mValue.equals(componentValue)) {
                return componentDataItem.mDisplayNameRes;
            }
        }
        String format = String.format(Locale.ENGLISH, "mode %1$d, invalid value %2$s for %3$s, items = %4$s", new Object[]{Integer.valueOf(i), componentValue, getKey(i), Arrays.toString(items.toArray())});
        Log.e(getClass().getSimpleName(), format);
        if (!Util.isDebugOsBuild()) {
            return -1;
        }
        throw new IllegalArgumentException(format);
    }

    public String getValueDisplayStringNotFromResource(int i) {
        String componentValue = getComponentValue(i);
        List<ComponentDataItem> items = getItems();
        for (ComponentDataItem componentDataItem : items) {
            if (componentDataItem.mValue.equals(componentValue)) {
                return componentDataItem.mDisplayNameStr;
            }
        }
        Log.e(getClass().getSimpleName(), String.format(Locale.ENGLISH, "mode %1$d, invalid value %2$s for %3$s, items = %4$s", new Object[]{Integer.valueOf(i), componentValue, getKey(i), Arrays.toString(items.toArray())}));
        return null;
    }

    @DrawableRes
    public int getValueSelectedDrawable(int i) {
        String componentValue = getComponentValue(i);
        List<ComponentDataItem> items = getItems();
        if (items == null || items.size() <= 0) {
            return -1;
        }
        for (ComponentDataItem componentDataItem : items) {
            if (componentDataItem.mValue.equals(componentValue)) {
                return componentDataItem.mIconSelectedRes;
            }
        }
        String format = String.format(Locale.ENGLISH, "mode %1$d, invalid value %2$s for %3$s, items = %4$s", new Object[]{Integer.valueOf(i), componentValue, getKey(i), Arrays.toString(items.toArray())});
        Log.e(getClass().getSimpleName(), format);
        if (!Util.isDebugOsBuild()) {
            return -1;
        }
        throw new IllegalArgumentException(format);
    }

    @DrawableRes
    public int getValueSelectedShadowDrawable(int i) {
        return -1;
    }

    public boolean isEmpty() {
        return this.mItems == null || this.mItems.isEmpty();
    }

    public boolean isModified(int i) {
        return !getComponentValue(i).equals(getDefaultValue(i));
    }

    public boolean isSwitchOn(int i) {
        return false;
    }

    public void reset(int i) {
        setComponentValue(i, getDefaultValue(i));
    }

    /* access modifiers changed from: protected */
    public void resetComponentValue(int i) {
    }

    public void setComponentValue(int i, String str) {
        if (this.mParentDataItem.isTransient()) {
            this.mParentDataItem.putString(getKey(i), str);
        } else {
            this.mParentDataItem.editor().putString(getKey(i), str).apply();
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.data.data.ComponentDataItem>, for r2v0, types: [java.util.List, java.util.List<com.android.camera.data.data.ComponentDataItem>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int supprotedItemsSize(List<ComponentDataItem> list) {
        int i;
        int i2 = 0;
        if (!(list == null || list.size() == 0)) {
            for (ComponentDataItem componentDataItem : list) {
                if (!componentDataItem.mIsDisabled) {
                    i2++;
                }
            }
        }
        return i;
    }

    public void toSwitch(int i, boolean z) {
    }
}
