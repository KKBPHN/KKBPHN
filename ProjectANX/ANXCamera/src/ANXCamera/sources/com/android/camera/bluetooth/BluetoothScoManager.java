package com.android.camera.bluetooth;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.provider.Settings.Secure;
import com.android.camera.CameraSettings;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import com.xiaomi.camera.util.SystemProperties;

public class BluetoothScoManager {
    private static final int SCO_OFF = 0;
    private static final int SCO_ON = 1;
    private static final String SETTING_BLUETOOTH_SCO_STATE = "miui_bluetooth_sco_state";
    private static final String TAG = "BluetoothScoManager";
    private IBluetoothSco mBluetoothSco;
    private final Context mContext;

    public BluetoothScoManager(Context context) {
        this.mContext = context;
        this.mBluetoothSco = new BluetoothSco(context);
    }

    public boolean isBluetoothScoOn() {
        return this.mBluetoothSco.isBluetootScoOn();
    }

    public void startBluetoothSco(int i) {
        if (supportBluetoothSco(i)) {
            this.mBluetoothSco.startBluetoothSco();
        }
    }

    public void stopBluetoothSco(int i) {
        if (C0122O00000o.instance().OO0oOoO() || SystemProperties.getBoolean("support_bluetooth_mic", false)) {
            this.mBluetoothSco.stopBluetoothSco();
        } else {
            Log.d(TAG, "(stopBluetoothSco)not supported bluetooth headset mic!");
        }
    }

    public boolean supportBluetoothSco(int i) {
        String str;
        String str2;
        if (!C0122O00000o.instance().OO0oOoO() && !SystemProperties.getBoolean("support_bluetooth_mic", false)) {
            str = TAG;
            str2 = "not supported bluetooth headset mic!";
        } else if (!DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_EAR_PHONE_RADIO, false)) {
            return false;
        } else {
            int i2 = Secure.getInt(this.mContext.getContentResolver(), SETTING_BLUETOOTH_SCO_STATE, -1);
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("system bluetooth sco state:");
            sb.append(i2);
            Log.d(str3, sb.toString());
            if (i2 == 1) {
                return false;
            }
            if (BluetoothAdapter.getDefaultAdapter().getProfileConnectionState(1) != 2) {
                str = TAG;
                str2 = "bluetooth headset no connect:";
            } else {
                if (!(i == 162 || i == 180 || i == 204)) {
                    if (i != 183) {
                        if (i != 184) {
                            return false;
                        }
                        if (DataRepository.dataItemLive().getMimojiStatusManager2().getMimojiRecordState() != 0) {
                            return true;
                        }
                        str = TAG;
                        str2 = "mimoji photo not support bluetoothSco";
                    } else if (!CameraSettings.getCurrentLiveMusic()[1].isEmpty()) {
                        str = TAG;
                        str2 = "MiLive video music not support bluetoothSco";
                    }
                }
                return true;
            }
        }
        Log.d(str, str2);
        return false;
    }
}
