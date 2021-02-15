package com.android.camera.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.android.camera.ActivityBase;
import com.android.camera.CameraAppImpl;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BluetoothHeadset;
import com.android.camera.statistic.CameraStatUtils;
import java.lang.ref.WeakReference;

public class BluetoothHeadsetImp extends BroadcastReceiver implements BluetoothHeadset {
    private static final String TAG = "BluetoothHeadsetImp";
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothScoManager mBluetoothScoManager = new BluetoothScoManager(CameraAppImpl.getAndroidContext());
    private WeakReference mConnectStateRef;

    public BluetoothHeadsetImp(ActivityBase activityBase) {
        this.mConnectStateRef = new WeakReference(activityBase);
    }

    public static BluetoothHeadset create(ActivityBase activityBase) {
        return new BluetoothHeadsetImp(activityBase);
    }

    private void onBluetoothHeadsetConnected() {
        ActivityBase activityBase = (ActivityBase) this.mConnectStateRef.get();
        if (activityBase != null) {
            BaseModule baseModule = (BaseModule) activityBase.getCurrentModule();
            if (baseModule != null) {
                baseModule.onBluetoothHeadsetConnected();
            }
        }
    }

    private void onBluetoothHeadsetConnecting() {
        ActivityBase activityBase = (ActivityBase) this.mConnectStateRef.get();
        if (activityBase != null) {
            BaseModule baseModule = (BaseModule) activityBase.getCurrentModule();
            if (baseModule != null) {
                baseModule.onBluetoothHeadsetConnecting();
            }
        }
    }

    private void onBluetoothHeadsetDisconnected() {
        ActivityBase activityBase = (ActivityBase) this.mConnectStateRef.get();
        if (activityBase != null) {
            BaseModule baseModule = (BaseModule) activityBase.getCurrentModule();
            if (baseModule != null) {
                baseModule.onBluetoothHeadsetDisconnected();
            }
        }
    }

    private void registerBluetoothHeadsetReceiver(Context context) {
        if (context == null || !isSupportBluetooth()) {
            Log.d(TAG, "Bluetooth is not supported");
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
        context.registerReceiver(this, intentFilter);
    }

    private void unregisterBluetoothHeadsetReceiver(Context context) {
        if (context == null || !isSupportBluetooth()) {
            Log.d(TAG, "Bluetooth is not supported");
        } else {
            context.unregisterReceiver(this);
        }
    }

    public boolean isBluetoothScoOn() {
        BluetoothScoManager bluetoothScoManager = this.mBluetoothScoManager;
        if (bluetoothScoManager != null) {
            return bluetoothScoManager.isBluetoothScoOn();
        }
        return false;
    }

    public boolean isSupportBluetooth() {
        return this.mBluetoothAdapter != null;
    }

    public boolean isSupportBluetoothSco(int i) {
        BluetoothScoManager bluetoothScoManager = this.mBluetoothScoManager;
        if (bluetoothScoManager != null) {
            return bluetoothScoManager.supportBluetoothSco(i);
        }
        return false;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if ("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED".equals(intent.getAction())) {
                int intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
                if (intExtra == 0) {
                    onBluetoothHeadsetDisconnected();
                } else if (intExtra == 1) {
                    onBluetoothHeadsetConnecting();
                } else if (intExtra == 2) {
                    onBluetoothHeadsetConnected();
                }
            }
        }
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(933, this);
        registerBluetoothHeadsetReceiver(CameraAppImpl.getAndroidContext());
    }

    public void startBluetoothSco(int i) {
        BluetoothScoManager bluetoothScoManager = this.mBluetoothScoManager;
        if (bluetoothScoManager != null) {
            bluetoothScoManager.startBluetoothSco(i);
            if (this.mBluetoothScoManager.supportBluetoothSco(i) && !this.mBluetoothScoManager.isBluetoothScoOn()) {
                CameraStatUtils.trackIntoBluetoothSco();
            }
        }
    }

    public void stopBluetoothSco(int i) {
        BluetoothScoManager bluetoothScoManager = this.mBluetoothScoManager;
        if (bluetoothScoManager != null) {
            bluetoothScoManager.stopBluetoothSco(i);
        }
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(933, this);
        unregisterBluetoothHeadsetReceiver(CameraAppImpl.getAndroidContext());
    }
}
