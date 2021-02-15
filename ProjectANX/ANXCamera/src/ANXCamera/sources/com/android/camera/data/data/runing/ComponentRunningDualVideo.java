package com.android.camera.data.data.runing;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import androidx.annotation.NonNull;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.DataItemBase;
import com.android.camera.dualvideo.recorder.RecordType;
import com.android.camera.dualvideo.render.FaceType;
import com.android.camera.dualvideo.render.LayoutType;
import com.android.camera.dualvideo.util.DualVideoConfigManager;
import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import com.android.camera.dualvideo.util.RenderSourceType;
import com.android.camera.dualvideo.util.SelectIndex;
import com.android.camera.dualvideo.util.UserSelectData;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComponentRunningDualVideo extends ComponentData {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String TAG = "ComponentRunningDualVideo";
    private boolean mDrawSelectWindow;
    private RecordType mRecordType;
    private ArrayList mUserSelectDataList = new ArrayList();

    /* renamed from: com.android.camera.data.data.runing.ComponentRunningDualVideo$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$dualvideo$recorder$RecordType = new int[RecordType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$android$camera$dualvideo$recorder$RecordType[RecordType.STANDALONE.ordinal()] = 1;
            $SwitchMap$com$android$camera$dualvideo$recorder$RecordType[RecordType.MERGED.ordinal()] = 2;
        }
    }

    public ComponentRunningDualVideo(DataItemBase dataItemBase) {
        super(dataItemBase);
        initUserSelectionDataIfNeed();
        setRecordType(RecordType.MERGED);
    }

    static /* synthetic */ void O000000o(ConcurrentHashMap concurrentHashMap, Integer num) {
        concurrentHashMap.clear();
        concurrentHashMap.put(RenderSourceType.MAIN, num);
    }

    static /* synthetic */ boolean O000000o(ConfigItem configItem) {
        return configItem.getFaceType() == FaceType.FACE_REMOTE;
    }

    static /* synthetic */ boolean O000000o(ConfigItem configItem, UserSelectData userSelectData) {
        return userSelectData.getmSelectWindowLayoutType() == configItem.mLayoutType;
    }

    static /* synthetic */ boolean O000000o(UserSelectData userSelectData, ConfigItem configItem) {
        return configItem.mLayoutType == userSelectData.getmSelectWindowLayoutType();
    }

    static /* synthetic */ boolean O00000Oo(UserSelectData userSelectData) {
        return DualVideoConfigManager.instance().getFaceType(userSelectData.getmSelectWindowLayoutType()) == FaceType.FACE_BACK;
    }

    static /* synthetic */ void O00000o(UserSelectData userSelectData) {
        LayoutType layoutType;
        if (userSelectData.getSelectIndex() == SelectIndex.INDEX_1) {
            layoutType = LayoutType.UP;
        } else if (userSelectData.getSelectIndex() == SelectIndex.INDEX_2) {
            layoutType = LayoutType.DOWN;
        } else {
            return;
        }
        userSelectData.setmRecordLayoutType(layoutType);
    }

    static /* synthetic */ boolean O00000o(ConfigItem configItem) {
        return configItem.getFaceType() == FaceType.FACE_REMOTE;
    }

    static /* synthetic */ boolean O00000o(Integer num) {
        return num.intValue() == 1000;
    }

    static /* synthetic */ void O00000o0(UserSelectData userSelectData) {
        LayoutType layoutType;
        if (userSelectData.getSelectIndex() == SelectIndex.INDEX_1) {
            layoutType = LayoutType.UP_FULL;
        } else if (userSelectData.getSelectIndex() == SelectIndex.INDEX_2) {
            layoutType = LayoutType.DOWN_FULL;
        } else {
            return;
        }
        userSelectData.setmRecordLayoutType(layoutType);
    }

    static /* synthetic */ boolean O00000oO(ConfigItem configItem) {
        return configItem.getFaceType() == FaceType.FACE_REMOTE;
    }

    static /* synthetic */ boolean O00000oO(Integer num) {
        return num.intValue() != 1000;
    }

    static /* synthetic */ Integer[] O0000o0O(int i) {
        return new Integer[i];
    }

    private LayoutType getLayoutForSelect() {
        ArrayList arrayList = (ArrayList) DualVideoConfigManager.instance().getConfigs().stream().collect(Collectors.toCollection(C0135O0000oO0.INSTANCE));
        arrayList.removeIf(new C0133O0000o0O(this));
        LayoutType layoutType = (LayoutType) arrayList.stream().filter(C0131O0000OoO.INSTANCE).findFirst().map(O00000o0.INSTANCE).orElse(null);
        return layoutType != null ? layoutType : ((ConfigItem) arrayList.get(0)).mLayoutType;
    }

    private void initUserSelectionDataIfNeed() {
        ArrayList arrayList;
        UserSelectData userSelectData;
        if (this.mUserSelectDataList.isEmpty()) {
            if (C0122O00000o.instance().OOO000o()) {
                setmDrawSelectWindow(true);
                this.mUserSelectDataList.add(new UserSelectData(SelectIndex.INDEX_1, LayoutType.PATCH_0, LayoutType.UP));
                arrayList = this.mUserSelectDataList;
                userSelectData = new UserSelectData(SelectIndex.INDEX_2, LayoutType.PATCH_3, LayoutType.DOWN);
            } else {
                setmDrawSelectWindow(false);
                this.mUserSelectDataList.add(new UserSelectData(SelectIndex.INDEX_1, LayoutType.PATCH_0, LayoutType.UP));
                arrayList = this.mUserSelectDataList;
                userSelectData = new UserSelectData(SelectIndex.INDEX_2, LayoutType.PATCH_1, LayoutType.DOWN);
            }
            arrayList.add(userSelectData);
        }
    }

    public /* synthetic */ boolean O00000o0(ConfigItem configItem) {
        return this.mUserSelectDataList.stream().anyMatch(new O0000Oo(configItem));
    }

    @NonNull
    public String getDefaultValue(int i) {
        return this.mRecordType.getName();
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public ConcurrentHashMap getIds() {
        return ismDrawSelectWindow() ? Camera2DataContainer.getInstance().getDefaultDualVideoCameraIds() : DualVideoConfigManager.sortId((Integer[]) this.mUserSelectDataList.stream().map(O000000o.INSTANCE).toArray(O00000Oo.INSTANCE));
    }

    public List getItems() {
        ArrayList arrayList = new ArrayList();
        ComponentDataItem componentDataItem = new ComponentDataItem(-1, -1, -1, R.string.dual_video_record_merged, R.string.accessibility_dual_video_merged, RecordType.MERGED.getName());
        arrayList.add(componentDataItem);
        ComponentDataItem componentDataItem2 = new ComponentDataItem(-1, -1, -1, R.string.dual_video_record_standalone, R.string.accessibility_dual_video_standalone, RecordType.STANDALONE.getName());
        arrayList.add(componentDataItem2);
        return Collections.unmodifiableList(arrayList);
    }

    public String getKey(int i) {
        return null;
    }

    public ConcurrentHashMap getLocalCameraId() {
        ConcurrentHashMap ids = getIds();
        Collection values = ids.values();
        if (values.stream().anyMatch(O0000o0.INSTANCE)) {
            values.stream().filter(O0000O0o.INSTANCE).findFirst().ifPresent(new O0000o(ids));
        }
        return ids;
    }

    public RecordType getRecordType() {
        return this.mRecordType;
    }

    public ArrayList getSelectedData() {
        return this.mUserSelectDataList;
    }

    public boolean ismDrawSelectWindow() {
        return this.mDrawSelectWindow;
    }

    public void reInit() {
        setRecordType(RecordType.MERGED);
        this.mUserSelectDataList.clear();
        initUserSelectionDataIfNeed();
    }

    public void refreshSelectData() {
        ArrayList configs = DualVideoConfigManager.instance().getConfigs();
        Iterator it = this.mUserSelectDataList.iterator();
        while (it.hasNext()) {
            UserSelectData userSelectData = (UserSelectData) it.next();
            if (configs.stream().noneMatch(new O0000o00(userSelectData))) {
                userSelectData.setSelectWindowLayoutType(getLayoutForSelect());
            }
        }
        if (configs.stream().anyMatch(C0134O0000o0o.INSTANCE)) {
            this.mUserSelectDataList.stream().filter(C0132O0000Ooo.INSTANCE).findFirst().ifPresent(new O00000o(configs));
        }
    }

    public void setRecordType(RecordType recordType) {
        Consumer consumer;
        Stream stream;
        StringBuilder sb = new StringBuilder();
        sb.append("setRecordType: ");
        sb.append(recordType);
        Log.d(TAG, sb.toString());
        if (this.mRecordType != recordType) {
            this.mRecordType = recordType;
            int i = AnonymousClass1.$SwitchMap$com$android$camera$dualvideo$recorder$RecordType[this.mRecordType.ordinal()];
            if (i != 1) {
                if (i == 2) {
                    stream = this.mUserSelectDataList.stream();
                    consumer = C0129O00000oO.INSTANCE;
                }
            }
            stream = this.mUserSelectDataList.stream();
            consumer = O0000OOo.INSTANCE;
            stream.forEach(consumer);
        }
    }

    public void setmDrawSelectWindow(boolean z) {
        this.mDrawSelectWindow = z;
    }
}
