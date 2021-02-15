package com.android.camera;

import com.android.camera.data.DataRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class PictureSizeManager {
    private static final int LIMIT_PICTURE_SIZE = 0;
    private static final int LIMIT_WIDTH_SIZE = 1;
    private static float[] PICTURE_ASPECT_RATIOS = {1.3333333f, 1.7777777f, 1.0f, 2.0f, 2.1666667f, 2.1111112f, 2.0833333f, 2.2222223f};
    private static final ArrayList sPictureList = new ArrayList();

    static {
        Arrays.sort(PICTURE_ASPECT_RATIOS);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.CameraSize>, for r7v0, types: [java.util.List, java.util.List<com.android.camera.CameraSize>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static CameraSize findMaxSizeWithRatio(List<CameraSize> list, float f) {
        CameraSize cameraSize;
        int i = 0;
        int i2 = 0;
        for (CameraSize cameraSize2 : list) {
            if (((double) Math.abs(cameraSize2.getRatio() - f)) < 0.02d && cameraSize2.area() > i * i2) {
                i = cameraSize2.getWidth();
                i2 = cameraSize2.getHeight();
            }
        }
        if (i != 0) {
            cameraSize = new CameraSize(i, i2);
        } else {
            cameraSize = new CameraSize();
        }
        return cameraSize;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x001d, code lost:
        if (r0.isEmpty() != false) goto L_0x001f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static CameraSize getBestPanoPictureSize() {
        CameraSize cameraSize;
        if (CameraSettings.isAspectRatio18_9(Display.getWindowWidth(), Display.getWindowHeight())) {
            cameraSize = findMaxSizeWithRatio(sPictureList, 2.0f);
        }
        cameraSize = findMaxSizeWithRatio(sPictureList, 1.7777777f);
        return cameraSize.isEmpty() ? new CameraSize(((CameraSize) sPictureList.get(0)).width, ((CameraSize) sPictureList.get(0)).height) : cameraSize;
    }

    public static CameraSize getBestPictureSize(float f) {
        return getBestPictureSize((List) sPictureList, f);
    }

    public static CameraSize getBestPictureSize(int i) {
        return getBestPictureSize((List) sPictureList, i);
    }

    public static CameraSize getBestPictureSize(List list, float f) {
        if (list.isEmpty()) {
            return new CameraSize();
        }
        CameraSize cameraSize = null;
        float[] fArr = PICTURE_ASPECT_RATIOS;
        int length = fArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            float f2 = fArr[i];
            if (((double) Math.abs(f - f2)) < 0.02d) {
                cameraSize = findMaxSizeWithRatio(list, f2);
                break;
            }
            i++;
        }
        if (cameraSize == null || cameraSize.isEmpty()) {
            cameraSize = new CameraSize(((CameraSize) list.get(0)).width, ((CameraSize) list.get(0)).height);
        }
        return cameraSize;
    }

    public static CameraSize getBestPictureSize(List list, float f, int i) {
        return getBestPictureSize(list, f, i, 0);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.CameraSize>, for r10v0, types: [java.util.List, java.util.List<com.android.camera.CameraSize>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static CameraSize getBestPictureSize(List<CameraSize> list, float f, int i, int i2) {
        float f2;
        CameraSize cameraSize;
        if (list.isEmpty()) {
            return new CameraSize();
        }
        float[] fArr = PICTURE_ASPECT_RATIOS;
        int length = fArr.length;
        int i3 = 0;
        int i4 = 0;
        while (true) {
            if (i4 >= length) {
                f2 = -1.0f;
                break;
            }
            f2 = fArr[i4];
            if (((double) Math.abs(f - f2)) < 0.02d) {
                break;
            }
            i4++;
        }
        if (f2 == -1.0f) {
            return new CameraSize();
        }
        int i5 = 0;
        for (CameraSize cameraSize2 : list) {
            if (((double) Math.abs(cameraSize2.getRatio() - f2)) < 0.02d && (i2 != 0 ? !(cameraSize2.getWidth() <= i3 || cameraSize2.getWidth() > i) : !(cameraSize2.area() <= i3 * i5 || cameraSize2.area() > i))) {
                i3 = cameraSize2.getWidth();
                i5 = cameraSize2.getHeight();
            }
        }
        if (i3 != 0) {
            cameraSize = new CameraSize(i3, i5);
        } else {
            cameraSize = new CameraSize();
        }
        return cameraSize;
    }

    public static CameraSize getBestPictureSize(List list, int i) {
        return (list == null || list.isEmpty()) ? new CameraSize() : getBestPictureSize(list, Util.getRatio(CameraSettings.getPictureSizeRatioString(i)));
    }

    public static CameraSize getBestPictureSizeLimitWidth(List list, float f, int i) {
        return getBestPictureSize(list, f, i, 1);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.CameraSize>, for r4v0, types: [java.util.List, java.util.List<com.android.camera.CameraSize>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static CameraSize getBestSquareSize(List<CameraSize> list, int i, boolean z) {
        int i2 = 0;
        if (list == null || list.isEmpty()) {
            return new CameraSize(0, 0);
        }
        for (CameraSize cameraSize : list) {
            if (cameraSize.getWidth() == cameraSize.getHeight()) {
                if (i <= 0 || i >= cameraSize.getWidth() || z) {
                    if (i2 < cameraSize.getWidth()) {
                        i2 = cameraSize.getWidth();
                    }
                }
            }
        }
        return new CameraSize(i2, i2);
    }

    public static void initialize(List list, int i, int i2, int i3) {
        initializeBase(list, 0, i, i2, i3);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.CameraSize>, for r5v0, types: [java.util.List, java.util.List<com.android.camera.CameraSize>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void initializeBase(List<CameraSize> list, int i, int i2, int i3, int i4) {
        float[] fArr;
        sPictureList.clear();
        if (list == 0 || list.size() == 0) {
            throw new IllegalArgumentException("The supported picture size list return from hal is null!");
        }
        if (i2 != 0) {
            ArrayList arrayList = new ArrayList();
            if (i == 0) {
                for (CameraSize cameraSize : list) {
                    if (cameraSize.area() <= i2) {
                        arrayList.add(cameraSize);
                    }
                }
            } else if (i == 1) {
                for (CameraSize cameraSize2 : list) {
                    if (cameraSize2.width <= i2) {
                        arrayList.add(cameraSize2);
                    }
                }
            }
            list = arrayList;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (float f : PICTURE_ASPECT_RATIOS) {
            CameraSize findMaxSizeWithRatio = findMaxSizeWithRatio(list, f);
            if (!findMaxSizeWithRatio.isEmpty()) {
                sPictureList.add(findMaxSizeWithRatio);
                linkedHashMap.put(Float.valueOf(f), findMaxSizeWithRatio);
            }
        }
        if (sPictureList.size() != 0) {
            DataRepository.dataItemConfig().getComponentConfigRatio().initSensorRatio(linkedHashMap, i3, i4);
            return;
        }
        throw new IllegalArgumentException("Not find the desire picture sizes!");
    }

    public static void initializeLimitWidth(List list, int i, int i2, int i3) {
        initializeBase(list, 1, i, i2, i3);
    }
}
