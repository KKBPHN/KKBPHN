package com.android.camera;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.app.Activity;
import android.content.ContentResolver;
import android.hardware.display.DisplayManager;
import android.miui.R;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.PowerManager;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.Spline;
import android.view.WindowManager.LayoutParams;
import androidx.annotation.Nullable;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.internal.R.array;
import com.android.internal.R.bool;
import com.android.internal.R.integer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import miui.reflect.Field;
import miui.reflect.NoSuchFieldException;
import miui.util.ReflectionUtils;

public class CameraBrightness implements CameraBrightnessCallback {
    private static final float SCREEN_AUTO_BRIGHTNESS_RATIO = 0.5f;
    private static final String TAG = "CameraBrightness";
    private int mBrightness = -1;
    private int mBrightnessMode = -1;
    private AsyncTask mCameraBrightnessTask;
    private Activity mCurrentActivity;
    private DisplayManager mDisplayManager;
    private boolean mFirstFocusChanged;
    private boolean mPaused;
    private float mScreenAutoBrightnessRatio = 0.0f;
    private boolean mUseDefaultValue;

    class CameraBrightnessTask extends AsyncTask {
        private final float ADJUST_RATIO_BASE = 0.1f;
        private final float ADJUST_RATIO_RANGE = 0.3f;
        private final boolean DEBUG = false;
        private WeakReference mActivityWeakReference;
        private int mBrightnessMode = -1;
        private WeakReference mCallbackWeakReference;
        private boolean mPaused;
        private Spline mPositiveScreenManualBrightnessSpline;
        private float mScreenAutoBrightnessRatioInner;
        private Spline mScreenManualBrightnessSpline;
        private boolean mUseDefaultValue;

        public CameraBrightnessTask(Activity activity, CameraBrightnessCallback cameraBrightnessCallback, boolean z, boolean z2, float f) {
            this.mActivityWeakReference = new WeakReference(activity);
            this.mCallbackWeakReference = new WeakReference(cameraBrightnessCallback);
            this.mPaused = z2;
            this.mUseDefaultValue = z;
            this.mScreenAutoBrightnessRatioInner = f;
        }

        private Spline createManualBrightnessSpline(int[] iArr, int[] iArr2) {
            try {
                int length = iArr.length;
                float[] fArr = new float[length];
                float[] fArr2 = new float[length];
                for (int i = 0; i < length; i++) {
                    fArr[i] = (float) iArr[i];
                    fArr2[i] = (float) iArr2[i];
                }
                return new MonotoneCubicSpline(fArr, fArr2);
            } catch (IllegalArgumentException e) {
                Log.e(CameraBrightness.TAG, "Could not create manual-brightness spline.", (Throwable) e);
                return null;
            }
        }

        private void createSpline() {
            if ((this.mScreenManualBrightnessSpline == null || this.mPositiveScreenManualBrightnessSpline == null) && getAndroidBoolRes("config_manual_spline_available", true)) {
                int[] androidArrayRes = getAndroidArrayRes("config_manualBrightnessRemapIn");
                int[] androidArrayRes2 = getAndroidArrayRes("config_manualBrightnessRemapOut");
                this.mScreenManualBrightnessSpline = createManualBrightnessSpline(androidArrayRes2, androidArrayRes);
                this.mPositiveScreenManualBrightnessSpline = createManualBrightnessSpline(androidArrayRes, androidArrayRes2);
                if (this.mScreenManualBrightnessSpline == null || this.mPositiveScreenManualBrightnessSpline == null) {
                    Log.e(CameraBrightness.TAG, "Error to create manual brightness spline");
                }
            }
        }

        private String execCommand(String str) {
            String str2 = CameraBrightness.TAG;
            long currentTimeMillis = System.currentTimeMillis();
            String str3 = "";
            try {
                Process exec = Runtime.getRuntime().exec(str);
                if (exec.waitFor() != 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("exit value = ");
                    sb.append(exec.exitValue());
                    Log.e(str2, sb.toString());
                    return str3;
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    stringBuffer.append(readLine);
                }
                bufferedReader.close();
                str3 = stringBuffer.toString();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("execCommand lcd value=");
                sb2.append(str3);
                sb2.append(" cost=");
                sb2.append(System.currentTimeMillis() - currentTimeMillis);
                Log.v(str2, sb2.toString());
                return str3;
            } catch (InterruptedException e) {
                Log.e(str2, "execCommand InterruptedException");
                e.printStackTrace();
            } catch (IOException e2) {
                Log.e(str2, "execCommand IOException");
                e2.printStackTrace();
            }
        }

