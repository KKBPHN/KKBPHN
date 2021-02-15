package com.android.camera.dualvideo.render;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.android.camera.CameraSettings;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.dualvideo.ModuleUtil;
import com.android.camera.dualvideo.recorder.RecordType;
import com.android.camera.dualvideo.util.DualVideoConfigManager;
import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import com.android.camera.dualvideo.util.RenderSourceType;
import com.android.camera.dualvideo.util.SelectIndex;
import com.android.camera.dualvideo.util.UserSelectData;
import com.android.camera.effect.draw_mode.DrawAttribute;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.gallery3d.ui.ExtTexture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CameraItemManager {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String TAG = "CameraItemManager";
    private final RegionHelper mRegionHelper;
    private final Object mRenderLocker;
    private final ArrayList mRenderableList = new ArrayList();
    private HashMap mTextureMap = new HashMap();

    /* renamed from: com.android.camera.dualvideo.render.CameraItemManager$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$dualvideo$render$FaceType = new int[FaceType.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$dualvideo$render$LayoutType = new int[LayoutType.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$dualvideo$util$SelectIndex = new int[SelectIndex.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(27:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|19|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Can't wrap try/catch for region: R(28:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|19|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Can't wrap try/catch for region: R(29:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|16|17|18|19|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x003d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0047 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0064 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x006e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0083 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x008e */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            try {
                $SwitchMap$com$android$camera$dualvideo$util$SelectIndex[SelectIndex.INDEX_0.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$camera$dualvideo$util$SelectIndex[SelectIndex.INDEX_1.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$camera$dualvideo$util$SelectIndex[SelectIndex.INDEX_2.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $SwitchMap$com$android$camera$dualvideo$render$FaceType[FaceType.FACE_BACK.ordinal()] = 1;
            $SwitchMap$com$android$camera$dualvideo$render$FaceType[FaceType.FACE_FRONT.ordinal()] = 2;
            $SwitchMap$com$android$camera$dualvideo$render$FaceType[FaceType.FACE_REMOTE.ordinal()] = 3;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.DOWN.ordinal()] = 1;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.DOWN_FULL.ordinal()] = 2;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.UP.ordinal()] = 3;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.UP_FULL.ordinal()] = 4;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.MINI.ordinal()] = 5;
            try {
                $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.FULL.ordinal()] = 6;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public CameraItemManager(Object obj) {
        this.mRenderLocker = obj;
        this.mRegionHelper = new RegionHelper(Util.getDisplayRect(ModuleUtil.getUIStyle()));
    }

    static /* synthetic */ boolean O000000o(CameraItem cameraItem, UserSelectData userSelectData) {
        return userSelectData.getmSelectWindowLayoutType() == cameraItem.getSelectWindowLayoutType();
    }

    static /* synthetic */ boolean O000000o(CameraItemInterface cameraItemInterface, ConfigItem configItem) {
        return configItem.mLayoutType == cameraItemInterface.getSelectWindowLayoutType();
    }

    static /* synthetic */ boolean O000000o(CameraItemInterface cameraItemInterface, UserSelectData userSelectData) {
        return userSelectData.getSelectIndex() == cameraItemInterface.getSelectedIndex();
    }

    static /* synthetic */ boolean O000000o(LayoutType layoutType, UserSelectData userSelectData) {
        return userSelectData.getmSelectWindowLayoutType() == layoutType;
    }

    static /* synthetic */ boolean O000000o(ConfigItem configItem, CameraItemInterface cameraItemInterface) {
        return cameraItemInterface.getSelectWindowLayoutType() == configItem.mLayoutType;
    }

    static /* synthetic */ boolean O000000o(SelectIndex selectIndex, UserSelectData userSelectData) {
        return userSelectData.getSelectIndex() == selectIndex;
    }

    static /* synthetic */ boolean O00000Oo(CameraItemInterface cameraItemInterface) {
        return cameraItemInterface.getSelectedIndex() != SelectIndex.INDEX_0;
    }

    static /* synthetic */ void O00000o(CameraItemInterface cameraItemInterface, UserSelectData userSelectData) {
        if (userSelectData.getmSelectWindowLayoutType() == cameraItemInterface.getSelectWindowLayoutType()) {
            cameraItemInterface.setSelectTypeWithAnim(userSelectData.getSelectIndex(), true);
        }
    }

    static /* synthetic */ boolean O00000o(CameraItemInterface cameraItemInterface) {
        return cameraItemInterface.getRenderLayoutType() == LayoutType.MINI;
    }

    static /* synthetic */ boolean O00000o0(int i, CameraItemInterface cameraItemInterface) {
        if (cameraItemInterface.getSelectedIndex() == SelectIndex.INDEX_0 || DualVideoConfigManager.instance().getCameraId(cameraItemInterface.getRenderLayoutType()) != i) {
            return false;
        }
        cameraItemInterface.setSelectTypeWithAnim(SelectIndex.INDEX_0, true);
        return true;
    }

    static /* synthetic */ boolean O00000o0(CameraItemInterface cameraItemInterface, UserSelectData userSelectData) {
        return userSelectData.getmSelectWindowLayoutType() == cameraItemInterface.getSelectWindowLayoutType();
    }

    static /* synthetic */ void O00000oo(UserSelectData userSelectData) {
        StringBuilder sb = new StringBuilder();
        sb.append("userdata: ");
        sb.append(userSelectData.toString());
        Log.d(TAG, sb.toString());
    }

    static /* synthetic */ boolean O00000oo(CameraItemInterface cameraItemInterface) {
        return cameraItemInterface.getRenderLayoutType() == LayoutType.PATCH_0 && cameraItemInterface.isVisible();
    }

    static /* synthetic */ boolean O0000O0o(CameraItemInterface cameraItemInterface) {
        return cameraItemInterface.getSelectedIndex() == SelectIndex.INDEX_2;
    }

    static /* synthetic */ void O0000Oo(CameraItemInterface cameraItemInterface) {
        SelectIndex selectIndex;
        if (cameraItemInterface.getSelectedIndex() == SelectIndex.INDEX_1) {
            selectIndex = SelectIndex.INDEX_0;
        } else if (cameraItemInterface.getSelectedIndex() == SelectIndex.INDEX_2) {
            selectIndex = SelectIndex.INDEX_1;
        } else {
            return;
        }
        cameraItemInterface.setSelectTypeWithAnim(selectIndex, true);
    }

    static /* synthetic */ void O0000Oo0(CameraItemInterface cameraItemInterface) {
        if (cameraItemInterface.getSelectedIndex() != SelectIndex.INDEX_0) {
            cameraItemInterface.setSelectTypeWithAnim(SelectIndex.INDEX_1, true);
        }
    }

    static /* synthetic */ void O0000OoO(CameraItemInterface cameraItemInterface) {
        StringBuilder sb = new StringBuilder();
        sb.append("printRenderList: ");
        sb.append(cameraItemInterface.toString());
        Log.d(TAG, sb.toString());
    }

    static /* synthetic */ boolean O0000o0o(CameraItemInterface cameraItemInterface) {
        return cameraItemInterface.getRenderLayoutType() == LayoutType.MINI;
    }

    private CameraItem createCameraItem(LayoutType layoutType) {
        CameraItem cameraItem;
        if (C0122O00000o.instance().OOO000o()) {
            cameraItem = new CameraItem(layoutType, layoutType, DualVideoConfigManager.instance().getFaceType(layoutType));
            cameraItem.alphaInSelectWindowFlag(true);
        } else {
            cameraItem = new CameraItem(layoutType, RenderUtil.getRecordType(layoutType), DualVideoConfigManager.instance().getFaceType(layoutType));
        }
        initCameraItemAttri(cameraItem);
        initSelected(cameraItem);
        return cameraItem;
    }

    private FaceType getFaceTypeByCameraTag(RenderSourceType renderSourceType) {
        if (renderSourceType == RenderSourceType.REMOTE) {
            return FaceType.FACE_REMOTE;
        }
        return Camera2DataContainer.getInstance().isFrontCameraId(((Integer) CameraSettings.getDualVideoConfig().getIds().get(renderSourceType)).intValue()) ? FaceType.FACE_FRONT : FaceType.FACE_BACK;
    }

    private SelectIndex getIndexBySelelectType(LayoutType layoutType) {
        return (SelectIndex) CameraSettings.getDualVideoConfig().getSelectedData().stream().filter(new C0176O00000oO(layoutType)).findAny().map(O00000Oo.INSTANCE).orElse(SelectIndex.INDEX_0);
    }

    private LayoutType getRenderTypeBySelectIndex(SelectIndex selectIndex) {
        return (LayoutType) CameraSettings.getDualVideoConfig().getSelectedData().stream().filter(new C0235O00oOooO(selectIndex)).map(C0233O000ooO0.INSTANCE).findAny().orElse(LayoutType.UNDEFINED);
    }

    private void initCameraItemAttri(CameraItemInterface cameraItemInterface) {
        HashMap hashMap;
        RenderSourceType renderSourceType;
        ExtTexture extTexture;
        Rect renderAreaFor = this.mRegionHelper.getRenderAreaFor(cameraItemInterface.getRenderLayoutType());
        int i = AnonymousClass1.$SwitchMap$com$android$camera$dualvideo$render$FaceType[cameraItemInterface.getFaceType().ordinal()];
        String str = TAG;
        if (i != 1) {
            if (i == 2) {
                Log.d(str, "initCameraItemAttri: front");
                hashMap = this.mTextureMap;
                renderSourceType = RenderSourceType.SUB;
            } else if (i != 3) {
                extTexture = null;
            } else {
                Log.d(str, "initCameraItemAttri: remote");
                hashMap = this.mTextureMap;
                renderSourceType = RenderSourceType.REMOTE;
            }
            extTexture = (ExtTexture) hashMap.get(renderSourceType);
        } else {
            Log.d(str, "initCameraItemAttri: back");
            hashMap = this.mTextureMap;
            renderSourceType = RenderSourceType.MAIN;
            extTexture = (ExtTexture) hashMap.get(renderSourceType);
        }
        ExtTexture extTexture2 = extTexture;
        DrawExtTexAttribute drawExtTexAttribute = new DrawExtTexAttribute(extTexture2, RenderUtil.generatePreviewTransMatrix(cameraItemInterface.getFaceType(), cameraItemInterface.getRenderLayoutType(), extTexture2, renderAreaFor), renderAreaFor.left, renderAreaFor.top, renderAreaFor.width(), renderAreaFor.height());
        cameraItemInterface.setRenderAttri(drawExtTexAttribute, 101);
    }

    private void initRenderableList() {
        if (!this.mRenderableList.isEmpty()) {
            return;
        }
        if (C0122O00000o.instance().OOO000o()) {
            DualVideoConfigManager.instance().getConfigs().stream().forEach(new C0189O0000ooo(this));
        } else {
            CameraSettings.getDualVideoConfig().getSelectedData().forEach(new C0187O0000oo0(this));
        }
    }

    private void initSelectIndexFromSelectData() {
        this.mRenderableList.forEach(new O00oOoOo(this));
    }

    private void initSelected(CameraItem cameraItem) {
        DataRepository.dataItemRunning().getComponentRunningDualVideo().getSelectedData().stream().filter(new C0185O0000oOo(cameraItem)).findFirst().ifPresent(new O000OO0o(cameraItem));
        Log.d(TAG, "initSelected: ");
    }

    private boolean onTouched(int i, int i2) {
        boolean z;
        CameraItemInterface cameraItemInterface;
        ArrayList arrayList;
        Consumer consumer;
        StringBuilder sb;
        String str;
        Iterator it = this.mRenderableList.iterator();
        do {
            z = false;
            if (!it.hasNext()) {
                return false;
            }
            cameraItemInterface = (CameraItemInterface) it.next();
        } while (!this.mRegionHelper.getRenderAreaFor(cameraItemInterface.getRenderLayoutType()).contains(i, i2));
        if (cameraItemInterface.getFaceType() == FaceType.FACE_FRONT) {
            z = true;
        }
        float presentZoom = DualVideoConfigManager.instance().getPresentZoom(cameraItemInterface.getSelectWindowLayoutType());
        SelectIndex selectedIndex = cameraItemInterface.getSelectedIndex();
        SelectIndex selectIndex = SelectIndex.INDEX_1;
        String str2 = "front";
        String str3 = "X";
        String str4 = TAG;
        if (selectedIndex == selectIndex) {
            this.mRenderableList.stream().filter(O0000O0o.INSTANCE).forEach(C0184O0000oOO.INSTANCE);
            cameraItemInterface.setSelectTypeWithAnim(SelectIndex.INDEX_2, true);
            sb = new StringBuilder();
            if (!z) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(presentZoom);
                sb2.append(str3);
                str2 = sb2.toString();
            }
            sb.append(str2);
            str = ", index from 1 to 2";
        } else {
            if (cameraItemInterface.getSelectedIndex() == SelectIndex.INDEX_0) {
                if (this.mRenderableList.stream().anyMatch(new C0192O000O0oo(DualVideoConfigManager.instance().getCameraId(cameraItemInterface.getRenderLayoutType())))) {
                    arrayList = this.mRenderableList;
                    consumer = O0000o00.INSTANCE;
                } else {
                    arrayList = this.mRenderableList;
                    consumer = O000O0o0.INSTANCE;
                }
                arrayList.forEach(consumer);
                cameraItemInterface.setSelectTypeWithAnim(SelectIndex.INDEX_2, true);
                sb = new StringBuilder();
                if (!z) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(presentZoom);
                    sb3.append(str3);
                    str2 = sb3.toString();
                }
                sb.append(str2);
                str = ", index from 0 to 2";
            }
            saveSelectedStatus();
            return true;
        }
        sb.append(str);
        Log.u(str4, sb.toString());
        saveSelectedStatus();
        return true;
    }

    private void saveSelectedStatus() {
        this.mRenderableList.forEach(new C0183O0000oO0(DataRepository.dataItemRunning().getComponentRunningDualVideo().getSelectedData()));
    }

    /* access modifiers changed from: private */
    public void updateRenderableList() {
        synchronized (this.mRenderLocker) {
            this.mRenderableList.stream().filter(C0179O0000Ooo.INSTANCE).forEach(new O0000Oo(this));
        }
    }

    public /* synthetic */ void O000000o(float f, List list, CameraItemInterface cameraItemInterface) {
        DrawExtTexAttribute drawExtTexAttribute = (DrawExtTexAttribute) cameraItemInterface.getRenderAttri(101);
        ExtTexture extTexture = drawExtTexAttribute.mExtTexture;
        float[] fArr = drawExtTexAttribute.mTextureTransform;
        int i = drawExtTexAttribute.mX;
        Rect rect = this.mRegionHelper.mDrawRect;
        DrawExtTexAttribute drawExtTexAttribute2 = new DrawExtTexAttribute(extTexture, fArr, (int) (((float) (i - rect.left)) * f), (int) (((float) (drawExtTexAttribute.mY - rect.top)) * f), (int) (((float) drawExtTexAttribute.mWidth) * f), (int) (((float) drawExtTexAttribute.mHeight) * f));
        list.add(drawExtTexAttribute2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00a6, code lost:
        if (r6 == r1) goto L_0x00a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00ad, code lost:
        r5 = r5.mTextureMap;
        r6 = com.android.camera.dualvideo.util.RenderSourceType.REMOTE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0025, code lost:
        if (r6 != 3) goto L_0x00b2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void O00000o0(CameraItemInterface cameraItemInterface) {
        HashMap hashMap;
        RenderSourceType renderSourceType;
        HashMap hashMap2;
        RenderSourceType renderSourceType2;
        DrawExtTexAttribute drawExtTexAttribute = (DrawExtTexAttribute) cameraItemInterface.getRenderAttri(101);
        if (CameraSettings.getDualVideoConfig().ismDrawSelectWindow()) {
            int i = AnonymousClass1.$SwitchMap$com$android$camera$dualvideo$render$FaceType[cameraItemInterface.getFaceType().ordinal()];
            if (i != 1) {
                if (i != 2) {
                }
                hashMap = this.mTextureMap;
                renderSourceType = RenderSourceType.SUB;
                drawExtTexAttribute.mExtTexture = (ExtTexture) hashMap.get(renderSourceType);
            }
        } else {
            int cameraId = DualVideoConfigManager.instance().getCameraId(cameraItemInterface.getSelectWindowLayoutType());
            ConcurrentHashMap localCameraId = CameraSettings.getDualVideoConfig().getLocalCameraId();
            if (cameraId == 1000) {
                hashMap2 = this.mTextureMap;
                renderSourceType2 = RenderSourceType.REMOTE;
            } else if (localCameraId.size() == 1) {
                hashMap2 = this.mTextureMap;
                renderSourceType2 = RenderSourceType.MAIN;
            } else {
                int intValue = ((Integer) localCameraId.get(RenderSourceType.MAIN)).intValue();
                int intValue2 = ((Integer) localCameraId.get(RenderSourceType.SUB)).intValue();
                StringBuilder sb = new StringBuilder();
                sb.append("changeTexture: ");
                sb.append(cameraId);
                sb.append(" main: ");
                sb.append(intValue);
                sb.append(" sub ");
                sb.append(intValue2);
                Log.d(TAG, sb.toString());
                if (cameraId != intValue) {
                }
            }
            drawExtTexAttribute.mExtTexture = (ExtTexture) hashMap2.get(renderSourceType2);
            return;
        }
        hashMap = this.mTextureMap;
        renderSourceType = RenderSourceType.MAIN;
        drawExtTexAttribute.mExtTexture = (ExtTexture) hashMap.get(renderSourceType);
    }

    public /* synthetic */ void O00000oO(CameraItemInterface cameraItemInterface) {
        cameraItemInterface.setSelectTypeWithAnim(getIndexBySelelectType(cameraItemInterface.getSelectWindowLayoutType()), false);
    }

    public /* synthetic */ void O00000oO(UserSelectData userSelectData) {
        this.mRenderableList.add(createCameraItem(userSelectData.getmSelectWindowLayoutType()));
    }

    public /* synthetic */ void O00000oo(ConfigItem configItem) {
        this.mRenderableList.add(createCameraItem(configItem.mLayoutType));
    }

    public /* synthetic */ void O0000O0o(ConfigItem configItem) {
        if (this.mRenderableList.stream().noneMatch(new C0191O000O0oO(configItem))) {
            this.mRenderableList.add(createCameraItem(configItem.mLayoutType));
        }
    }

    public /* synthetic */ void O0000Ooo(CameraItemInterface cameraItemInterface) {
        if (cameraItemInterface.getSelectedIndex() == SelectIndex.INDEX_1 || cameraItemInterface.getSelectedIndex() == SelectIndex.INDEX_2) {
            cameraItemInterface.setRenderLayoutTypeWithAnim(cameraItemInterface.getSelectWindowLayoutType(), this.mRegionHelper, true);
        }
    }

    public /* synthetic */ void O0000o(CameraItemInterface cameraItemInterface) {
        cameraItemInterface.updateRenderAttri(this.mRegionHelper);
    }

    public /* synthetic */ void O0000o0(CameraItemInterface cameraItemInterface) {
        cameraItemInterface.alphaInSelectWindowFlag(false);
        int i = AnonymousClass1.$SwitchMap$com$android$camera$dualvideo$util$SelectIndex[cameraItemInterface.getSelectedIndex().ordinal()];
        if (i == 2 || i == 3) {
            cameraItemInterface.alphaInSelectedFrame(false);
            cameraItemInterface.setRenderLayoutTypeWithAnim(getRenderTypeBySelectIndex(cameraItemInterface.getSelectedIndex()), this.mRegionHelper, true);
            return;
        }
        cameraItemInterface.setVisibilityWithAnim(false, true);
    }

    public /* synthetic */ void O0000o00(CameraItemInterface cameraItemInterface) {
        cameraItemInterface.alphaInSelectWindowFlag(true);
        int i = AnonymousClass1.$SwitchMap$com$android$camera$dualvideo$util$SelectIndex[cameraItemInterface.getSelectedIndex().ordinal()];
        if (i == 1) {
            cameraItemInterface.setRenderLayoutTypeWithAnim(cameraItemInterface.getRenderLayoutType(), this.mRegionHelper, false);
        } else if (i == 2 || i == 3) {
            cameraItemInterface.alphaInSelectedFrame(true);
            cameraItemInterface.setRenderLayoutTypeWithAnim(cameraItemInterface.getSelectWindowLayoutType(), this.mRegionHelper, true);
        }
        if (!cameraItemInterface.isVisible()) {
            cameraItemInterface.setVisibilityWithAnim(true, true);
        }
    }

    public void changeTexture() {
        Log.d(TAG, "changeTexture: ");
        this.mRenderableList.stream().filter(O000O0o.INSTANCE).forEach(new C0177O00000oo(this));
    }

    @SuppressLint({"SwitchIntDef"})
    public void expandBottom() {
        LayoutType layoutType;
        Log.u(TAG, "expandBottom: ");
        if (!isAnimating()) {
            Iterator it = this.mRenderableList.iterator();
            while (it.hasNext()) {
                CameraItemInterface cameraItemInterface = (CameraItemInterface) it.next();
                if (cameraItemInterface.isVisible()) {
                    int i = AnonymousClass1.$SwitchMap$com$android$camera$dualvideo$render$LayoutType[cameraItemInterface.getRenderLayoutType().ordinal()];
                    if (i == 1 || i == 2) {
                        layoutType = LayoutType.FULL;
                    } else if (i == 3 || i == 4) {
                        layoutType = LayoutType.MINI;
                    }
                    cameraItemInterface.setRenderLayoutTypeWithAnim(layoutType, this.mRegionHelper, true);
                }
            }
        }
    }

    @SuppressLint({"SwitchIntDef"})
    public void expandOrShrinkTop() {
        Log.u(TAG, "expandOrShrinkTop: ");
        if (!isAnimating() && !this.mRenderableList.isEmpty()) {
            boolean z = CameraSettings.getDualVideoConfig().getRecordType() == RecordType.MERGED;
            Iterator it = this.mRenderableList.iterator();
            while (it.hasNext()) {
                CameraItemInterface cameraItemInterface = (CameraItemInterface) it.next();
                if (cameraItemInterface.isVisible()) {
                    LayoutType lastRenderLayoutType = cameraItemInterface.getLastRenderLayoutType();
                    switch (AnonymousClass1.$SwitchMap$com$android$camera$dualvideo$render$LayoutType[cameraItemInterface.getRenderLayoutType().ordinal()]) {
                        case 1:
                        case 2:
                            lastRenderLayoutType = LayoutType.MINI;
                            break;
                        case 3:
                        case 4:
                            lastRenderLayoutType = LayoutType.FULL;
                            break;
                        case 5:
                            if (lastRenderLayoutType == LayoutType.MINI || lastRenderLayoutType == LayoutType.FULL || lastRenderLayoutType.isSelectWindowType()) {
                                if (!z) {
                                    lastRenderLayoutType = LayoutType.UP_FULL;
                                    break;
                                } else {
                                    lastRenderLayoutType = LayoutType.UP;
                                    break;
                                }
                            }
                        case 6:
                            if (lastRenderLayoutType == LayoutType.MINI || lastRenderLayoutType == LayoutType.FULL || lastRenderLayoutType.isSelectWindowType()) {
                                if (!z) {
                                    lastRenderLayoutType = LayoutType.DOWN_FULL;
                                    break;
                                } else {
                                    lastRenderLayoutType = LayoutType.DOWN;
                                    break;
                                }
                            }
                        default:
                            continue;
                    }
                    cameraItemInterface.setRenderLayoutTypeWithAnim(lastRenderLayoutType, this.mRegionHelper, true);
                }
            }
        }
    }

    public DrawAttribute getFullTypeRecordAttri(RenderSourceType renderSourceType) {
        ExtTexture extTexture = (ExtTexture) this.mTextureMap.get(renderSourceType);
        DrawExtTexAttribute drawExtTexAttribute = new DrawExtTexAttribute(extTexture, RenderUtil.generatePreviewTransMatrix(getFaceTypeByCameraTag(renderSourceType), LayoutType.FULL, extTexture, new Rect(0, 0, RenderUtil.OUTPUT_SIZE.getWidth(), RenderUtil.OUTPUT_SIZE.getHeight())), 0, 0, RenderUtil.OUTPUT_SIZE.getWidth(), RenderUtil.OUTPUT_SIZE.getHeight());
        return drawExtTexAttribute;
    }

    public ArrayList getRenderableList() {
        if (this.mRenderableList.isEmpty()) {
            initRenderableList();
        }
        return this.mRenderableList;
    }

    public List getRenderableListForRecord() {
        int width = Util.getDisplayRect(1).width();
        int width2 = RenderUtil.OUTPUT_SIZE.getWidth();
        float f = width != width2 ? ((float) width2) / ((float) width) : 1.0f;
        ArrayList arrayList = new ArrayList();
        getRenderableList().stream().filter(C0194O000OOoo.INSTANCE).sorted(C0232O000ooO.INSTANCE).forEachOrdered(new C0188O0000ooO(this, f, arrayList));
        return arrayList;
    }

    public ArrayList getVisibleRenderList() {
        return (ArrayList) getRenderableList().stream().filter(C0194O000OOoo.INSTANCE).collect(Collectors.toCollection(C0195O000Oo00.INSTANCE));
    }

    public boolean hasMiniCameraItem() {
        return this.mRenderableList.stream().anyMatch(C0182O0000oO.INSTANCE);
    }

    public boolean is6PatchWindow() {
        return this.mRenderableList.stream().anyMatch(O0000o0.INSTANCE);
    }

    public boolean isAnimating() {
        if (!this.mRenderableList.isEmpty()) {
            return this.mRenderableList.stream().anyMatch(O000000o.INSTANCE);
        }
        return false;
    }

    public void printRenderList() {
        Log.d(TAG, "printRenderList: start");
        if (!this.mRenderableList.isEmpty()) {
            this.mRenderableList.forEach(O00000o.INSTANCE);
        }
    }

    public void reinitRenderableList() {
        this.mRenderableList.clear();
    }

    public void selectItem(MotionEvent motionEvent) {
        StringBuilder sb = new StringBuilder();
        sb.append("selectItem: ");
        sb.append(motionEvent.getAction());
        Log.d(TAG, sb.toString());
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            Iterator it = this.mRenderableList.iterator();
            while (it.hasNext()) {
                CameraItemInterface cameraItemInterface = (CameraItemInterface) it.next();
                if (this.mRegionHelper.getRenderAreaFor(cameraItemInterface.getRenderLayoutType()).contains(x, y)) {
                    cameraItemInterface.onKeyDown();
                }
            }
        } else if (actionMasked == 1 || actionMasked == 3) {
            onTouched((int) motionEvent.getX(), (int) motionEvent.getY());
            this.mRenderableList.stream().filter(C0230O000oo0O.INSTANCE).forEachOrdered(O000OO.INSTANCE);
        }
    }

    public void setTexture(RenderSourceType renderSourceType, ExtTexture extTexture) {
        HashMap hashMap = this.mTextureMap;
        if (extTexture == null) {
            hashMap.remove(renderSourceType);
        } else {
            hashMap.put(renderSourceType, extTexture);
        }
    }

    public void switchRecordToSelectWindow() {
        if (!isAnimating()) {
            Log.d(TAG, "switchPreviewTo6Patch: ");
            this.mRegionHelper.setDrawRect(Util.getDisplayRect(ModuleUtil.getUIStyle()));
            this.mRenderableList.forEach(new C0190O000O0Oo(this));
            this.mRenderableList.forEach(new C0180O0000o0O(this));
        }
    }

    public void switchSelectToRecordWindow() {
        if (!isAnimating()) {
            printRenderList();
            this.mRegionHelper.setDrawRect(Util.getDisplayRect(ModuleUtil.getUIStyle()));
            CameraSettings.getDualVideoConfig().getSelectedData().forEach(C0178O0000OoO.INSTANCE);
            initSelectIndexFromSelectData();
            this.mRenderableList.forEach(new C0236O00oOooo(this));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0056  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x005c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @SuppressLint({"SwitchIntDef"})
    public boolean switchTopBottom() {
        SelectIndex selectIndex;
        LayoutType layoutType;
        Log.d(TAG, "switchTopBottom ");
        if (isAnimating()) {
            return false;
        }
        Iterator it = this.mRenderableList.iterator();
        while (it.hasNext()) {
            CameraItemInterface cameraItemInterface = (CameraItemInterface) it.next();
            if (cameraItemInterface.isVisible()) {
                switch (AnonymousClass1.$SwitchMap$com$android$camera$dualvideo$render$LayoutType[cameraItemInterface.getRenderLayoutType().ordinal()]) {
                    case 1:
                        layoutType = LayoutType.UP;
                        break;
                    case 2:
                        layoutType = LayoutType.UP_FULL;
                        break;
                    case 3:
                        layoutType = LayoutType.DOWN;
                        break;
                    case 4:
                        layoutType = LayoutType.DOWN_FULL;
                        break;
                    case 5:
                        layoutType = LayoutType.FULL;
                        break;
                    case 6:
                        layoutType = LayoutType.MINI;
                        break;
                    default:
                        if (cameraItemInterface.getSelectedIndex() == SelectIndex.INDEX_1) {
                            if (cameraItemInterface.getSelectedIndex() != SelectIndex.INDEX_2) {
                                break;
                            } else {
                                selectIndex = SelectIndex.INDEX_1;
                            }
                        } else {
                            selectIndex = SelectIndex.INDEX_2;
                        }
                        cameraItemInterface.setSelectTypeWithAnim(selectIndex, false);
                        continue;
                }
                cameraItemInterface.setRenderLayoutTypeWithAnim(layoutType, this.mRegionHelper, false);
                if (cameraItemInterface.getSelectedIndex() == SelectIndex.INDEX_1) {
                }
                cameraItemInterface.setSelectTypeWithAnim(selectIndex, false);
                continue;
            }
        }
        return true;
    }

    public void updateCameraItemList() {
        if (this.mRenderableList.size() != DualVideoConfigManager.instance().getConfigs().size()) {
            if (this.mRenderableList.size() > DualVideoConfigManager.instance().getConfigs().size()) {
                this.mRenderableList.removeIf(O000OO00.INSTANCE);
                updateRenderableList();
            }
            if (this.mRenderableList.size() < DualVideoConfigManager.instance().getConfigs().size()) {
                updateRenderableList();
                DualVideoConfigManager.instance().getConfigs().stream().forEachOrdered(new O000O00o(this));
            }
            ArrayList selectedData = CameraSettings.getDualVideoConfig().getSelectedData();
            Iterator it = this.mRenderableList.iterator();
            while (it.hasNext()) {
                CameraItemInterface cameraItemInterface = (CameraItemInterface) it.next();
                if (selectedData.stream().anyMatch(new O000O0OO(cameraItemInterface))) {
                    selectedData.stream().forEach(new O0000Oo0(cameraItemInterface));
                } else {
                    cameraItemInterface.setSelectTypeWithAnim(SelectIndex.INDEX_0, true);
                }
            }
        }
    }

    public boolean updateMiniWindowLocation(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        return false;
                    }
                } else if (!this.mRegionHelper.mIsHovering) {
                    return false;
                } else {
                    int x = (int) (motionEvent.getX() - this.mRegionHelper.mStartX);
                    float y = motionEvent.getY();
                    RegionHelper regionHelper = this.mRegionHelper;
                    int i = (int) (y - regionHelper.mStartY);
                    regionHelper.setStartPosition(motionEvent.getX(), motionEvent.getY());
                    this.mRegionHelper.updateMarginOffset(x, i);
                    return true;
                }
            }
            RegionHelper regionHelper2 = this.mRegionHelper;
            if (!regionHelper2.mIsHovering) {
                return false;
            }
            regionHelper2.mIsHovering = false;
            regionHelper2.moveToEdge();
            return true;
        }
        float x2 = motionEvent.getX();
        float y2 = motionEvent.getY();
        Rect renderAreaFor = this.mRegionHelper.getRenderAreaFor(LayoutType.MINI);
        int i2 = renderAreaFor.left;
        boolean z = x2 > ((float) i2) && x2 < ((float) (i2 + renderAreaFor.width()));
        int i3 = renderAreaFor.top;
        boolean z2 = y2 > ((float) i3) && y2 < ((float) (i3 + renderAreaFor.height()));
        if (!z || !z2) {
            return false;
        }
        RegionHelper regionHelper3 = this.mRegionHelper;
        regionHelper3.mIsHovering = true;
        regionHelper3.setListener(new C0186O0000oo(this));
        this.mRegionHelper.setStartPosition(x2, y2);
        return true;
    }
}
