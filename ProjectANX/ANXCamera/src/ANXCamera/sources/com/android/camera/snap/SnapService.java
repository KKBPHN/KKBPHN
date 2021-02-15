package com.android.camera.snap;

import android.annotation.TargetApi;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import com.android.camera.CameraApplicationDelegate;
import com.android.camera.R;
import com.android.camera.ThermalDetector;
import com.android.camera.ThermalDetector.OnThermalNotificationListener;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.storage.Storage;
import java.lang.ref.WeakReference;

@TargetApi(21)
public class SnapService extends Service {
    public static final int END = 101;
    public static final int MAX_DELAY = 5000;
    /* access modifiers changed from: private */
    public static final String TAG = "SnapService";
    private static boolean mScreenOn;
    private final InnerHandler mHandler = new InnerHandler(this);
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (!"android.intent.action.KEYCODE_POWER_UP".equals(intent.getAction())) {
                if (!"android.intent.action.SCREEN_ON".equals(intent.getAction())) {
                    return;
                }
            }
            SnapTrigger.getInstance().handleKeyEvent(26, 0, System.currentTimeMillis());
        }
    };
    private boolean mRegistered;
    private OnThermalNotificationListener mThermalNotificationListener = O000000o.INSTANCE;
    private WakeLock mWakeLock;

    class InnerHandler extends Handler {
        private final WeakReference mService;

        public InnerHandler(SnapService snapService) {
            this.mService = new WeakReference(snapService);
        }

        public void handleMessage(Message message) {
            SnapService snapService = (SnapService) this.mService.get();
            if (message != null && snapService != null && message.what == 101) {
                Log.d(SnapService.TAG, "stop service");
                snapService.destroy();
                snapService.stopSelf();
            }
        }
    }

    static /* synthetic */ void O00000Oo(int i) {
        if (ThermalDetector.getInstance().thermalConstrained() && SnapTrigger.getInstance().isRunning()) {
            SnapTrigger.getInstance().onThermalConstrained();
        }
    }

    /* access modifiers changed from: private */
    public void destroy() {
        unregisterPowerKeyReceiver();
        this.mHandler.removeMessages(101);
        SnapTrigger.destroy();
    }

    private void registerPowerKeyReceiver() {
        if (!this.mRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.KEYCODE_POWER_UP");
            registerReceiver(this.mReceiver, intentFilter);
            this.mRegistered = true;
        }
    }

    private void releaseWakeLock() {
        WakeLock wakeLock = this.mWakeLock;
        if (wakeLock != null) {
            wakeLock.release();
            this.mWakeLock = null;
            Log.d(TAG, "release wakelock");
        }
    }

    public static void setScreenOn(boolean z) {
        mScreenOn = z;
    }

    private void triggerWatchdog() {
        this.mHandler.removeMessages(101);
        this.mHandler.sendEmptyMessageDelayed(101, 5000);
    }

    private void unregisterPowerKeyReceiver() {
        if (this.mRegistered) {
            unregisterReceiver(this.mReceiver);
            this.mRegistered = false;
        }
    }

    public void aquireWakeLock() {
        if (this.mWakeLock == null) {
            this.mWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, TAG);
            this.mWakeLock.acquire();
            Log.d(TAG, "acquire wakelock");
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        aquireWakeLock();
        ThermalDetector.getInstance().onCreate(getBaseContext());
        MistatsWrapper.initialize(CameraApplicationDelegate.getAndroidContext());
        String string = getResources().getString(R.string.camera_snap_mode_title);
        String str = Util.CAMERA_CHANNEL_ID;
        NotificationChannel notificationChannel = new NotificationChannel(str, string, 1);
        notificationChannel.enableLights(false);
        notificationChannel.setShowBadge(false);
        ((NotificationManager) getSystemService("notification")).createNotificationChannel(notificationChannel);
        startForeground(1, new Builder(this, str).setTicker("camera snap service").setWhen(System.currentTimeMillis()).build());
    }

    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
        ThermalDetector.getInstance().unregisterReceiver();
        ThermalDetector.getInstance().onDestroy();
        destroy();
        releaseWakeLock();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d(TAG, "start service");
        Storage.initStorage(this);
        triggerWatchdog();
        if (mScreenOn || intent == null) {
            return super.onStartCommand(intent, i, i2);
        }
        if (SnapTrigger.getInstance().init(this, this.mHandler)) {
            SnapTrigger.getInstance().handleKeyEvent(intent.getIntExtra(SnapKeyReceiver.KEY_CODE, 0), intent.getIntExtra(SnapKeyReceiver.KEY_ACTION, 0), intent.getLongExtra(SnapKeyReceiver.KEY_EVENT_TIME, 0));
            registerPowerKeyReceiver();
        }
        ThermalDetector.getInstance().registerReceiver(this.mThermalNotificationListener);
        return super.onStartCommand(intent, i, i2);
    }
}