        /* JADX WARNING: type inference failed for: r4v1, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r4v2, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r4v13 */
        /* JADX WARNING: type inference failed for: r4v14 */
        /* JADX WARNING: type inference failed for: r4v15, types: [int[]] */
        /* JADX WARNING: type inference failed for: r4v16 */
        /* JADX WARNING: type inference failed for: r4v17 */
        /* JADX WARNING: type inference failed for: r4v18 */
        /* JADX WARNING: type inference failed for: r4v19 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r4v13
  assigns: []
  uses: []
  mth insns count: 42
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
        	at jadx.core.ProcessClass.process(ProcessClass.java:35)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 4 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int[] getAndroidArrayRes(String str) {
            ? r4;
            String str2;
            String str3;
            ? r42;
            ? r43;
            ? r44 = "I";
            String str4 = CameraBrightness.TAG;
            try {
                r43 = r44;
                r42 = CameraAppImpl.getAndroidContext().getResources().getIntArray(Field.of(array.class, str, (String) r44).getInt(null));
                r43 = r42;
                return r42;
            } catch (NoSuchFieldException e) {
                str2 = e.getMessage();
                r4 = r42;
                Log.e(str4, str2);
                try {
                    return CameraAppImpl.getAndroidContext().getResources().getIntArray(GeneralUtils.miuiResArrayField(str, r4).getInt(null));
                } catch (NoSuchFieldException e2) {
                    str3 = e2.getMessage();
                    Log.e(str4, str3);
                    return new int[]{0, 255};
                } catch (IllegalArgumentException e3) {
                    str3 = e3.getMessage();
                    Log.e(str4, str3);
                    return new int[]{0, 255};
                }
            } catch (IllegalArgumentException e4) {
                str2 = e4.getMessage();
                r4 = r43;
                Log.e(str4, str2);
                return CameraAppImpl.getAndroidContext().getResources().getIntArray(GeneralUtils.miuiResArrayField(str, r4).getInt(null));
            }
        }

        /* JADX WARNING: type inference failed for: r4v1, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r4v2, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r4v11 */
        /* JADX WARNING: type inference failed for: r4v12 */
        /* JADX WARNING: type inference failed for: r4v13, types: [boolean] */
        /* JADX WARNING: type inference failed for: r4v14 */
        /* JADX WARNING: type inference failed for: r4v15 */
        /* JADX WARNING: type inference failed for: r4v16 */
        /* JADX WARNING: type inference failed for: r4v17 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r4v11
  assigns: []
  uses: []
  mth insns count: 40
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
        	at jadx.core.ProcessClass.process(ProcessClass.java:35)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 4 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean getAndroidBoolRes(String str, boolean z) {
            ? r4;
            String str2;
            String str3;
            ? r42;
            ? r43;
            ? r44 = "I";
            String str4 = CameraBrightness.TAG;
            try {
                r43 = r44;
                r42 = CameraAppImpl.getAndroidContext().getResources().getBoolean(Field.of(bool.class, str, (String) r44).getInt(null));
                r43 = r42;
                return r42;
            } catch (NoSuchFieldException e) {
                str2 = e.getMessage();
                r4 = r42;
                Log.e(str4, str2);
                try {
                    return CameraAppImpl.getAndroidContext().getResources().getBoolean(GeneralUtils.miuiResBoolField(str, r4).getInt(null));
                } catch (NoSuchFieldException e2) {
                    str3 = e2.getMessage();
                    Log.e(str4, str3);
                    return z;
                } catch (IllegalArgumentException e3) {
                    str3 = e3.getMessage();
                    Log.e(str4, str3);
                    return z;
                }
            } catch (IllegalArgumentException e4) {
                str2 = e4.getMessage();
                r4 = r43;
                Log.e(str4, str2);
                return CameraAppImpl.getAndroidContext().getResources().getBoolean(GeneralUtils.miuiResBoolField(str, r4).getInt(null));
            }
        }

        /* JADX WARNING: type inference failed for: r2v1, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r2v2, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r2v4 */
        /* JADX WARNING: type inference failed for: r2v5 */
        /* JADX WARNING: type inference failed for: r2v6, types: [int] */
        /* JADX WARNING: type inference failed for: r2v7 */
        /* JADX WARNING: type inference failed for: r2v8 */
        /* JADX WARNING: type inference failed for: r2v9 */
        /* JADX WARNING: type inference failed for: r2v10 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v4
  assigns: []
  uses: []
  mth insns count: 24
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
        	at jadx.core.ProcessClass.process(ProcessClass.java:35)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 4 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int getAndroidIntResource(String str) {
            String str2;
            ? r2;
            ? r22;
            ? r23;
            ? r24 = CameraBrightness.TAG;
            try {
                r23 = r24;
                r22 = CameraAppImpl.getAndroidContext().getResources().getInteger(Field.of(integer.class, str, "I").getInt(null));
                r23 = r22;
                return r22;
            } catch (NoSuchFieldException e) {
                str2 = e.getMessage();
                r2 = r22;
                Log.e(r2, str2);
                return 0;
            } catch (IllegalArgumentException e2) {
                str2 = e2.getMessage();
                r2 = r23;
                Log.e(r2, str2);
                return 0;
            }
        }

        @Nullable
        private Integer getBrightIsAndroidP(LayoutParams layoutParams, Activity activity) {
            int i;
            String str = "screen_brightness_mode";
            ContentResolver contentResolver = activity.getContentResolver();
            try {
                this.mBrightnessMode = System.getInt(contentResolver, str);
            } catch (SettingNotFoundException unused) {
            }
            CameraBrightnessCallback cameraBrightnessCallback = (CameraBrightnessCallback) this.mCallbackWeakReference.get();
            if (C0122O00000o.instance().OOOoOOo() && cameraBrightnessCallback != null) {
                if (!this.mPaused && !this.mUseDefaultValue) {
                    cameraBrightnessCallback.setPreviousBrightnessMode(this.mBrightnessMode);
                    if (this.mBrightnessMode == 1) {
                        System.putInt(contentResolver, str, 0);
                    }
                } else if (cameraBrightnessCallback.getPreviousBrightnessMode() == 1) {
                    System.putInt(contentResolver, str, 1);
                    this.mBrightnessMode = 1;
                }
            }
            String str2 = CameraBrightness.TAG;
            if (cameraBrightnessCallback != null && this.mBrightnessMode == 1) {
                if (!this.mUseDefaultValue && !this.mPaused && this.mScreenAutoBrightnessRatioInner == 0.0f) {
                    Log.d(str2, "adjustBrightnessInAutoMode(0.5)");
                    cameraBrightnessCallback.adjustBrightnessInAutoMode(0.5f);
                } else if (this.mScreenAutoBrightnessRatioInner == 0.5f) {
                    Log.d(str2, "adjustBrightnessInAutoMode(0)");
                    cameraBrightnessCallback.adjustBrightnessInAutoMode(0.0f);
                }
                cameraBrightnessCallback.setBrightness((int) layoutParams.screenBrightness);
                return null;
            } else if (this.mBrightnessMode != 0) {
                return null;
            } else {
                if (this.mUseDefaultValue || this.mPaused) {
                    i = -1;
                } else {
                    try {
                        int i2 = ReflectionUtils.findField(PowerManager.class, "BRIGHTNESS_ON").getInt(PowerManager.class);
                        int i3 = System.getInt(activity.getContentResolver(), "screen_brightness");
                        StringBuilder sb = new StringBuilder();
                        sb.append("android P -> current back light -> ");
                        sb.append(i3);
                        Log.d(str2, sb.toString());
                        float enLargeBrightness = (float) toEnLargeBrightness((int) Math.ceil((double) (((float) (i3 * 256)) / ((float) (i2 + 1)))));
                        float f = layoutParams.screenBrightness;
                        if (f <= 0.0f || enLargeBrightness != ((float) ((int) (f * 255.0f)))) {
                            i = (int) enLargeBrightness;
                        } else {
                            Log.v(str2, "android P -> doInBackground brightness unchanged");
                            return null;
                        }
                    } catch (Exception e) {
                        Log.e(str2, e.toString());
                        return null;
                    }
                }
                return Integer.valueOf(i);
            }
        }

        @Nullable
        private Integer getBrightNotAndroidP(LayoutParams layoutParams) {
            int currentBackLight = getCurrentBackLight();
            StringBuilder sb = new StringBuilder();
            sb.append("current back light -> ");
            sb.append(currentBackLight);
            String sb2 = sb.toString();
            String str = CameraBrightness.TAG;
            Log.d(str, sb2);
            if (currentBackLight <= 0) {
                return null;
            }
            createSpline();
            float f = layoutParams.screenBrightness;
            if (f > 0.0f) {
                float f2 = f * 255.0f;
                Spline spline = this.mPositiveScreenManualBrightnessSpline;
                if (spline != null) {
                    f2 = spline.interpolate(f2);
                }
                if (Math.abs(Math.round(f2) - currentBackLight) <= 1) {
                    Log.v(str, "doInBackground brightness unchanged");
                    return null;
                }
            }
            Spline spline2 = this.mScreenManualBrightnessSpline;
            if (spline2 != null) {
                currentBackLight = (int) spline2.interpolate((float) currentBackLight);
            }
            return Integer.valueOf(toEnLargeBrightness(currentBackLight));
        }

        private int getCurrentBackLight() {
            String str;
            String str2 = null;
            int i = 0;
            while (true) {
                String str3 = "0";
                boolean equals = str3.equals(str2);
                str = CameraBrightness.TAG;
                if ((equals || str2 == null) && i < 3 && !isCancelled()) {
                    str2 = execCommand("cat sys/class/backlight/panel0-backlight/brightness");
                    if (str3.equals(str2) || str2 == null) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            Log.e(str, e.getMessage());
                        }
                        i++;
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("getCurrentBackLight currentSetting=");
            sb.append(str2);
            Log.v(str, sb.toString());
            if (!TextUtils.isEmpty(str2)) {
                int androidIntResource = getAndroidIntResource("config_backlightBits");
                if (androidIntResource <= 0) {
                    androidIntResource = getMiuiIntResource("config_backlightBit");
                }
                int parseFloat = (int) Float.parseFloat(str2);
                if (androidIntResource > 8) {
                    int i2 = androidIntResource - 8;
                    int i3 = 1;
                    if (parseFloat >= (1 << i2)) {
                        i3 = parseFloat >> i2;
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("getCurrentBackLight convert to ");
                    sb2.append(i3);
                    Log.v(str, sb2.toString());
                    return i3;
                }
            }
            return -1;
        }

        /* JADX WARNING: type inference failed for: r2v1, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r2v2, types: [java.lang.String] */
        /* JADX WARNING: type inference failed for: r2v4 */
        /* JADX WARNING: type inference failed for: r2v5 */
        /* JADX WARNING: type inference failed for: r2v6, types: [int] */
        /* JADX WARNING: type inference failed for: r2v7 */
        /* JADX WARNING: type inference failed for: r2v8 */
        /* JADX WARNING: type inference failed for: r2v9 */
        /* JADX WARNING: type inference failed for: r2v10 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v4
  assigns: []
  uses: []
  mth insns count: 24
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
        	at jadx.core.ProcessClass.process(ProcessClass.java:35)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 4 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int getMiuiIntResource(String str) {
            String str2;
            ? r2;
            ? r22;
            ? r23;
            ? r24 = CameraBrightness.TAG;
            try {
                r23 = r24;
                r22 = CameraAppImpl.getAndroidContext().getResources().getInteger(Field.of(R.integer.class, str, "I").getInt(null));
                r23 = r22;
                return r22;
            } catch (NoSuchFieldException e) {
                str2 = e.getMessage();
                r2 = r22;
                Log.e(r2, str2);
                return 0;
            } catch (IllegalArgumentException e2) {
                str2 = e2.getMessage();
                r2 = r23;
                Log.e(r2, str2);
                return 0;
            }
        }

        private int toEnLargeBrightness(int i) {
            return Integer.valueOf(Util.clamp((int) (((double) i) * (((double) (((((float) Util.clamp(i, 0, 185)) / 185.0f) * 0.3f) + 0.1f)) + 1.0d)), 0, 255)).intValue();
        }

        private void updateBrightness(int i) {
            Activity activity = (Activity) this.mActivityWeakReference.get();
            if (activity != null) {
                LayoutParams attributes = activity.getWindow().getAttributes();
                float f = (this.mUseDefaultValue || this.mPaused) ? -1.0f : ((float) i) / 255.0f;
                attributes.screenBrightness = f;
                StringBuilder sb = new StringBuilder();
                sb.append("updateBrightness setting=");
                sb.append(i);
                sb.append(" useDefaultValue=");
                sb.append(this.mUseDefaultValue);
                sb.append(" screenBrightness=");
                sb.append(attributes.screenBrightness);
                Log.v(CameraBrightness.TAG, sb.toString());
                activity.getWindow().setAttributes(attributes);
                CameraBrightnessCallback cameraBrightnessCallback = (CameraBrightnessCallback) this.mCallbackWeakReference.get();
                if (cameraBrightnessCallback != null) {
                    cameraBrightnessCallback.setBrightness(i);
                }
            }
        }

        /* access modifiers changed from: protected */
        public Integer doInBackground(Void... voidArr) {
            StringBuilder sb = new StringBuilder();
            sb.append("doInBackground useDefaultValue=");
            sb.append(this.mUseDefaultValue);
            sb.append(" paused=");
            sb.append(this.mPaused);
            Log.v(CameraBrightness.TAG, sb.toString());
            Activity activity = (Activity) this.mActivityWeakReference.get();
            LayoutParams attributes = activity.getWindow().getAttributes();
            if (VERSION.SDK_INT >= 28) {
                return getBrightIsAndroidP(attributes, activity);
            }
            if (activity != null) {
                return getBrightNotAndroidP(attributes);
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Integer num) {
            if (num != null) {
                CameraBrightnessCallback cameraBrightnessCallback = (CameraBrightnessCallback) this.mCallbackWeakReference.get();
                if (cameraBrightnessCallback != null) {
                    cameraBrightnessCallback.setBrightness(num.intValue());
                }
                if (!isCancelled()) {
                    updateBrightness(num.intValue());
                }
            }
        }
    }

