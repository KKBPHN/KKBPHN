package com.xiaomi.asr.engine;

public interface WVPListener {
    void onAbortEnrollmentComplete();

    void onAudioData(byte[] bArr);

    void onCommitEnrollmentComplete();

    void onConflictAudio();

    void onDebug(String str);

    void onEndOfSpeech();

    void onEnergyLevelAvailable(float f, boolean z);

    void onEnrollAudioBufferAvailable(byte[] bArr, boolean z);

    void onEnrollmentComplete(boolean z, boolean z2, float f, int i);

    void onGenerateModel(boolean z, String str);

    void onGrammarUpdated(boolean z);

    void onInit(boolean z);

    void onPhraseSpotted(PhraseWakeupResult phraseWakeupResult);

    void onRelease();

    void onStartAudio();

    void onStartOfSpeech();

    void onStopAudio();
}
