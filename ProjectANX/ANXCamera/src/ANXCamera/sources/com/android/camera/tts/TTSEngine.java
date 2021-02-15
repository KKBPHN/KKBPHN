package com.android.camera.tts;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;

public interface TTSEngine {
    int getStream();

    void initialize(Context context, OnInitListener onInitListener);

    boolean isInitialized();

    boolean isSpeaking();

    void setOnUtteranceProgressListener(UtteranceProgressListener utteranceProgressListener);

    void shutdown();

    int speak(CharSequence charSequence, int i, Bundle bundle, String str);

    void stop();
}