    public class MonotoneCubicSpline extends Spline {
        private float[] mM;
        private float[] mX;
        private float[] mY;

        public MonotoneCubicSpline(float[] fArr, float[] fArr2) {
            if (fArr == null || fArr2 == null || fArr.length != fArr2.length || fArr.length < 2) {
                throw new IllegalArgumentException("There must be at least two control points and the arrays must be of equal length.");
            }
            int length = fArr.length;
            int i = length - 1;
            float[] fArr3 = new float[i];
            float[] fArr4 = new float[length];
            int i2 = 0;
            while (i2 < i) {
                int i3 = i2 + 1;
                float f = fArr[i3] - fArr[i2];
                if (f > 0.0f) {
                    fArr3[i2] = (fArr2[i3] - fArr2[i2]) / f;
                    i2 = i3;
                } else {
                    throw new IllegalArgumentException("The control points must all have strictly increasing X values.");
                }
            }
            fArr4[0] = fArr3[0];
            for (int i4 = 1; i4 < i; i4++) {
                fArr4[i4] = (fArr3[i4 - 1] + fArr3[i4]) * 0.5f;
            }
            fArr4[i] = fArr3[length - 2];
            for (int i5 = 0; i5 < i; i5++) {
                if (fArr3[i5] == 0.0f) {
                    fArr4[i5] = 0.0f;
                    fArr4[i5 + 1] = 0.0f;
                } else {
                    float f2 = fArr4[i5] / fArr3[i5];
                    int i6 = i5 + 1;
                    float f3 = fArr4[i6] / fArr3[i5];
                    if (f2 < 0.0f || f3 < 0.0f) {
                        throw new IllegalArgumentException("The control points must have monotonic Y values.");
                    }
                    float hypot = (float) Math.hypot((double) f2, (double) f3);
                    if (hypot > 9.0f) {
                        float f4 = 3.0f / hypot;
                        fArr4[i5] = f2 * f4 * fArr3[i5];
                        fArr4[i6] = f4 * f3 * fArr3[i5];
                    }
                }
            }
            this.mX = fArr;
            this.mY = fArr2;
            this.mM = fArr4;
        }

