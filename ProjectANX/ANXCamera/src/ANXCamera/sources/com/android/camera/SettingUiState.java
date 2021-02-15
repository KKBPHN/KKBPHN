package com.android.camera;

import com.android.camera.log.Log;

public class SettingUiState {
    public boolean isMutexEnable = false;
    public boolean isRomove = false;

    public String toString(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("funcName:");
        sb.append(str);
        sb.append("  isRomove:");
        sb.append(this.isRomove);
        sb.append("  isMutexEnable:");
        sb.append(this.isMutexEnable);
        String sb2 = sb.toString();
        Log.i("SettingUiState", sb2);
        return sb2;
    }
}
