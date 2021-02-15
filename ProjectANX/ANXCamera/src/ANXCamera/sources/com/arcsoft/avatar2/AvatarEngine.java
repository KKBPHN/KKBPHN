package com.arcsoft.avatar2;

import android.text.TextUtils;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarConfigInfo;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarConfigType;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarConfigValue;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarProcessInfo;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarProfileInfo;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarProfileResult;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarTongueAnimationParam;
import com.arcsoft.avatar2.AvatarConfig.GetConfigCallback;
import com.arcsoft.avatar2.AvatarConfig.GetSupportConfigTypeCallback;
import com.arcsoft.avatar2.AvatarConfig.UpdateProgressCallback;
import com.arcsoft.avatar2.extrascene.ExtraSceneInfo;
import com.arcsoft.avatar2.util.ASVLOFFSCREEN;
import com.arcsoft.avatar2.util.LOG;
import com.arcsoft.avatar2.util.TimeConsumingUtil;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class AvatarEngine implements AvatarConfig {
    private static final String a = "AvatarEngine";
    /* access modifiers changed from: private */
    public int b = -1;
    private int c = -1;
    /* access modifiers changed from: private */
    public TreeMap d = new TreeMap();
    private ASAvatarConfigValue e = new ASAvatarConfigValue();
    private long f = 0;
    private String g = "";

    static {
        System.loadLibrary("mimoji_jni");
        System.loadLibrary("mimoji_avatarengine");
    }

    private int a(String str) {
        return nativeSetHeadWearGrayTexture(this.f, str);
    }

    private native int nativeAvatarProcess(long j, int i, int i2, int i3, byte[] bArr, byte[] bArr2, int i4, boolean z, int i5);

    private native int nativeAvatarProcessEx(long j, int i, int i2, int i3, ByteBuffer[] byteBufferArr, byte[] bArr, int i4, boolean z, int i5);

    private native int nativeAvatarProcessEx2(long j, ASVLOFFSCREEN asvloffscreen, byte[] bArr, int i, boolean z, int i2);

    private native int nativeAvatarProfile(long j, String str, int i, int i2, int i3, byte[] bArr, int i4, boolean z, ASAvatarProfileResult aSAvatarProfileResult, ASAvatarProfileInfo aSAvatarProfileInfo, UpdateProgressCallback updateProgressCallback);

    private native int nativeAvatarRender(long j, int i, int i2, int i3, int i4, boolean z, int[] iArr);

    private native long nativeCreate();

    private native int nativeDestroy(long j);

    private native int nativeGetConfig(long j, int i, int i2, GetConfigCallback getConfigCallback);

    private native int nativeGetConfigValue(long j, ASAvatarConfigValue aSAvatarConfigValue);

    private native int nativeGetSupportConfigType(long j, int i, GetSupportConfigTypeCallback getSupportConfigTypeCallback);

    private native String nativeGetVersion();

    private native int nativeInit(long j, String str, String str2);

    private native boolean nativeIsSupportSwitchGender(long j);

    private native int nativeLoadColorValue(String str);

    private native int nativeLoadConfig(long j, String str);

    private native int nativeOutlineCreateEngine(long j, String str);

    private native int nativeOutlineDestroyEngine(long j);

    private native int nativeOutlineProcess(long j, byte[] bArr, int i, int i2, int i3, int i4, ASAvatarProcessInfo aSAvatarProcessInfo);

    private native int nativeOutlineProcessEx(long j, ASVLOFFSCREEN asvloffscreen, int i, ASAvatarProcessInfo aSAvatarProcessInfo);

    private native int nativeProcessOutlineExpression(long j, byte[] bArr, int i, int i2, int i3, int i4, boolean z, int i5, ASAvatarProcessInfo aSAvatarProcessInfo);

    private native int nativeProcessWithInfo(long j, byte[] bArr, int i, int i2, int i3, int i4, boolean z, int i5, ASAvatarProcessInfo aSAvatarProcessInfo);

    private native int nativeProcessWithInfoToEdit(long j, ASVLOFFSCREEN asvloffscreen, int i, boolean z, int i2, ASAvatarProcessInfo aSAvatarProcessInfo, boolean z2);

    private native boolean nativeProcessWithInfoToPreview(long j, ASVLOFFSCREEN asvloffscreen, int i, boolean z, int i2, ASAvatarProcessInfo aSAvatarProcessInfo, boolean z2);

    private native int nativeReadRGBA(long j, String str, int i, int i2, byte[] bArr);

    private native int nativeReleaseRender(long j);

    private native int nativeRenderBackgroundWithImageData(long j, ASVLOFFSCREEN asvloffscreen, int i, boolean z, int[] iArr);

    private native int nativeRenderBackgroundWithTexture(long j, int i, int i2, boolean z, float[] fArr);

    private native int nativeRenderThumb(long j, int i, int i2, int i3, int i4, byte[] bArr, int i5, int i6, int i7, float[] fArr, float f2);

    private native int nativeRenderWithBackground(long j, String str, ASVLOFFSCREEN asvloffscreen, int i, boolean z, int i2, int i3, int i4, int i5, boolean z2, int[] iArr, byte[] bArr, boolean z3);

    private native int nativeSaveConfig(long j, String str);

    private native int nativeSetAnimationParam(long j, boolean z, int i);

    private native int nativeSetConfig(long j, ASAvatarConfigInfo aSAvatarConfigInfo);

    private native int nativeSetHairRenderParam(long j, boolean z, boolean z2);

    private native int nativeSetHeadWearGrayTexture(long j, String str);

    private native int nativeSetHeadWearTexture(long j, String str);

    private native int nativeSetProcessInfo(long j, ASAvatarProcessInfo aSAvatarProcessInfo);

    private native int nativeSetRenderScene(long j, boolean z, float f2, float[] fArr);

    private native int nativeSetTemplate(long j, String str);

    private native int nativeSetTongueAnimationParam(long j, ASAvatarTongueAnimationParam aSAvatarTongueAnimationParam);

    private native int nativeSetToothTexture(long j, String str);

    private native int nativeSwitchGender(long j, boolean z);

    private native int nativeUnInit(long j);

    public synchronized int avatarProcess(int i, int i2, int i3, byte[] bArr, byte[] bArr2, int i4, boolean z, int i5) {
        int nativeAvatarProcess;
        synchronized (this) {
            nativeAvatarProcess = nativeAvatarProcess(this.f, i, i2, i3, bArr, bArr2, i4, z, i5);
        }
        return nativeAvatarProcess;
    }

    public synchronized int avatarProcessEx(int i, int i2, int i3, ByteBuffer[] byteBufferArr, byte[] bArr, int i4, boolean z, int i5) {
        int nativeAvatarProcessEx;
        synchronized (this) {
            nativeAvatarProcessEx = nativeAvatarProcessEx(this.f, i, i2, i3, byteBufferArr, bArr, i4, z, i5);
        }
        return nativeAvatarProcessEx;
    }

    public synchronized int avatarProcessEx2(ASVLOFFSCREEN asvloffscreen, byte[] bArr, int i, boolean z, int i2) {
        return nativeAvatarProcessEx2(this.f, asvloffscreen, bArr, i, z, i2);
    }

    public int avatarProcessWithInfo(byte[] bArr, int i, int i2, int i3, int i4, boolean z, int i5, ASAvatarProcessInfo aSAvatarProcessInfo) {
        String str = "avatarProcessWithInfo";
        TimeConsumingUtil.startTheTimer(str);
        int nativeProcessWithInfo = nativeProcessWithInfo(this.f, bArr, i, i2, i3, i4, z, i5, aSAvatarProcessInfo);
        TimeConsumingUtil.stopTiming("performance", str);
        return nativeProcessWithInfo;
    }

    public int avatarProcessWithInfoEx(ASVLOFFSCREEN asvloffscreen, int i, boolean z, int i2, ASAvatarProcessInfo aSAvatarProcessInfo, boolean z2) {
        return nativeProcessWithInfoToEdit(this.f, asvloffscreen, i, z, i2, aSAvatarProcessInfo, z2);
    }

    public synchronized int avatarProfile(String str, int i, int i2, int i3, byte[] bArr, int i4, boolean z, ASAvatarProfileResult aSAvatarProfileResult, ASAvatarProfileInfo aSAvatarProfileInfo, UpdateProgressCallback updateProgressCallback) {
        int nativeAvatarProfile;
        synchronized (this) {
            nativeAvatarProfile = nativeAvatarProfile(this.f, str, i, i2, i3, bArr, i4, z, aSAvatarProfileResult, aSAvatarProfileInfo, updateProgressCallback);
        }
        return nativeAvatarProfile;
    }

    public synchronized void avatarRender(int i, int i2, int i3, int i4, boolean z, int[] iArr) {
        nativeAvatarRender(this.f, i, i2, i3, i4, z, iArr);
    }

    public int checkOutlineInfo(ASAvatarProcessInfo aSAvatarProcessInfo) {
        String str;
        String str2 = "CheckOutLine";
        if (aSAvatarProcessInfo == null) {
            str = "null";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("faceCount = ");
            sb.append(aSAvatarProcessInfo.getFaceCount());
            LOG.d(str2, sb.toString());
            if (aSAvatarProcessInfo.getFaceCount() > 1) {
                return 10;
            }
            if (aSAvatarProcessInfo.shelterIsNull()) {
                str = "shelterFlags == null";
            } else if (aSAvatarProcessInfo.getFaceCount() <= 0) {
                return 1;
            } else {
                return aSAvatarProcessInfo.checkOutLineInfo();
            }
        }
        LOG.d(str2, str);
        return 1;
    }

    public synchronized int createOutlineEngine(String str) {
        return nativeOutlineCreateEngine(this.f, str);
    }

    public synchronized void destroy() {
        TimeConsumingUtil.startTheTimer("destroy");
        nativeDestroy(this.f);
        this.f = 0;
        TimeConsumingUtil.stopTiming("performance", "destroy");
    }

    public synchronized int destroyOutlineEngine() {
        return nativeOutlineDestroyEngine(this.f);
    }

    public synchronized ArrayList getConfig(final int i, int i2) {
        final ArrayList arrayList;
        arrayList = new ArrayList();
        nativeGetConfig(this.f, i, i2, new GetConfigCallback() {
            public void onGetConfig(int i, int i2, int i3, int i4, String str, String str2, int i5, int i6, boolean z, boolean z2, boolean z3, float f) {
                ASAvatarConfigInfo aSAvatarConfigInfo = new ASAvatarConfigInfo();
                aSAvatarConfigInfo.configID = i;
                aSAvatarConfigInfo.configType = i3;
                aSAvatarConfigInfo.gender = i4;
                aSAvatarConfigInfo.name = str;
                aSAvatarConfigInfo.configThumbPath = str2;
                aSAvatarConfigInfo.isDefault = z;
                aSAvatarConfigInfo.isValid = z2;
                aSAvatarConfigInfo.isSupportContinuous = z3;
                aSAvatarConfigInfo.continuousValue = f;
                aSAvatarConfigInfo.startColorValue = i5;
                aSAvatarConfigInfo.endColorValue = i6;
                if (i3 == 5 && i2 != -1) {
                    String num = new Integer(i2).toString();
                    if (!AvatarEngine.this.d.containsKey(num)) {
                        AvatarEngine.this.d.put(num, aSAvatarConfigInfo);
                    }
                    if (i2 != AvatarEngine.this.b) {
                        return;
                    }
                }
                arrayList.add(aSAvatarConfigInfo);
                StringBuilder sb = new StringBuilder();
                sb.append("type = ");
                sb.append(i);
                sb.append(" info = ");
                sb.append(aSAvatarConfigInfo.toString());
                LOG.d(AvatarEngine.a, sb.toString());
            }
        });
        return arrayList;
    }

    public synchronized void getConfigValue(ASAvatarConfigValue aSAvatarConfigValue) {
        nativeGetConfigValue(this.f, aSAvatarConfigValue);
        this.b = aSAvatarConfigValue.configFaceColorID;
        this.c = aSAvatarConfigValue.configLipColorID;
    }

    public String getExtraSceneName() {
        ASAvatarConfigValue aSAvatarConfigValue = this.e;
        String str = "";
        if (aSAvatarConfigValue == null) {
            return str;
        }
        getConfigValue(aSAvatarConfigValue);
        int i = this.e.configHeadwearStyleID;
        if (i == 13) {
            return ExtraSceneInfo.RABBIT_EARS;
        }
        switch (i) {
            case 20:
                return ExtraSceneInfo.ANGEL_WINGS_CAP;
            case 21:
                return ExtraSceneInfo.CHICKEN_HAT;
            case 22:
                return ExtraSceneInfo.WIZARD_HAT;
            case 23:
                return ExtraSceneInfo.MI_RABBIT_HAT;
            default:
                switch (i) {
                    case 25:
                        return "ClownHat";
                    case 26:
                        return ExtraSceneInfo.EMOJI_HELMET;
                    case 27:
                        return ExtraSceneInfo.SHIBAINU_HAT;
                    case 28:
                        return ExtraSceneInfo.ASTRONAUT_HELMET;
                    case 29:
                        return ExtraSceneInfo.PRINCESS_HAT;
                    default:
                        return str;
                }
        }
    }

    public synchronized ArrayList getSupportConfigType(int i) {
        final ArrayList arrayList;
        arrayList = new ArrayList();
        nativeGetSupportConfigType(this.f, i, new GetSupportConfigTypeCallback() {
            public void onGetSupportConfigType(String str, int i) {
                ASAvatarConfigType aSAvatarConfigType = new ASAvatarConfigType();
                aSAvatarConfigType.configType = i;
                aSAvatarConfigType.configTypeDesc = str;
                arrayList.add(aSAvatarConfigType);
            }
        });
        return arrayList;
    }

    public String getTemplatePath() {
        return this.g;
    }

    public String getVersion() {
        return nativeGetVersion();
    }

    public synchronized void init(String str, String str2) {
        TimeConsumingUtil.startTheTimer("init");
        this.f = nativeCreate();
        int nativeInit = nativeInit(this.f, str, str2);
        String str3 = a;
        StringBuilder sb = new StringBuilder();
        sb.append("init res = ");
        sb.append(nativeInit);
        LOG.d(str3, sb.toString());
        TimeConsumingUtil.stopTiming("performance", "init");
    }

    public synchronized boolean isSupportSwitchGender() {
        return nativeIsSupportSwitchGender(this.f);
    }

    public synchronized int loadColorValue(String str) {
        return nativeLoadColorValue(str);
    }

    public synchronized void loadConfig(String str) {
        TimeConsumingUtil.startTheTimer("loadConfig");
        nativeLoadConfig(this.f, str);
        TimeConsumingUtil.stopTiming("performance", "loadConfig");
    }

    public boolean nativeProcessWithInfoToPreview(ASVLOFFSCREEN asvloffscreen, int i, boolean z, int i2, ASAvatarProcessInfo aSAvatarProcessInfo, boolean z2) {
        return nativeProcessWithInfoToPreview(this.f, asvloffscreen, i, z, i2, aSAvatarProcessInfo, z2);
    }

    public synchronized int outlineProcess(byte[] bArr, int i, int i2, int i3, int i4) {
        ASAvatarProcessInfo aSAvatarProcessInfo;
        aSAvatarProcessInfo = new ASAvatarProcessInfo();
        TimeConsumingUtil.startTheTimer("outlineProcess");
        int nativeOutlineProcess = nativeOutlineProcess(this.f, bArr, i, i2, i3, i4, aSAvatarProcessInfo);
        TimeConsumingUtil.stopTiming("performance", "outlineProcess");
        StringBuilder sb = new StringBuilder();
        sb.append("nativeOutlineProcess = ");
        sb.append(nativeOutlineProcess);
        LOG.d("CheckOutLine", sb.toString());
        return checkOutlineInfo(aSAvatarProcessInfo);
    }

    public synchronized int outlineProcessEx(ASVLOFFSCREEN asvloffscreen, int i) {
        ASAvatarProcessInfo aSAvatarProcessInfo;
        aSAvatarProcessInfo = new ASAvatarProcessInfo();
        TimeConsumingUtil.startTheTimer("outlineProcessEx");
        int nativeOutlineProcessEx = nativeOutlineProcessEx(this.f, asvloffscreen, i, aSAvatarProcessInfo);
        TimeConsumingUtil.stopTiming("performance", "outlineProcessEx");
        StringBuilder sb = new StringBuilder();
        sb.append("nativeOutlineProcess = ");
        sb.append(nativeOutlineProcessEx);
        LOG.d("CheckOutLine", sb.toString());
        return checkOutlineInfo(aSAvatarProcessInfo);
    }

    public synchronized int processOutlineExpression(byte[] bArr, int i, int i2, int i3, int i4, boolean z, int i5, ASAvatarProcessInfo aSAvatarProcessInfo) {
        int nativeProcessOutlineExpression;
        synchronized (this) {
            nativeProcessOutlineExpression = nativeProcessOutlineExpression(this.f, bArr, i, i2, i3, i4, z, i5, aSAvatarProcessInfo);
        }
        return nativeProcessOutlineExpression;
    }

    public int readRGBA(String str, int i, int i2, byte[] bArr) {
        return nativeReadRGBA(this.f, str, i, i2, bArr);
    }

    public synchronized void releaseRender() {
        TimeConsumingUtil.startTheTimer("releaseRender");
        nativeReleaseRender(this.f);
        TimeConsumingUtil.stopTiming("performance", "releaseRender");
    }

    public int renderBackgroundWithImageData(ASVLOFFSCREEN asvloffscreen, int i, boolean z, int[] iArr) {
        return nativeRenderBackgroundWithImageData(this.f, asvloffscreen, i, z, iArr);
    }

    public int renderBackgroundWithTexture(int i, int i2, boolean z, float[] fArr) {
        return nativeRenderBackgroundWithTexture(this.f, i, i2, z, fArr);
    }

    public synchronized int renderThumb(int i, int i2, int i3, int i4, byte[] bArr, int i5, int i6, int i7, float[] fArr, float f2) {
        int nativeRenderThumb;
        synchronized (this) {
            TimeConsumingUtil.startTheTimer("renderThumb");
            nativeRenderThumb = nativeRenderThumb(this.f, i, i2, i3, i4, bArr, i5, i6, i7, fArr, f2);
            TimeConsumingUtil.stopTiming("performance", "renderThumb");
        }
        return nativeRenderThumb;
    }

    public int renderWithBackground(String str, ASVLOFFSCREEN asvloffscreen, int i, boolean z, int i2, int i3, int i4, int i5, boolean z2, int[] iArr, byte[] bArr, boolean z3) {
        return nativeRenderWithBackground(this.f, str, asvloffscreen, i, z, i2, i3, i4, i5, z2, iArr, bArr, z3);
    }

    public synchronized int saveConfig(String str) {
        return nativeSaveConfig(this.f, str);
    }

    public void setAnimationParam(boolean z, int i) {
        nativeSetAnimationParam(this.f, z, i);
    }

    public synchronized int setConfig(ASAvatarConfigInfo aSAvatarConfigInfo) {
        if (aSAvatarConfigInfo.configType == 3) {
            this.b = aSAvatarConfigInfo.configID;
            String num = new Integer(this.b).toString();
            if (this.d.size() > 0 && this.d.containsKey(num)) {
                boolean z = false;
                Iterator it = this.d.values().iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (((ASAvatarConfigInfo) it.next()).configID == this.c) {
                            z = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (z) {
                    nativeSetConfig(this.f, (ASAvatarConfigInfo) this.d.get(num));
                }
            }
        } else if (aSAvatarConfigInfo.configType == 5) {
            this.c = aSAvatarConfigInfo.configID;
        }
        return nativeSetConfig(this.f, aSAvatarConfigInfo);
    }

    public int setHairRenderParam(boolean z, boolean z2) {
        return nativeSetHairRenderParam(this.f, z, z2);
    }

    public void setHeadWearGrayPath(ASAvatarConfigInfo aSAvatarConfigInfo) {
        if (!TextUtils.isEmpty(this.g) && aSAvatarConfigInfo != null && aSAvatarConfigInfo.configType == 12 && aSAvatarConfigInfo.configID == 21) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.g);
            sb.append("/cartoon_HEADWEAR/style21/Tex_21_gray_alpha20.png");
            a(sb.toString());
        }
    }

    public int setHeadWearTexture(String str) {
        return nativeSetHeadWearTexture(this.f, str);
    }

    public int setProcessInfo(ASAvatarProcessInfo aSAvatarProcessInfo) {
        return nativeSetProcessInfo(this.f, aSAvatarProcessInfo);
    }

    public synchronized void setRenderScene(boolean z, float f2) {
        nativeSetRenderScene(this.f, z, f2, null);
    }

    public synchronized void setRenderScene3F(boolean z, float f2, float[] fArr) {
        nativeSetRenderScene(this.f, z, f2, fArr);
    }

    public synchronized void setTemplatePath(String str) {
        TimeConsumingUtil.startTheTimer("setTemplatePath");
        nativeSetTemplate(this.f, str);
        this.g = str;
        TimeConsumingUtil.stopTiming("performance", "setTemplatePath");
    }

    public int setTongueAnimationParam(ASAvatarTongueAnimationParam aSAvatarTongueAnimationParam) {
        return nativeSetTongueAnimationParam(this.f, aSAvatarTongueAnimationParam);
    }

    public int setToothTexture(String str) {
        return nativeSetToothTexture(this.f, str);
    }

    public synchronized void switchGender(boolean z) {
        nativeSwitchGender(this.f, z);
    }

    public synchronized void unInit() {
        TimeConsumingUtil.startTheTimer("unInit");
        int nativeUnInit = nativeUnInit(this.f);
        TimeConsumingUtil.stopTiming("performance", "unInit");
        String str = a;
        StringBuilder sb = new StringBuilder();
        sb.append("uninit res = ");
        sb.append(nativeUnInit);
        LOG.d(str, sb.toString());
    }
}