        public float interpolate(float f) {
            int length = this.mX.length;
            if (Float.isNaN(f)) {
                return f;
            }
            float[] fArr = this.mX;
            int i = 0;
            if (f <= fArr[0]) {
                return this.mY[0];
            }
            int i2 = length - 1;
            if (f >= fArr[i2]) {
                return this.mY[i2];
            }
            while (true) {
                float[] fArr2 = this.mX;
                int i3 = i + 1;
                if (f < fArr2[i3]) {
                    float f2 = fArr2[i3] - fArr2[i];
                    float f3 = (f - fArr2[i]) / f2;
                    float[] fArr3 = this.mY;
                    float f4 = 2.0f * f3;
                    float f5 = fArr3[i] * (f4 + 1.0f);
                    float[] fArr4 = this.mM;
                    float f6 = f5 + (fArr4[i] * f2 * f3);
                    float f7 = 1.0f - f3;
                    return (f6 * f7 * f7) + (((fArr3[i3] * (3.0f - f4)) + (f2 * fArr4[i3] * (f3 - 1.0f))) * f3 * f3);
                } else if (f == fArr2[i3]) {
                    return this.mY[i3];
                } else {
                    i = i3;
                }
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            int length = this.mX.length;
            sb.append("MonotoneCubicSpline{[");
            for (int i = 0; i < length; i++) {
                String str = ", ";
                if (i != 0) {
                    sb.append(str);
                }
                sb.append("(");
                sb.append(this.mX[i]);
                sb.append(str);
                sb.append(this.mY[i]);
                sb.append(": ");
                sb.append(this.mM[i]);
                sb.append(")");
            }
            sb.append("]}");
            return sb.toString();
        }
    }

