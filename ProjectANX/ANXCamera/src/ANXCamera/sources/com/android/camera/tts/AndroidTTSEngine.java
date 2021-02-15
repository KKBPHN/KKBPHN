package com.android.camera.tts;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;

class AndroidTTSEngine implements TTSEngine {
    private TextToSpeech mTextToSpeech;

    AndroidTTSEngine() {
    }

    public int getStream() {
        return 3;
    }

    public void initialize(Context context, OnInitListener onInitListener) {
        if (this.mTextToSpeech == null) {
            this.mTextToSpeech = new TextToSpeech(context, onInitListener);
        }
    }

    public boolean isInitialized() {
        return this.mTextToSpeech != null;
    }

    public boolean isSpeaking() {
        TextToSpeech textToSpeech = this.mTextToSpeech;
        if (textToSpeech != null) {
            return textToSpeech.isSpeaking();
        }
        return false;
    }

    public void setOnUtteranceProgressListener(UtteranceProgressListener utteranceProgressListener) {
        this.mTextToSpeech.setOnUtteranceProgressListener(utteranceProgressListener);
    }

    public void shutdown() {
        this.mTextToSpeech.shutdown();
        this.mTextToSpeech = null;
    }

    public int speak(CharSequence charSequence, int i, Bundle bundle, String str) {
        return this.mTextToSpeech.speak(charSequence, i, bundle, str);
    }

    public void stop() {
        TextToSpeech textToSpeech = this.mTextToSpeech;
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }
}
