package com.android.camera;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import com.android.camera.log.Log;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public class AutoLockManager {
    public static int HIBERNATION_TIMEOUT = 3;
    private static long MILLIS_IN_MINUTE = 60000;
    private static final int MSG_HIBERNATE = 1;
    private static final int MSG_LOCK_SCREEN = 0;
    private static final String TAG = "AutoLockManager";
    private static WeakHashMap sMap = new WeakHashMap();
    private boolean mCameraAlwaysKeepScreenOn = false;
    private WeakReference mContext;
    private Handler mHandler;
    private long mHibernationTimeOut;
    private boolean mPaused;
    private long mScreenOffTimeOut = 15000;

    private AutoLockManager(Context context) {
        long j;
        long j2;
        this.mContext = new WeakReference(context);
        int i = SystemProperties.getInt("camera.debug.hibernation_timeout_seconds", 0);
        this.mCameraAlwaysKeepScreenOn = SystemProperties.getBoolean("camera_always_keep_screen_on", false);
        if (i > 0) {
            j = 1000;
            j2 = (long) i;
        } else {
            j2 = MILLIS_IN_MINUTE;
            j = (long) C0124O00000oO.OOoo0O0();
        }
        this.mHibernationTimeOut = j2 * j;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mHibernationTimeOut = ");
        sb.append(this.mHibernationTimeOut);
        sb.append(", mScreenOffTimeOut = ");
        sb.append(this.mScreenOffTimeOut);
        Log.v(str, sb.toString());
        updateScreenOffTimeout(context);
    }

    public static AutoLockManager getInstance(Context context) {
        AutoLockManager autoLockManager = (AutoLockManager) sMap.get(context);
        if (autoLockManager != null) {
            return autoLockManager;
        }
        AutoLockManager autoLockManager2 = new AutoLockManager(context);
        sMap.put(context, autoLockManager2);
        return autoLockManager2;
    }

    /* access modifiers changed from: private */
    public void hibernate() {
        Context context = (Context) this.mContext.get();
        if (!this.mPaused && context != null) {
            ((Camera) context).onHibernate();
        }
    }

    private void initHandler() {
        if (this.mHandler == null) {
            HandlerThread handlerThread = new HandlerThread("my_handler_thread");
            handlerThread.start();
            this.mHandler = new Handler(handlerThread.getLooper()) {
                public void dispatchMessage(Message message) {
                    int i = message.what;
                    if (i == 0) {
                        AutoLockManager.this.lockScreen();
                    } else if (1 == i) {
                        AutoLockManager.this.hibernate();
                    }
                }
            };
        }
    }

    /* access modifiers changed from: private */
    public void lockScreen() {
        if (!this.mPaused) {
            CameraAppImpl.getAndroidContext().sendBroadcast(new Intent("com.miui.app.ExtraStatusBarManager.TRIGGER_TOGGLE_LOCK"));
        }
    }

    public static void removeInstance(Context context) {
        AutoLockManager autoLockManager = (AutoLockManager) sMap.remove(context);
        if (autoLockManager != null) {
            Handler handler = autoLockManager.mHandler;
            if (handler != null) {
                handler.getLooper().quit();
            }
        }
    }

    private void updateScreenOffTimeout(Context context) {
        try {
            this.mScreenOffTimeOut = (long) System.getInt(context.getContentResolver(), "screen_off_timeout");
        } catch (SettingNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void cancelHibernate() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(1);
        }
    }

    public void hibernateDelayed() {
        if (!this.mCameraAlwaysKeepScreenOn && !this.mPaused && this.mHibernationTimeOut < this.mScreenOffTimeOut) {
            initHandler();
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.removeMessages(1);
                Context context = (Context) this.mContext.get();
                if (context != null) {
                    Camera camera = (Camera) context;
                    if (!camera.isRecording()) {
                        this.mHandler.sendEmptyMessageDelayed(1, this.mHibernationTimeOut);
                        Log.v(TAG, "send MSG_HIBERNATE");
                    } else {
                        String str = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("isRecording = ");
                        sb.append(camera.isRecording());
                        Log.w(str, sb.toString());
                    }
                }
            }
        }
    }

    public void lockScreenDelayed() {
        initHandler();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(0);
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("lockScreenDelayed: ");
        sb.append(this.mScreenOffTimeOut);
        Log.d(str, sb.toString());
        this.mHandler.sendEmptyMessageDelayed(0, this.mScreenOffTimeOut);
    }

    public void onPause() {
        this.mPaused = true;
        removeMessage();
    }

    public void onResume() {
        this.mPaused = false;
        Context context = (Context) this.mContext.get();
        if (context != null) {
            updateScreenOffTimeout(context);
        }
    }

    public void onUserInteraction() {
        hibernateDelayed();
    }

    public void removeMessage() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            Log.v(TAG, "removeMessage");
        }
    }
}