    public CameraBrightness(Activity activity) {
        this.mCurrentActivity = activity;
        this.mUseDefaultValue = true;
        this.mFirstFocusChanged = false;
        this.mDisplayManager = (DisplayManager) CameraAppImpl.getAndroidContext().getSystemService(DisplayManager.class);
    }

    private void adjustBrightness() {
        if (this.mCurrentActivity != null && C0124O00000oO.OOoOooO()) {
            cancelLastTask();
            CameraBrightnessTask cameraBrightnessTask = new CameraBrightnessTask(this.mCurrentActivity, this, this.mUseDefaultValue, this.mPaused, this.mScreenAutoBrightnessRatio);
            this.mCameraBrightnessTask = cameraBrightnessTask.execute(new Void[0]);
        }
    }

    private void cancelLastTask() {
        AsyncTask asyncTask = this.mCameraBrightnessTask;
        if (asyncTask != null) {
            asyncTask.cancel(true);
            this.mCameraBrightnessTask = null;
        }
    }

    public void adjustBrightnessInAutoMode(float f) {
        try {
            CompatibilityUtils.setTemporaryAutoBrightnessAdjustment(this.mDisplayManager, f);
            this.mScreenAutoBrightnessRatio = f;
        } catch (SecurityException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Meet exception when adjustBrightnessInAutoMode(): ");
            sb.append(e);
            Log.e(TAG, sb.toString());
        }
    }

