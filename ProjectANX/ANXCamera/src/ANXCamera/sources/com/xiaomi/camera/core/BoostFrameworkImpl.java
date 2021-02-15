package com.xiaomi.camera.core;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraIntentManager;
import com.android.camera.log.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class BoostFrameworkImpl implements BaseBoostFramework {
    private static final String BOOST_CLASS_NAME_MTK = "android.util.MtkBoostFramework";
    private static final String BOOST_CLASS_NAME_QUALCOM = "android.util.BoostFramework";
    private static final String BOOST_METHOD_NAME = "perfLockAcquire";
    private static final String BOOST_PARAM_NAME;
    private static final String BOOST_STOP_NAME = "perfLockRelease";
    private static final String TAG = "BoostFrameworkImpl";
    private static boolean isInited;
    private static Class mBoostClass = null;
    private static Method mStartBoost = null;
    private static Method mStopBoost = null;
    private int[] mDefaultParams = new int[0];
    private int[] mDefultMtkPerfList = {4194304, 2000000, 4194560, 2000000, 16777216, 0, 12582912, 0};
    private int[] mDefultQComPerfList = {1082146816, 4095, 1082147072, 4095, 1082130432, 4095, 1082130688, 4095, 1082130944, 4095, 1082147328, 4095};
    private Object mPerf = null;
    private int[] mVideoPerfList = {1082130432, 4095, 1082130688, 4095, 1086455808, 20, 1086439424, 30};

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(C0124O00000oO.O0Ooo0o);
        sb.append("_boost_params");
        BOOST_PARAM_NAME = sb.toString();
    }

    public BoostFrameworkImpl() {
        synchronized (BoostFrameworkImpl.class) {
            if (!isInited) {
                try {
                    mBoostClass = Class.forName(C0124O00000oO.isMTKPlatform() ? BOOST_CLASS_NAME_MTK : BOOST_CLASS_NAME_QUALCOM);
                    if (mBoostClass != null) {
                        mStartBoost = mBoostClass.getMethod(BOOST_METHOD_NAME, new Class[]{Integer.TYPE, int[].class});
                        mStopBoost = mBoostClass.getMethod(BOOST_STOP_NAME, new Class[0]);
                    }
                    int[] boostParams = getBoostParams();
                    if (boostParams != null) {
                        this.mDefaultParams = boostParams;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isInited = true;
            }
        }
        try {
            if (mBoostClass != null) {
                Constructor constructor = mBoostClass.getConstructor(new Class[0]);
                constructor.setAccessible(true);
                this.mPerf = constructor.newInstance(new Object[0]);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private int[] getBoostParams() {
        int identifier = CameraAppImpl.getAndroidContext().getResources().getIdentifier(BOOST_PARAM_NAME, "array", CameraIntentManager.CALLER_MIUI_CAMERA);
        if (identifier > 0) {
            return CameraAppImpl.getAndroidContext().getResources().getIntArray(identifier);
        }
        return null;
    }

    private boolean startBoostInternal(int i, int[] iArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("startBoostInternal ");
        sb.append(i);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (this.mPerf == null) {
            Log.d(str, "not init boost pref");
            return false;
        }
        try {
            if (mStartBoost != null) {
                Log.d(str, "ready to boost");
                mStartBoost.setAccessible(true);
                mStartBoost.invoke(this.mPerf, new Object[]{Integer.valueOf(i), iArr});
                return true;
            }
        } catch (Exception e) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("start boost exception ");
            sb3.append(e);
            Log.e(str, sb3.toString());
            e.printStackTrace();
        }
        return false;
    }

    private void stopBoostInternal() {
        String str = TAG;
        Log.d(str, "stopBoostInternal:");
        if (this.mPerf == null) {
            Log.d(str, "not init boost pref, not need to stop");
            return;
        }
        try {
            if (mStopBoost != null) {
                Log.d(str, "ready to stop boost");
                mStopBoost.setAccessible(true);
                mStopBoost.invoke(this.mPerf, new Object[0]);
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("stop boost exception ");
            sb.append(e);
            Log.e(str, sb.toString());
            e.printStackTrace();
        }
    }

    public boolean startBoost(int i, int i2) {
        int[] iArr = new int[0];
        if (i2 == 0 || i2 == 1) {
            iArr = this.mVideoPerfList;
        } else if (i2 == 2 || i2 == 3 || i2 == 4) {
            int[] iArr2 = this.mDefaultParams;
            if (iArr2 == null || iArr2.length == 0) {
                this.mDefaultParams = C0124O00000oO.isMTKPlatform() ? this.mDefultMtkPerfList : this.mDefultQComPerfList;
            }
            iArr = this.mDefaultParams;
        }
        return startBoost(i, iArr);
    }

    public boolean startBoost(int i, int[] iArr) {
        return startBoostInternal(i, iArr);
    }

    public void stopBoost() {
        stopBoostInternal();
    }
}
