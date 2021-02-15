package com.android.camera.module.impl.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.android.camera.ActivityBase;
import com.android.camera.Camera;
import com.android.camera.CameraAppImpl;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.GalleryConnect;

public class GalleryConnectImpl extends BroadcastReceiver implements GalleryConnect {
    private static final String ACTION_CHANGE_PHOTO_PAGE_BACKGROUND_ALPHA = "com.miui.gallery.ACTION_CHANGE_PHOTO_PAGE_BACKGROUND_ALPHA";
    private static final String CAMERA_PACKAGE = "com.android.camera";
    private static final String KEY_IS_TRANSLUCENT = "is_translucent";
    private static final String TAG = "GalleryConnect";
    private Camera mActivity;
    private long mJumpTime;

    public GalleryConnectImpl(ActivityBase activityBase) {
        this.mActivity = (Camera) activityBase;
    }

    public static GalleryConnectImpl create(ActivityBase activityBase) {
        return new GalleryConnectImpl(activityBase);
    }

    private void registerReceiver(Context context) {
        if (context != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_CHANGE_PHOTO_PAGE_BACKGROUND_ALPHA);
            context.registerReceiver(this, intentFilter);
        }
    }

    private void unregisterReceiver(Context context) {
        if (context != null) {
            context.unregisterReceiver(this);
        }
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && System.currentTimeMillis() - this.mJumpTime >= 400) {
            if (ACTION_CHANGE_PHOTO_PAGE_BACKGROUND_ALPHA.equals(intent.getAction())) {
                boolean booleanExtra = intent.getBooleanExtra(KEY_IS_TRANSLUCENT, false);
                StringBuilder sb = new StringBuilder();
                sb.append("onReceive: ");
                sb.append(booleanExtra);
                Log.e(TAG, sb.toString());
                Camera camera = this.mActivity;
                if (booleanExtra) {
                    camera.resumeActivity(false);
                } else {
                    camera.pauseActivity(false);
                }
            }
        }
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(937, this);
        registerReceiver(CameraAppImpl.getAndroidContext());
    }

    public void setJumpTime(long j) {
        this.mJumpTime = j;
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(937, this);
        unregisterReceiver(CameraAppImpl.getAndroidContext());
    }
}