    public int getCurrentBrightness() {
        return this.mBrightness;
    }

    public float getCurrentBrightnessAuto() {
        return this.mScreenAutoBrightnessRatio;
    }

    public int getCurrentBrightnessManual() {
        return this.mBrightness;
    }

    public int getPreviousBrightnessMode() {
        return this.mBrightnessMode;
    }

    public void onPause() {
        StringBuilder sb = new StringBuilder();
        sb.append("onPause mUseDefaultValue=");
        sb.append(this.mUseDefaultValue);
        Log.v(TAG, sb.toString());
        this.mFirstFocusChanged = false;
        this.mPaused = true;
        if (!this.mUseDefaultValue) {
            this.mUseDefaultValue = true;
            adjustBrightness();
        }
    }

    public void onResume() {
        this.mUseDefaultValue = false;
        this.mPaused = false;
        this.mScreenAutoBrightnessRatio = 0.0f;
        Log.v(TAG, "onResume adjustBrightness");
        adjustBrightness();
    }

    public void onWindowFocusChanged(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("onWindowFocusChanged hasFocus=");
        sb.append(z);
        sb.append(" mFirstFocusChanged=");
        sb.append(this.mFirstFocusChanged);
        Log.v(TAG, sb.toString());
        if (this.mFirstFocusChanged || !z) {
            if (this.mUseDefaultValue == z) {
                this.mUseDefaultValue = !z;
                adjustBrightness();
            }
            return;
        }
        this.mFirstFocusChanged = true;
    }

    public void setBrightness(int i) {
        this.mBrightness = i;
    }

    public void setPreviousBrightnessMode(int i) {
        this.mBrightnessMode = i;
    }
}
