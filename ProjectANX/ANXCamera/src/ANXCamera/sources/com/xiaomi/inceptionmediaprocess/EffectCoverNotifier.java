package com.xiaomi.inceptionmediaprocess;

public interface EffectCoverNotifier {
    void OnReceiveAllComplete();

    void OnReceivePngFile(String str, long j);
}
