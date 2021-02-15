package com.ss.android.ttve.model;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

@Keep
public class VEMusicWaveBean {
    private ArrayList waveBean;

    @Nullable
    public ArrayList getWaveBean() {
        return this.waveBean;
    }

    public void setWaveBean(@NonNull ArrayList arrayList) {
        this.waveBean = arrayList;
    }
}
