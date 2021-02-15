package com.android.camera.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceViewHolder;
import com.android.camera.CameraSettings;

public class QRCodePreference extends CheckBoxPreference {
    public QRCodePreference(Context context) {
        super(context);
    }

    public QRCodePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public QRCodePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public boolean callChangeListener(Object obj) {
        if (!Boolean.TRUE.equals(obj) || CameraSettings.isQRCodeReceiverAvailable(getContext())) {
            return super.callChangeListener(obj);
        }
        return false;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(16908289);
        if (findViewById != null) {
            findViewById.setEnabled(CameraSettings.isQRCodeReceiverAvailable(getContext()));
        }
    }
}
