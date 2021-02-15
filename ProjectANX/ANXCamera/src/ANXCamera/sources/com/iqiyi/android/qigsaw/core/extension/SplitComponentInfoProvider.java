package com.iqiyi.android.qigsaw.core.extension;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class SplitComponentInfoProvider {
    private final Set splitNames;

    SplitComponentInfoProvider(@NonNull Set set) {
        this.splitNames = set;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public Map getSplitActivitiesMap() {
        HashMap hashMap = new HashMap(0);
        for (String str : this.splitNames) {
            String[] splitActivities = ComponentInfoManager.getSplitActivities(str);
            if (splitActivities != null && splitActivities.length > 0) {
                ArrayList arrayList = new ArrayList();
                Collections.addAll(arrayList, splitActivities);
                hashMap.put(str, arrayList);
            }
        }
        return hashMap;
    }

    /* access modifiers changed from: 0000 */
    public String getSplitApplicationName(String str) {
        return ComponentInfoManager.getSplitApplication(str);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List getSplitReceivers() {
        ArrayList arrayList = new ArrayList();
        for (String splitReceivers : this.splitNames) {
            String[] splitReceivers2 = ComponentInfoManager.getSplitReceivers(splitReceivers);
            if (splitReceivers2 != null && splitReceivers2.length > 0) {
                Collections.addAll(arrayList, splitReceivers2);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List getSplitServices() {
        ArrayList arrayList = new ArrayList();
        for (String splitServices : this.splitNames) {
            String[] splitServices2 = ComponentInfoManager.getSplitServices(splitServices);
            if (splitServices2 != null && splitServices2.length > 0) {
                Collections.addAll(arrayList, splitServices2);
            }
        }
        return arrayList;
    }
}
