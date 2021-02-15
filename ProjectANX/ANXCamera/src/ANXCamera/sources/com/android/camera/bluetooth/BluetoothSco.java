package com.android.camera.bluetooth;

import android.content.Context;
import android.media.AudioManager;
import com.android.camera.log.Log;

public class BluetoothSco implements IBluetoothSco {
    private static final String TAG = "BluetoothSco";
    private AudioManager mAudioManager;
    private final Context mContext;

    public BluetoothSco(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public AudioManager getAudioManager() {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        }
        return this.mAudioManager;
    }

    public boolean isBluetootScoOn() {
        return getAudioManager().isBluetoothScoAvailableOffCall() && getAudioManager().isBluetoothScoOn();
    }

    public void startBluetoothSco() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("(startBluetoothSco)isBluetoothScoAvailableOffCall:");
        sb.append(getAudioManager().isBluetoothScoAvailableOffCall());
        sb.append(",isBluetoothScoOn:");
        sb.append(getAudioManager().isBluetoothScoOn());
        Log.d(str, sb.toString());
        if (getAudioManager().isBluetoothScoAvailableOffCall() && !getAudioManager().isBluetoothScoOn()) {
            getAudioManager().setMode(3);
            getAudioManager().startBluetoothSco();
            Log.d(TAG, "startBluetoothSco");
        }
    }

    public void stopBluetoothSco() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("(stopBluetoothSco)isBluetoothScoAvailableOffCall:");
        sb.append(getAudioManager().isBluetoothScoAvailableOffCall());
        Log.d(str, sb.toString());
        if (getAudioManager().isBluetoothScoAvailableOffCall()) {
            getAudioManager().setMode(0);
            getAudioManager().stopBluetoothSco();
            Log.d(TAG, "stopBluetoothSco");
        }
    }
}
