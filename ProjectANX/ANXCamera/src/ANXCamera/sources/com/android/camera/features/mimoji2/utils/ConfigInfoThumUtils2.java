package com.android.camera.features.mimoji2.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.android.camera.log.Log;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarConfigInfo;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarConfigValue;
import com.arcsoft.avatar2.AvatarEngine;
import com.arcsoft.avatar2.util.AvatarConfigUtils;
import com.arcsoft.avatar2.util.LOG;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class ConfigInfoThumUtils2 {
    private static final String TAG = "ConfigInfoThumUtils2";
    private final float[] mBeardStyleRegion = {522.0f, 628.0f, 160.0f, 326.0f, 200.0f, 200.0f, 1.0f};
    private final float[] mEarRingShapeRegion = {1031.0f, 1240.0f, 649.0f, 543.0f, 200.0f, 200.0f, 1.0f};
    private final float[] mEarShapeRegion = {1031.0f, 1240.0f, 179.0f, 543.0f, 200.0f, 200.0f, 1.0f};
    private final float[] mEyeBrowSharpRegion = {528.0f, 635.0f, 164.0f, 209.0f, 200.0f, 200.0f, 1.0f};
    private final float[] mEyeSharpRegion = {528.0f, 635.0f, 164.0f, 209.0f, 200.0f, 200.0f, 1.0f};
    private ArrayList mEyeWearList;
    private final float[] mEyeWearStyleRegin = {261.0f, 314.0f, 35.0f, 40.0f, 200.0f, 200.0f, 1.0f};
    private final float[] mEyelashStyleRegion = {1306.0f, 1570.0f, 389.0f, 668.0f, 200.0f, 200.0f, 1.0f};
    private final float[] mFaceSharpRegion = {261.0f, 314.0f, 35.0f, 40.0f, 200.0f, 200.0f, 1.0f};
    private final float[] mFrecklesRegion = {601.0f, 723.0f, 201.0f, 274.0f, 200.0f, 200.0f, 1.0f};
    private ArrayList mHairList;
    private final float[] mHairStyleRegion = {270.0f, 325.0f, 23.0f, 37.0f, 220.0f, 220.0f, 1.0f};
    private final String mHat = "Hat";
    private final float[] mHatStyleRegion = {270.0f, 325.0f, 23.0f, 20.0f, 220.0f, 220.0f, 1.0f};
    private final String mHeadWear = "Hea";
    private final float[] mHeadWearStyleRegion = {422.0f, 510.0f, 98.0f, 15.0f, 220.0f, 220.0f, 0.8f};
    private final float[] mMouthSharpRegion = {1198.0f, 1442.0f, 499.0f, 784.0f, 200.0f, 200.0f, 1.0f};
    private final float[] mNevusRegion = {601.0f, 723.0f, 201.0f, 274.0f, 200.0f, 200.0f, 1.0f};
    private boolean mNoEyeWear = false;
    private boolean mNoHair = false;
    private final float[] mNoseShapeRegion = {737.0f, 887.0f, 270.0f, 378.0f, 200.0f, 200.0f, 1.0f};
    private float[] mTempRegion = new float[7];
    private final float[] mnNormalRegion = {200.0f, 200.0f, 0.0f, 0.0f, 200.0f, 200.0f, 1.0f};

    /* JADX WARNING: Removed duplicated region for block: B:42:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00c5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void renderThumb(AvatarEngine avatarEngine, ASAvatarConfigInfo aSAvatarConfigInfo, int i, float[] fArr) {
        float[] fArr2;
        float[] fArr3;
        AvatarEngine avatarEngine2 = avatarEngine;
        ASAvatarConfigInfo aSAvatarConfigInfo2 = aSAvatarConfigInfo;
        int i2 = i;
        this.mNoHair = false;
        this.mNoEyeWear = false;
        String str = TAG;
        if (avatarEngine2 == null) {
            Log.e(str, "mimoji void renderThumb[avatarEngine, info, gender, backColor] avatar null");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("mConfigType : ");
        sb.append(aSAvatarConfigInfo2.configType);
        LOG.d(str, sb.toString());
        int i3 = aSAvatarConfigInfo2.configType;
        if (i3 == 1) {
            fArr2 = this.mHairStyleRegion;
        } else if (i3 != 12) {
            if (i3 != 14) {
                if (i3 != 16) {
                    if (i3 == 18) {
                        fArr3 = this.mEyelashStyleRegion;
                    } else if (i3 == 7) {
                        fArr3 = this.mFrecklesRegion;
                    } else if (i3 == 8) {
                        fArr3 = this.mNevusRegion;
                    } else if (i3 != 9) {
                        switch (i3) {
                            case 20:
                                fArr2 = this.mFaceSharpRegion;
                                break;
                            case 21:
                                fArr3 = this.mEyeSharpRegion;
                                break;
                            case 22:
                                fArr3 = this.mMouthSharpRegion;
                                break;
                            case 23:
                                fArr3 = this.mNoseShapeRegion;
                                break;
                            case 24:
                                break;
                            case 25:
                                fArr3 = this.mEyeBrowSharpRegion;
                                break;
                            default:
                                fArr2 = this.mnNormalRegion;
                                break;
                        }
                    } else {
                        fArr2 = this.mEyeWearStyleRegin;
                    }
                    this.mTempRegion = fArr3;
                    this.mNoEyeWear = true;
                }
                this.mTempRegion = this.mEarRingShapeRegion;
                this.mNoHair = true;
                this.mNoEyeWear = true;
            } else {
                this.mTempRegion = this.mBeardStyleRegion;
                this.mNoEyeWear = true;
                this.mNoHair = true;
            }
            if (this.mNoHair) {
                if (this.mHairList == null) {
                    this.mHairList = avatarEngine2.getConfig(1, i2);
                }
                if (this.mHairList.size() > 0) {
                    ArrayList arrayList = this.mHairList;
                    avatarEngine2.setConfig((ASAvatarConfigInfo) arrayList.get(arrayList.size() - 1));
                }
            }
            if (this.mNoEyeWear) {
                if (this.mEyeWearList == null) {
                    this.mEyeWearList = avatarEngine2.getConfig(9, i2);
                }
                if (this.mEyeWearList.size() > 0) {
                    avatarEngine2.setConfig((ASAvatarConfigInfo) this.mEyeWearList.get(0));
                }
            }
            avatarEngine.setConfig(aSAvatarConfigInfo);
            float[] fArr4 = this.mTempRegion;
            int i4 = (int) fArr4[4];
            int i5 = (int) fArr4[5];
            byte[] bArr = new byte[(i4 * i5 * 4)];
            avatarEngine.renderThumb((int) fArr4[0], (int) fArr4[1], (int) fArr4[2], (int) fArr4[3], bArr, i4, i5, i4 * 4, fArr, fArr4[6]);
            Bitmap createBitmap = Bitmap.createBitmap(i4, i5, Config.ARGB_8888);
            createBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bArr));
            aSAvatarConfigInfo2.thum = createBitmap;
        } else {
            fArr2 = aSAvatarConfigInfo2.name.substring(0, 3).equalsIgnoreCase("Hat") ? this.mHatStyleRegion : this.mHeadWearStyleRegion;
        }
        this.mTempRegion = fArr2;
        if (this.mNoHair) {
        }
        if (this.mNoEyeWear) {
        }
        avatarEngine.setConfig(aSAvatarConfigInfo);
        float[] fArr42 = this.mTempRegion;
        int i42 = (int) fArr42[4];
        int i52 = (int) fArr42[5];
        byte[] bArr2 = new byte[(i42 * i52 * 4)];
        avatarEngine.renderThumb((int) fArr42[0], (int) fArr42[1], (int) fArr42[2], (int) fArr42[3], bArr2, i42, i52, i42 * 4, fArr, fArr42[6]);
        Bitmap createBitmap2 = Bitmap.createBitmap(i42, i52, Config.ARGB_8888);
        createBitmap2.copyPixelsFromBuffer(ByteBuffer.wrap(bArr2));
        aSAvatarConfigInfo2.thum = createBitmap2;
    }

    public void reset(AvatarEngine avatarEngine, ASAvatarConfigValue aSAvatarConfigValue) {
        ASAvatarConfigInfo aSAvatarConfigInfo;
        ASAvatarConfigInfo aSAvatarConfigInfo2;
        if (avatarEngine == null) {
            Log.e(TAG, "mimoji void reset avatar null");
            return;
        }
        if (this.mNoHair) {
            int currentConfigIdWithType = AvatarConfigUtils.getCurrentConfigIdWithType(1, aSAvatarConfigValue);
            if (currentConfigIdWithType == -1) {
                currentConfigIdWithType = 0;
            }
            Iterator it = this.mHairList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    aSAvatarConfigInfo2 = null;
                    break;
                }
                aSAvatarConfigInfo2 = (ASAvatarConfigInfo) it.next();
                if (aSAvatarConfigInfo2.configID == currentConfigIdWithType) {
                    break;
                }
            }
            if (aSAvatarConfigInfo2 != null) {
                avatarEngine.setConfig(aSAvatarConfigInfo2);
            }
        }
        if (this.mNoEyeWear) {
            int currentConfigIdWithType2 = AvatarConfigUtils.getCurrentConfigIdWithType(9, aSAvatarConfigValue);
            if (currentConfigIdWithType2 == -1) {
                currentConfigIdWithType2 = 0;
            }
            Iterator it2 = this.mEyeWearList.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    aSAvatarConfigInfo = null;
                    break;
                }
                aSAvatarConfigInfo = (ASAvatarConfigInfo) it2.next();
                if (aSAvatarConfigInfo.configID == currentConfigIdWithType2) {
                    break;
                }
            }
            if (aSAvatarConfigInfo != null) {
                avatarEngine.setConfig(aSAvatarConfigInfo);
            }
        }
    }
}
