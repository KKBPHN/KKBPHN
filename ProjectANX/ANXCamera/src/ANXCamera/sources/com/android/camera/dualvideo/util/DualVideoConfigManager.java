package com.android.camera.dualvideo.util;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import com.android.camera.data.data.config.ComponentManuallyDualLens;
import com.android.camera.dualvideo.render.FaceType;
import com.android.camera.dualvideo.render.LayoutType;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class DualVideoConfigManager {
    public static final String FRONT_NAME = "front";
    public static final int REMOTE_CAMERA_ID = 1000;
    public static final String REMOTE_NAME = "remote";
    private static final String TAG = "CameraIDManager";
    private static DualVideoConfigManager mManager;
    private ArrayList mConfigs = new ArrayList();

    public class ConfigItem {
        public int mCameraId;
        public String mDescription;
        public LayoutType mLayoutType;
        public float mPresentZoom;
        public float mRelativeZoom;

        ConfigItem(LayoutType layoutType, int i, float f, float f2, String str) {
            this.mPresentZoom = f;
            this.mCameraId = i;
            this.mRelativeZoom = f2;
            this.mDescription = str;
            this.mLayoutType = layoutType;
        }

        public FaceType getFaceType() {
            return this.mCameraId == 1000 ? FaceType.FACE_REMOTE : Camera2DataContainer.getInstance().isFrontCameraId(this.mCameraId) ? FaceType.FACE_FRONT : FaceType.FACE_BACK;
        }
    }

    private DualVideoConfigManager() {
        initConfig();
    }

    static /* synthetic */ boolean O000000o(int i, LayoutType layoutType) {
        return layoutType.getIndex() == LayoutType.PATCH_0.getIndex() + i;
    }

    static /* synthetic */ boolean O000000o(int i, ConfigItem configItem) {
        return configItem.mCameraId == i;
    }

    static /* synthetic */ void O00000Oo(ConcurrentHashMap concurrentHashMap, Integer num) {
        concurrentHashMap.put(num.intValue() == 1000 ? RenderSourceType.REMOTE : RenderSourceType.MAIN, num);
    }

    static /* synthetic */ boolean O00000Oo(int i, ConfigItem configItem) {
        return configItem.mCameraId == i;
    }

    static /* synthetic */ boolean O00000Oo(LayoutType layoutType, ConfigItem configItem) {
        return configItem.mLayoutType == layoutType;
    }

    static /* synthetic */ boolean O00000o(LayoutType layoutType, ConfigItem configItem) {
        return configItem.mLayoutType == layoutType;
    }

    static /* synthetic */ boolean O00000o0(int i, ConfigItem configItem) {
        return configItem.mCameraId == i;
    }

    static /* synthetic */ boolean O00000o0(LayoutType layoutType, ConfigItem configItem) {
        return configItem.mLayoutType == layoutType;
    }

    static /* synthetic */ boolean O00000oO(LayoutType layoutType, ConfigItem configItem) {
        return configItem.mLayoutType == layoutType;
    }

    static /* synthetic */ boolean O0000O0o(Integer num) {
        return num.intValue() == 1000;
    }

    static /* synthetic */ boolean O0000OoO(ConfigItem configItem) {
        return configItem.mLayoutType == LayoutType.PATCH_0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x005d A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private String getDescription(List list) {
        char c;
        String str = (String) list.get(1);
        int hashCode = str.hashCode();
        String str2 = "front";
        if (hashCode != 3743) {
            if (hashCode != 3746) {
                if (hashCode != 3556266) {
                    if (hashCode != 3649235) {
                        if (hashCode == 97705513 && str.equals(str2)) {
                            c = 4;
                            if (c != 0 || c == 1 || c == 2 || c == 3) {
                                StringBuilder sb = new StringBuilder();
                                sb.append((String) list.get(0));
                                sb.append("X");
                                return sb.toString();
                            } else if (c == 4) {
                                return str2;
                            } else {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("config error: ");
                                sb2.append((String) list.get(1));
                                throw new RuntimeException(sb2.toString());
                            }
                        }
                    } else if (str.equals(ComponentManuallyDualLens.LENS_WIDE)) {
                        c = 1;
                        if (c != 0) {
                        }
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append((String) list.get(0));
                        sb3.append("X");
                        return sb3.toString();
                    }
                } else if (str.equals(ComponentManuallyDualLens.LENS_TELE)) {
                    c = 2;
                    if (c != 0) {
                    }
                    StringBuilder sb32 = new StringBuilder();
                    sb32.append((String) list.get(0));
                    sb32.append("X");
                    return sb32.toString();
                }
            } else if (str.equals("uw")) {
                c = 0;
                if (c != 0) {
                }
                StringBuilder sb322 = new StringBuilder();
                sb322.append((String) list.get(0));
                sb322.append("X");
                return sb322.toString();
            }
        } else if (str.equals("ut")) {
            c = 3;
            if (c != 0) {
            }
            StringBuilder sb3222 = new StringBuilder();
            sb3222.append((String) list.get(0));
            sb3222.append("X");
            return sb3222.toString();
        }
        c = 65535;
        if (c != 0) {
        }
        StringBuilder sb32222 = new StringBuilder();
        sb32222.append((String) list.get(0));
        sb32222.append("X");
        return sb32222.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0056  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0099  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getIDByConfig(String str) {
        char c;
        int hashCode = str.hashCode();
        if (hashCode != 3743) {
            if (hashCode != 3746) {
                if (hashCode != 3556266) {
                    if (hashCode != 3649235) {
                        if (hashCode == 97705513 && str.equals("front")) {
                            c = 4;
                            if (c != 0) {
                                return Camera2DataContainer.getInstance().getUltraWideCameraId();
                            }
                            if (c == 1) {
                                return Camera2DataContainer.getInstance().getMainBackCameraId();
                            }
                            if (c == 2) {
                                return Camera2DataContainer.getInstance().getAuxCameraId();
                            }
                            if (c == 3) {
                                return Camera2DataContainer.getInstance().getUltraTeleCameraId();
                            }
                            if (c == 4) {
                                return Camera2DataContainer.getInstance().getFrontCameraId();
                            }
                            StringBuilder sb = new StringBuilder();
                            sb.append("config error: ");
                            sb.append(str);
                            throw new RuntimeException(sb.toString());
                        }
                    } else if (str.equals(ComponentManuallyDualLens.LENS_WIDE)) {
                        c = 1;
                        if (c != 0) {
                        }
                    }
                } else if (str.equals(ComponentManuallyDualLens.LENS_TELE)) {
                    c = 2;
                    if (c != 0) {
                    }
                }
            } else if (str.equals("uw")) {
                c = 0;
                if (c != 0) {
                }
            }
        } else if (str.equals("ut")) {
            c = 3;
            if (c != 0) {
            }
        }
        c = 65535;
        if (c != 0) {
        }
    }

    private LayoutType getLayoutType(int i) {
        return (LayoutType) Stream.of(LayoutType.values()).filter(new O0000o(i)).findFirst().get();
    }

    private float getZoomById(int i) {
        return ((Float) this.mConfigs.stream().filter(new C0238O00000oo(i)).findFirst().map(O00000o0.INSTANCE).orElse(Float.valueOf(1.0f))).floatValue();
    }

    private void initConfig() {
        if (this.mConfigs.isEmpty()) {
            String O0ooOOO = C0122O00000o.instance().O0ooOOO();
            if (O0ooOOO == null) {
                ArrayList arrayList = this.mConfigs;
                ConfigItem configItem = new ConfigItem(getLayoutType(0), Camera2DataContainer.getInstance().getMainBackCameraId(), 1.0f, 1.0f, "1X");
                arrayList.add(configItem);
                ArrayList arrayList2 = this.mConfigs;
                ConfigItem configItem2 = new ConfigItem(getLayoutType(1), Camera2DataContainer.getInstance().getFrontCameraId(), 1.0f, 1.0f, "front");
                arrayList2.add(configItem2);
            } else {
                int i = 0;
                for (String lowerCase : Arrays.asList(O0ooOOO.toLowerCase(Locale.ENGLISH).split(";"))) {
                    List asList = Arrays.asList(lowerCase.toLowerCase(Locale.ENGLISH).split(":"));
                    ArrayList arrayList3 = this.mConfigs;
                    ConfigItem configItem3 = new ConfigItem(getLayoutType(i), getIDByConfig((String) asList.get(1)), Float.parseFloat((String) asList.get(0)), Float.parseFloat((String) asList.get(2)), getDescription(asList));
                    arrayList3.add(configItem3);
                    i++;
                }
            }
            this.mConfigs.sort(C0239O0000OoO.INSTANCE);
            StringBuilder sb = new StringBuilder();
            sb.append("getSelectWindowConfig:");
            sb.append(this.mConfigs.toString());
            Log.d(TAG, sb.toString());
        }
    }

    public static synchronized DualVideoConfigManager instance() {
        DualVideoConfigManager dualVideoConfigManager;
        synchronized (DualVideoConfigManager.class) {
            if (mManager == null) {
                mManager = new DualVideoConfigManager();
            }
            dualVideoConfigManager = mManager;
        }
        return dualVideoConfigManager;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x008f, code lost:
        if (r5.getZoomById(((java.lang.Integer) r0.get(com.android.camera.dualvideo.util.RenderSourceType.SUB)).intValue()) > r5.getZoomById(((java.lang.Integer) r0.get(com.android.camera.dualvideo.util.RenderSourceType.MAIN)).intValue())) goto L_0x0065;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static ConcurrentHashMap sortId(Integer[] numArr) {
        Assert.check(numArr.length == 2);
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        if (Stream.of(numArr).anyMatch(O0000Oo.INSTANCE)) {
            Stream.of(numArr).forEach(new O0000O0o(concurrentHashMap));
            return concurrentHashMap;
        }
        concurrentHashMap.put(RenderSourceType.MAIN, numArr[0]);
        concurrentHashMap.put(RenderSourceType.SUB, numArr[1]);
        if (Camera2DataContainer.getInstance().isFrontCameraId(((Integer) concurrentHashMap.get(RenderSourceType.SUB)).intValue())) {
            return concurrentHashMap;
        }
        if (!Camera2DataContainer.getInstance().isFrontCameraId(((Integer) concurrentHashMap.get(RenderSourceType.MAIN)).intValue())) {
            DualVideoConfigManager instance = instance();
        }
        switchMainSubId(concurrentHashMap);
        return concurrentHashMap;
    }

    private static void switchMainSubId(ConcurrentHashMap concurrentHashMap) {
        int intValue = ((Integer) concurrentHashMap.get(RenderSourceType.MAIN)).intValue();
        concurrentHashMap.put(RenderSourceType.MAIN, Integer.valueOf(((Integer) concurrentHashMap.get(RenderSourceType.SUB)).intValue()));
        concurrentHashMap.put(RenderSourceType.SUB, Integer.valueOf(intValue));
    }

    public boolean addExternalConfig(int i, String str) {
        if (this.mConfigs.stream().anyMatch(new O00000Oo(i))) {
            return false;
        }
        ArrayList arrayList = this.mConfigs;
        ConfigItem configItem = new ConfigItem(getLayoutType(arrayList.size()), i, 1.0f, 1.0f, str);
        arrayList.add(configItem);
        return true;
    }

    public int getCameraId(LayoutType layoutType) {
        return ((Integer) this.mConfigs.stream().filter(new O0000OOo(layoutType)).findFirst().map(O00000o.INSTANCE).orElse(Integer.valueOf(-1))).intValue();
    }

    public String getConfigDescription(LayoutType layoutType) {
        return (String) this.mConfigs.stream().filter(new O0000o00(layoutType)).findFirst().map(C0240O0000Ooo.INSTANCE).orElse(null);
    }

    public ArrayList getConfigs() {
        return this.mConfigs;
    }

    public FaceType getFaceType(LayoutType layoutType) {
        return (FaceType) this.mConfigs.stream().filter(new C0241O0000o0O(layoutType)).findFirst().map(O0000o0.INSTANCE).orElse(FaceType.FACE_BACK);
    }

    public float getMinZoom() {
        return ((Float) this.mConfigs.stream().filter(C0237O00000oO.INSTANCE).findFirst().map(O000000o.INSTANCE).orElse(Float.valueOf(0.6f))).floatValue();
    }

    public float getPresentZoom(LayoutType layoutType) {
        return ((Float) this.mConfigs.stream().filter(new C0242O0000o0o(layoutType)).findFirst().map(C0244O0000oO0.INSTANCE).orElse(Float.valueOf(1.0f))).floatValue();
    }

    public void removeExternalConfig(int i) {
        this.mConfigs.removeIf(new O0000Oo0(i));
    }
}
