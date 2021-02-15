package com.xiaomi.inceptionmediaprocess;

public interface EffectCameraNotifier {
    void OnNeedStopRecording();

    void OnNotifyRender();

    void OnRecordFailed();

    void OnRecordFinish(String str);

    void OnRecordFinish(String str, long j, long j2);
}
